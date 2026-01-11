# Tagebau Katalog Parser + Stable Diffusion (AUTOMATIC1111) Jobrunner

Dieses Kommandozeilenprogramm:
1. liest eine Katalog-JSON ein,
2. parsed sie in Datenklassen (JPA-annotiert, für spätere SQL-Mappings),
3. verlinkt Referenzen (Product.category_id -> Category),
4. kann optional pro `products[].media.images[]` ein Bild über die AUTOMATIC1111-WebUI-API generieren
   und lokal unter den im JSON definierten Pfaden speichern (plus Thumbnail).

## Build
```bash
mvn -DskipTests package
```

## Parse/Validate (ohne Bilder)
```bash
java -jar target/tagebau-catalog-parser-1.1.0-shaded.jar --parse <katalog.json>
```

## Bilder generieren (Stable Diffusion WebUI / AUTOMATIC1111)
Voraussetzungen:
- AUTOMATIC1111 WebUI läuft lokal und API ist erreichbar (standardmäßig http://127.0.0.1:7860).
  API-Endpunkt: POST /sdapi/v1/txt2img

Run:
```bash
java -jar target/tagebau-catalog-parser-1.1.0-shaded.jar --generate-images <katalog.json> <out-root> \
  --sd-url http://127.0.0.1:7860 --size 1024x1024 --steps 30 --cfg 7.0 \
  --sampler "DPM++ 2M Karras" --concurrency 4 --thumb 256
```

Prompts:
- pro Bild wird `generation_prompt` aus dem JSON verwendet und um
  `Stilattribute: Dramatisch, Heroisch.` ergänzt.

Hinweis:
- Thumbnails werden deterministisch aus dem generierten Bild lokal erstellt (Center-Crop + Downscale).
