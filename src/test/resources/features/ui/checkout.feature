@UI @Order
Feature: Product Order - SauceDemo

  Background:
    Given the user is on the SauceDemo login page
    And the user logs in with valid credentials

  @Smoke @AddToCart
  Scenario: Add one product to cart and verify cart count
    When the user adds the first product to the cart
    Then the cart count should be "1"

  @Smoke @FullOrder
  Scenario: Complete a full product order successfully
    When the user adds the first product to the cart
    And the user navigates to the cart
    Then the cart page should be displayed
    And the cart should contain 1 item
    When the user clicks checkout
    Then the checkout information page should be displayed
    When the user fills in checkout information with "Manoj" "Kumar" "600001"
    And the user clicks continue
    Then the checkout overview page should be displayed
    When the user clicks finish
    Then the order confirmation page should be displayed
    And the confirmation message should be "Thank you for your order!"

  @Regression @CartNavigation
  Scenario: Navigate to cart and continue shopping
    When the user adds the first product to the cart
    And the user navigates to the cart
    Then the cart page should be displayed
    When the user clicks continue shopping
    Then the user should be on the inventory page

  @Regression @CheckoutValidation
  Scenario: Checkout with missing first name
    When the user adds the first product to the cart
    And the user navigates to the cart
    And the user clicks checkout
    When the user fills in checkout information with "" "Kumar" "600001"
    And the user clicks continue
    Then a checkout error should be displayed containing "First Name is required"

  @Regression @CheckoutValidation
  Scenario: Checkout with missing last name
    When the user adds the first product to the cart
    And the user navigates to the cart
    And the user clicks checkout
    When the user fills in checkout information with "Manoj" "" "600001"
    And the user clicks continue
    Then a checkout error should be displayed containing "Last Name is required"

  @Regression @CheckoutValidation
  Scenario: Checkout with missing postal code
    When the user adds the first product to the cart
    And the user navigates to the cart
    And the user clicks checkout
    When the user fills in checkout information with "Manoj" "Kumar" ""
    And the user clicks continue
    Then a checkout error should be displayed containing "Postal Code is required"

  @Regression @CancelOrder
  Scenario: Cancel order from checkout overview
    When the user adds the first product to the cart
    And the user navigates to the cart
    And the user clicks checkout
    When the user fills in checkout information with "Manoj" "Kumar" "600001"
    And the user clicks continue
    Then the checkout overview page should be displayed
    When the user cancels the order
    Then the user should be on the inventory page
