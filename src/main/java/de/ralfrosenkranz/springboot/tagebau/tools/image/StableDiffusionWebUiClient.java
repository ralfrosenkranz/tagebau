package de.ralfrosenkranz.springboot.tagebau.tools.image;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class StableDiffusionWebUiClient {

    private final HttpClient http;
    private final ObjectMapper mapper = new ObjectMapper();
    private final URI baseUrl;

    public StableDiffusionWebUiClient(URI baseUrl) {
        this.baseUrl = baseUrl;
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Calls AUTOMATIC1111 WebUI API: POST /sdapi/v1/txt2img
     * Returns decoded PNG bytes (first image).
     */
    public byte[] txt2img(String prompt, int width, int height, int steps, double cfgScale, String sampler, long seed) throws IOException, InterruptedException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("prompt", prompt);
        payload.put("width", width);
        payload.put("height", height);
        payload.put("steps", steps);
        payload.put("cfg_scale", cfgScale);
        payload.put("sampler_name", sampler);
        payload.put("seed", seed);

        String json = mapper.writeValueAsString(payload);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(baseUrl.resolve("/sdapi/v1/txt2img"))
                .timeout(Duration.ofMinutes(5))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (resp.statusCode() / 100 != 2) {
            throw new IOException("SD WebUI API error " + resp.statusCode() + ": " + resp.body());
        }

        JsonNode root = mapper.readTree(resp.body());
        JsonNode images = root.path("images");
        if (!images.isArray() || images.isEmpty()) {
            throw new IOException("SD WebUI response missing images[]");
        }
        String b64 = images.get(0).asText();
        // sometimes includes data prefix "data:image/png;base64,..."
        int comma = b64.indexOf(',');
        if (comma >= 0 && b64.substring(0, comma).toLowerCase().contains("base64")) {
            b64 = b64.substring(comma + 1);
        }
        return Base64.getDecoder().decode(b64);
    }
}
