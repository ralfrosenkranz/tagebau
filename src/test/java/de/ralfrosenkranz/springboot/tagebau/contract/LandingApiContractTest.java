package de.ralfrosenkranz.springboot.tagebau.contract;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class LandingApiContractTest extends AbstractOpenApiContractTest {

    private final OpenApiValidationFilter filter =
            openApiFilter("static/tagebau/openapi/openapi-landing.yaml");

    @Test
    void getLanding_matchesOpenApi() {
        given()
                .filter(filter)
        .when()
                .get("/api/landing")
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
    void getTopProducts_matchesOpenApi() {
        given()
                .filter(filter)
        .when()
                .get("/api/products/top")
        .then()
                .statusCode(200);
    }

    @Test
    void searchProducts_matchesOpenApi() {
        given()
                .filter(filter)
                .queryParam("q", "Produkt")
        .when()
                .get("/api/products/search")
        .then()
                .statusCode(200);
    }
}
