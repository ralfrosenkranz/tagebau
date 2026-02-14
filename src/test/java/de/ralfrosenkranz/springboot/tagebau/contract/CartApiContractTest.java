package de.ralfrosenkranz.springboot.tagebau.contract;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CartApiContractTest extends AbstractOpenApiContractTest {

    private final OpenApiValidationFilter filter =
            openApiFilter("static/tagebau/openapi/openapi-cart.yaml");

    @Test
    void getCart_matchesOpenApi() {
        given()
                .filter(filter)
        .when()
                .get("/api/cart")
        .then()
                .statusCode(200);
    }

    @Test
    void listOrders_matchesOpenApi() {
        given()
                .filter(filter)
        .when()
                .get("/api/orders")
        .then()
                .statusCode(200);
    }

    @Test
    void getOrder_notFound_isDocumented() {
        given()
                .filter(filter)
        .when()
                .get("/api/orders/{orderId}", 999999)
        .then()
                .statusCode(404);
    }

    @Test
    void registerUser_matchesOpenApi() {
        String body = """
                {
                  "username": "testuser",
                  "email": "test@example.com",
                  "password": "secret123"
                }
                """;

        given()
                .filter(filter)
                .contentType("application/json")
                .body(body)
        .when()
                .post("/api/users/register")
        .then()
                .statusCode(201);
    }

    @Test
    void loginUser_matchesOpenApi() {
        String body = """
                {
                  "username": "testuser",
                  "password": "secret123"
                }
                """;

        given()
                .filter(filter)
                .contentType("application/json")
                .body(body)
        .when()
                .post("/api/users/login")
        .then()
                .statusCode(204);
    }
}
