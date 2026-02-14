package de.ralfrosenkranz.springboot.tagebau.contract;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.net.URL;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
public abstract class AbstractOpenApiContractTest {

    @LocalServerPort
    int port;

    protected OpenApiValidationFilter openApiFilter(String classpathResource) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(classpathResource);
        if (url == null) {
            throw new IllegalStateException("OpenAPI resource not found on classpath: " + classpathResource);
        }
        return new OpenApiValidationFilter(url.toString());
    }

    @BeforeEach
    void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }
}
