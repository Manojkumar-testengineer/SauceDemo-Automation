@API @SimpleBooksAPI
Feature: Simple Books API - REST Assured Tests

  @API @Smoke @Status
  Scenario: Verify API status is healthy
    When I send a GET request to "/status"
    Then the response status code should be 200
    And the response should contain "status" as "OK"

  @API @Smoke @GetBooks
  Scenario: Get list of all books
    When I send a GET request to "/books"
    Then the response status code should be 200
    And the response should return a list of books
    And each book should have "id" and "name" fields

  @API @Regression @GetBooks
  Scenario: Get list of fiction books only
    When I send a GET request to "/books" with query param "type" as "fiction"
    Then the response status code should be 200
    And all returned books should have type "fiction"

  @API @Regression @GetBooks
  Scenario: Get list of non-fiction books only
    When I send a GET request to "/books" with query param "type" as "non-fiction"
    Then the response status code should be 200
    And all returned books should have type "non-fiction"

  @API @Regression @GetBooks
  Scenario: Get list with limit parameter
    When I send a GET request to "/books" with query param "limit" as "3"
    Then the response status code should be 200
    And the response should return at most 3 books

  @API @Regression @SingleBook
  Scenario: Get a single book by ID
    When I send a GET request to "/books/1"
    Then the response status code should be 200
    And the response body should contain field "id" with value "1"
    And the response body should contain "name" field

  @API @Regression @GetBook
  Scenario: Get a book with invalid ID returns 404
    When I send a GET request to "/books/9999"
    Then the response status code should be 404

  @API @Smoke @Auth
  Scenario: Register API client and get access token
    When I register a new API client
    Then the response status code should be 201
    And the response should contain an "accessToken"

  @API @Regression @SubmitOrder
  Scenario: Submit a new book order
    Given I have a valid API access token
    When I submit an order for book ID 1 with customer name "Manoj Kumar"
    Then the response status code should be 201
    And the response should contain an "orderId"

  @API @Regression @AllOrders
  Scenario: Get all orders
    Given I have a valid API access token
    And I have an existing order
    When I send a GET request to "/orders" with auth token
    Then the response status code should be 200
    And the response should return a list of orders

  @API @Regression @SingleOrder
  Scenario: Get a single order by ID
    Given I have a valid API access token
    And I have an existing order
    When I get the order by its ID with auth token
    Then the response status code should be 200
    And the response should contain "bookId"

  @API @Regression @UpdateOrder
  Scenario: Update an existing order with PATCH
    Given I have a valid API access token
    And I have an existing order
    When I update the order customer name to "Updated Manoj" using PATCH with auth token
    Then the response status code should be 204

  @API @Regression @DeleteOrder
  Scenario: Delete an existing order
    Given I have a valid API access token
    And I have an existing order
    When I delete the order using DELETE with auth token
    Then the response status code should be 204
    And the order should no longer exist

  @API @Regression @Negative
  Scenario: Submit order without authentication returns 401
    When I submit an order without auth token for book ID 1
    Then the response status code should be 401

  @API @Regression @Negative
  Scenario: Submit order for unavailable book returns error
    Given I have a valid API access token
    When I submit an order for book ID 101 with customer name "Test User"
    Then the response status code should be 400
