package api.steps;

import api.utils.ApiUtils;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ApiSteps {

    private Response response;
    private String accessToken;
    private String orderId;

    @Given("I have a valid API access token")
    public void iHaveAValidApiAccessToken() {
        accessToken = ApiUtils.getNewAccessToken();
        Assert.assertNotNull(accessToken, "Access token should not be null");
        System.out.println("==> Access Token acquired: " + accessToken.substring(0, 20) + "...");
    }

    @Given("I have an existing order")
    public void iHaveAnExistingOrder() {
        String body = "{ \"bookId\": 1, \"customerName\": \"Test Customer\" }";
        Response orderResponse = RestAssured
                .given()
                .spec(ApiUtils.getAuthRequestSpec(accessToken))
                .body(body)
                .post("/orders");

        Assert.assertEquals(orderResponse.statusCode(), 201,
                "Failed to create existing order. Body: " + orderResponse.body().asString());
        orderId = orderResponse.jsonPath().getString("orderId");
        Assert.assertNotNull(orderId, "Order ID should not be null");
        System.out.println("==> Pre-existing Order created with ID: " + orderId);
    }


    @When("I send a GET request to {string}")
    public void iSendAGetRequestTo(String endpoint) {
        response = RestAssured
                .given()
                .spec(ApiUtils.getRequestSpec())
                .get(endpoint);
    }

    @When("I send a GET request to {string} with query param {string} as {string}")
    public void iSendAGetRequestWithQueryParam(String endpoint, String paramKey, String paramValue) {
        response = RestAssured
                .given()
                .spec(ApiUtils.getRequestSpec())
                .queryParam(paramKey, paramValue)
                .get(endpoint);
    }

    @When("I send a GET request to {string} with auth token")
    public void iSendAGetRequestWithAuthToken(String endpoint) {
        response = RestAssured
                .given()
                .spec(ApiUtils.getAuthRequestSpec(accessToken))
                .get(endpoint);
    }

    @When("I register a new API client")
    public void iRegisterANewApiClient() {
        String clientName = "Test Client " + System.currentTimeMillis();
        String clientEmail = "test" + System.currentTimeMillis() + "@example.com";
        String requestBody = "{\n" +
                "  \"clientName\": \"" + clientName + "\",\n" +
                "  \"clientEmail\": \"" + clientEmail + "\"\n" +
                "}";

        response = RestAssured
                .given()
                .spec(ApiUtils.getRequestSpec())
                .body(requestBody)
                .post("/api-clients");
    }

    @When("I submit an order for book ID {int} with customer name {string}")
    public void iSubmitAnOrderForBook(int bookId, String customerName) {
        String body = "{ \"bookId\": " + bookId + ", \"customerName\": \"" + customerName + "\" }";
        response = RestAssured
                .given()
                .spec(ApiUtils.getAuthRequestSpec(accessToken))
                .body(body)
                .post("/orders");

        if (response.statusCode() == 201) {
            orderId = response.jsonPath().getString("orderId");
        }
    }

    @When("I get the order by its ID with auth token")
    public void iGetTheOrderByItsId() {
        Assert.assertNotNull(orderId, "Order ID must be set before fetching");
        response = RestAssured
                .given()
                .spec(ApiUtils.getAuthRequestSpec(accessToken))
                .get("/orders/" + orderId);
    }

    @When("I update the order customer name to {string} using PATCH with auth token")
    public void iUpdateTheOrderCustomerName(String newName) {
        Assert.assertNotNull(orderId, "Order ID must be set before updating");
        String body = "{ \"customerName\": \"" + newName + "\" }";
        response = RestAssured
                .given()
                .spec(ApiUtils.getAuthRequestSpec(accessToken))
                .body(body)
                .patch("/orders/" + orderId);
    }

    @When("I delete the order using DELETE with auth token")
    public void iDeleteTheOrder() {
        Assert.assertNotNull(orderId, "Order ID must be set before deleting");
        response = RestAssured
                .given()
                .spec(ApiUtils.getAuthRequestSpec(accessToken))
                .delete("/orders/" + orderId);
    }

    @When("I submit an order without auth token for book ID {int}")
    public void iSubmitOrderWithoutAuth(int bookId) {
        String body = "{ \"bookId\": " + bookId + ", \"customerName\": \"No Auth User\" }";
        response = RestAssured
                .given()
                .spec(ApiUtils.getRequestSpec())
                .body(body)
                .post("/orders");
    }


    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedCode) {
        System.out.println("==> Response Body: " + response.body().asString());
        Assert.assertEquals(response.statusCode(), expectedCode,
                "Expected status " + expectedCode + " but got " + response.statusCode()
                        + ". Body: " + response.body().asString());
    }

    @Then("the response should contain {string} as {string}")
    public void theResponseShouldContain(String key, String expectedValue) {
        String actualValue = response.jsonPath().getString(key);
        Assert.assertEquals(actualValue, expectedValue,
                "Expected '" + key + "' to be '" + expectedValue + "' but got '" + actualValue + "'");
    }

    @Then("the response should return a list of books")
    public void theResponseShouldReturnAListOfBooks() {
        List<?> books = response.jsonPath().getList("$");
        Assert.assertNotNull(books, "Books list should not be null");
        Assert.assertFalse(books.isEmpty(), "Books list should not be empty");
        System.out.println("==> Total books returned: " + books.size());
    }

    @Then("each book should have {string} and {string} fields")
    public void eachBookShouldHaveFields(String field1, String field2) {
        List<Map<String, Object>> books = response.jsonPath().getList("$");
        for (Map<String, Object> book : books) {
            Assert.assertTrue(book.containsKey(field1),
                    "Book missing field: " + field1);
            Assert.assertTrue(book.containsKey(field2),
                    "Book missing field: " + field2);
        }
    }

    @Then("all returned books should have type {string}")
    public void allReturnedBooksShouldHaveType(String expectedType) {
        List<Map<String, Object>> books = response.jsonPath().getList("$");
        Assert.assertFalse(books.isEmpty(), "No books returned for type: " + expectedType);
        for (Map<String, Object> book : books) {
            Assert.assertEquals(book.get("type").toString(), expectedType,
                    "Book type mismatch: expected " + expectedType + " but got " + book.get("type"));
        }
    }

    @Then("the response should return at most {int} books")
    public void theResponseShouldReturnAtMostBooks(int limit) {
        List<?> books = response.jsonPath().getList("$");
        Assert.assertTrue(books.size() <= limit,
                "Expected at most " + limit + " books but got " + books.size());
    }

    @Then("the response body should contain field {string} with value {string}")
    public void theResponseBodyShouldContainFieldWithValue(String field, String expectedValue) {
        Object actualValue = response.jsonPath().get(field);
        Assert.assertNotNull(actualValue, "Field '" + field + "' not found in response");
        Assert.assertEquals(actualValue.toString(), expectedValue,
                "Field '" + field + "' expected '" + expectedValue + "' but got '" + actualValue + "'");
    }

    @Then("the response body should contain {string} field")
    public void theResponseBodyShouldContainField(String field) {
        Object value = response.jsonPath().get(field);
        Assert.assertNotNull(value, "Field '" + field + "' should be present in response");
    }

    @Then("the response should contain an {string}")
    public void theResponseShouldContainAn(String field) {
        String value = response.jsonPath().getString(field);
        Assert.assertNotNull(value, "Expected '" + field + "' in response but was null");
        Assert.assertFalse(value.isEmpty(), "Expected '" + field + "' to not be empty");
        System.out.println("==> " + field + ": " + value);
    }

    @Then("the response should return a list of orders")
    public void theResponseShouldReturnAListOfOrders() {
        List<?> orders = response.jsonPath().getList("$");
        Assert.assertNotNull(orders, "Orders list should not be null");
        System.out.println("==> Total orders returned: " + orders.size());
    }

    @Then("the response should contain {string}")
    public void theResponseShouldContainKey(String key) {
        Object value = response.jsonPath().get(key);
        Assert.assertNotNull(value, "Response should contain key: " + key);
    }

    @Then("the order should no longer exist")
    public void theOrderShouldNoLongerExist() {
        Response checkResponse = RestAssured
                .given()
                .spec(ApiUtils.getAuthRequestSpec(accessToken))
                .get("/orders/" + orderId);
        Assert.assertEquals(checkResponse.statusCode(), 404,
                "Deleted order should return 404 but got: " + checkResponse.statusCode());
    }
}
