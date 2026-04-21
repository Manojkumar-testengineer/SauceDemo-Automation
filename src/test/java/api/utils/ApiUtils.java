package api.utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import utils.ConfigReader;

import java.util.UUID;

public class ApiUtils {

    private static RequestSpecification requestSpec;

    public static RequestSpecification getRequestSpec() {
        if (requestSpec == null) {
            requestSpec = new RequestSpecBuilder()
                    .setBaseUri(ConfigReader.getApiBaseUrl())
                    .setContentType(ContentType.JSON)
                    .log(LogDetail.ALL)
                    .build();
        }
        return requestSpec;
    }

    public static RequestSpecification getAuthRequestSpec(String token) {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.getApiBaseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .log(LogDetail.ALL)
                .build();
    }

    public static String getNewAccessToken() {
        String clientName = "Test Client " + System.currentTimeMillis();
        String clientEmail = "test" + System.currentTimeMillis() + "@example.com";
        String requestBody = "{\n" +
                "  \"clientName\": \"" + clientName + "\",\n" +
                "  \"clientEmail\": \"" + clientEmail + "\"\n" +
                "}";

        Response response = RestAssured
                .given()
                .spec(getRequestSpec())
                .body(requestBody)
                .post("/api-clients");

        if (response.statusCode() != 201) {
            throw new RuntimeException("Failed to get access token. Status: " + response.statusCode()
                    + " | Body: " + response.body().asString());
        }

        return response.jsonPath().getString("accessToken");
    }
}
