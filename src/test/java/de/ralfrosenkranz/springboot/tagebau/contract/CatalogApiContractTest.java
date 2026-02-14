package de.ralfrosenkranz.springboot.tagebau.contract;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CatalogApiContractTest extends AbstractOpenApiContractTest {

    private final OpenApiValidationFilter filter =
            openApiFilter("static/tagebau/openapi/openapi-catalog.yaml");

    @Test
    void getCatalog_matchesOpenApi() {
        given()
                .filter(filter)
        .when()
                .get("/api/catalog")
        .then()
                .statusCode(200);
    }

    @Test
    void getCategories_matchesOpenApi() {
        given()
                .filter(filter)
        .when()
                .get("/api/categories")
        .then()
                .statusCode(200);
    }

    @Test
    void getProductsByCategory_matchesOpenApi() {
        given()
                .filter(filter)
        .when()
                .get("/api/categories/{categoryId}/products", "cat-001")
        .then()
                .statusCode(200);
    }
}
