package de.ralfrosenkranz.springboot.tagebau.tools.catalog;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.ralfrosenkranz.springboot.tagebau.server.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CatalogParser {

    private final ObjectMapper mapper;

    public CatalogParser() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public Catalog parse(Path jsonFile) throws IOException {
        JsonNode root = mapper.readTree(Files.readString(jsonFile));
        Catalog catalog = new Catalog();
        catalog.setSchemaVersion(text(root, "schema_version"));
        catalog.setGeneratedAt(Instant.parse(text(root, "generated_at")));
        catalog.setNote(root.path("note").isMissingNode() ? null : root.path("note").asText(null));

        // categories
        List<Category> categories = new ArrayList<>();
        for (JsonNode c : root.withArray("categories")) {
            Category cat = new Category();
            cat.setId(text(c, "id"));
            cat.setName(text(c, "name"));
            cat.setCatalog(catalog);
            categories.add(cat);
        }
        catalog.setCategories(categories);

        // products
        List<Product> products = new ArrayList<>();
        for (JsonNode p : root.withArray("products")) {
            Product prod = new Product();
            prod.setId(text(p, "id"));
            prod.setSku(text(p, "sku"));
            prod.setCategoryId(text(p, "category_id"));
            prod.setCategoryName(text(p, "category_name"));
            prod.setTechnicalName(text(p, "technical_name"));
            prod.setNickname(text(p, "nickname"));
            prod.setCondition(text(p, "condition"));
            prod.setShortDescription(p.path("short_description").asText(null));
            prod.setLongDescriptionMarkdown(p.path("long_description_markdown").asText(null));
            prod.setCatalog(catalog);

            // specs
            JsonNode s = p.path("specs");
            if (!s.isMissingNode() && s.isObject()) {
                ProductSpecs specs = new ProductSpecs();
                specs.setProduct(prod);
                specs.setMachineType(s.path("machine_type").asText(null));
                specs.setOperatingWeightT(doubleOrNull(s, "operating_weight_t"));
                specs.setBucketCapacityM3(doubleOrNull(s, "bucket_capacity_m3"));
                specs.setEnginePowerKw(intOrNull(s, "engine_power_kw"));
                specs.setHoursUsed(intOrNull(s, "hours_used"));
                specs.setBoomLengthM(doubleOrNull(s, "boom_length_m"));
                specs.setPayloadT(doubleOrNull(s, "payload_t"));
                specs.setTireSize(s.path("tire_size").asText(null));
                specs.setThroughputTph(doubleOrNull(s, "throughput_tph"));
                specs.setBeltWidthMm(intOrNull(s, "belt_width_mm"));
                specs.setWheelDiameterM(doubleOrNull(s, "wheel_diameter_m"));
                specs.setBucketCount(intOrNull(s, "bucket_count"));
                specs.setBladeCapacityM3(doubleOrNull(s, "blade_capacity_m3"));
                specs.setHoleDiameterMm(intOrNull(s, "hole_diameter_mm"));
                specs.setMaxHoleDepthM(doubleOrNull(s, "max_hole_depth_m"));
                prod.setSpecs(specs);
            }

            // pricing
            JsonNode pr = p.path("pricing");
            if (!pr.isMissingNode() && pr.isObject()) {
                Pricing pricing = new Pricing();
                pricing.setProduct(prod);
                pricing.setCurrency(text(pr, "currency"));
                pricing.setPriceExorbitant(pr.path("price_exorbitant").asLong());
                pricing.setListPriceEvenMoreExorbitant(pr.path("list_price_even_more_exorbitant").asLong());
                pricing.setVatNote(pr.path("vat_note").asText(null));
                prod.setPricing(pricing);
            }

            // inventory
            JsonNode inv = p.path("inventory");
            if (!inv.isMissingNode() && inv.isObject()) {
                Inventory inventory = new Inventory();
                inventory.setProduct(prod);
                inventory.setStockQty(inv.path("stock_qty").asInt());
                inventory.setAvailability(text(inv, "availability"));
                prod.setInventory(inventory);
            }

            // shipping
            JsonNode sh = p.path("shipping");
            if (!sh.isMissingNode() && sh.isObject()) {
                Shipping shipping = new Shipping();
                shipping.setProduct(prod);
                shipping.setShippingCostEur(sh.path("shipping_cost_eur").asInt());
                shipping.setLeadTimeDays(sh.path("lead_time_days").asInt());
                shipping.setIncotermsSuggestion(sh.path("incoterms_suggestion").asText(null));
                shipping.setNotes(sh.path("notes").asText(null));
                prod.setShipping(shipping);
            }

            // media/images
            JsonNode m = p.path("media");
            if (!m.isMissingNode() && m.isObject()) {
                ProductMedia media = new ProductMedia();
                media.setProduct(prod);

                List<MediaImage> imgs = new ArrayList<>();
                for (JsonNode img : m.withArray("images")) {
                    MediaImage mi = new MediaImage();
                    mi.setMedia(media);
                    mi.setRole(text(img, "role"));
                    mi.setLabel(text(img, "label"));
                    mi.setFile(text(img, "file"));
                    mi.setThumbnailFile(text(img, "thumbnail_file"));
                    mi.setGenerationPrompt(img.path("generation_prompt").asText(null));
                    imgs.add(mi);
                }
                media.setImages(imgs);
                prod.setMedia(media);
            }

            products.add(prod);
        }
        catalog.setProducts(products);

        return catalog;
    }

    private static String text(JsonNode node, String field) {
        JsonNode v = node.get(field);
        if (v == null || v.isNull()) throw new IllegalArgumentException("Missing required field: " + field);
        return v.asText();
    }

    private static Integer intOrNull(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return (v == null || v.isNull() || v.isMissingNode()) ? null : v.asInt();
    }

    private static Double doubleOrNull(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return (v == null || v.isNull() || v.isMissingNode()) ? null : v.asDouble();
    }
}
