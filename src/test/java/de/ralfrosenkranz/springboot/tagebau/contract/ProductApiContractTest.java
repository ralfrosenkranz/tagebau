package de.ralfrosenkranz.springboot.tagebau.contract;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ProductApiContractTest extends AbstractOpenApiContractTest {

    private final OpenApiValidationFilter filter =
            openApiFilter("static/tagebau/openapi/openapi-product.yaml");

    @Test
    void getProductDetail_matchesOpenApi() {
        given()
                .filter(filter)
        .when()
                .get("/api/products/{productId}", "prod-0001")
        .then()
                .statusCode(200);
    }

    @Test
    void getProductImages_matchesOpenApi() {
        given()
                .filter(filter)
        .when()
                .get("/api/products/{productId}/media/images", "prod-0001")
        .then()
                .statusCode(200);
    }

    @Test
    void getRelatedProducts_matchesOpenApi() {
        given()
                .filter(filter)
        .when()
                .get("/api/products/{productId}/related", "prod-0001")
        .then()
                .statusCode(200);
    }
}
