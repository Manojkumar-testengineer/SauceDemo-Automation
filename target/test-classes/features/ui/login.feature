@UI @Login
Feature: Login Functionality - SauceDemo

  Background:
    Given the user is on the SauceDemo login page

  @Smoke @ValidLogin
  Scenario: Successful login with valid credentials
    When the user enters username "standard_user" and password "secret_sauce"
    And the user clicks the login button
    Then the user should be redirected to the inventory page
    And the page heading should be "Products"

  @Regression @InvalidLogin
  Scenario: Login with invalid username
    When the user enters username "invalid_user" and password "secret_sauce"
    And the user clicks the login button
    Then an error message should be displayed
    And the error message should contain "Username and password do not match"

  @Regression @InvalidLogin
  Scenario: Login with invalid password
    When the user enters username "standard_user" and password "wrong_password"
    And the user clicks the login button
    Then an error message should be displayed
    And the error message should contain "Username and password do not match"

  @Regression @LockedUser
  Scenario: Login with locked out user
    When the user enters username "locked_out_user" and password "secret_sauce"
    And the user clicks the login button
    Then an error message should be displayed
    And the error message should contain "Sorry, this user has been locked out"

  @Regression @EmptyFields
  Scenario: Login with empty username and password
    When the user clicks the login button
    Then an error message should be displayed
    And the error message should contain "Username is required"

  @Regression @EmptyFields
  Scenario: Login with empty password only
    When the user enters username "standard_user" and password ""
    And the user clicks the login button
    Then an error message should be displayed
    And the error message should contain "Password is required"

  @Regression @EmptyFields
  Scenario: Login with empty username only
    When the user enters username "" and password "secret_sauce"
    And the user clicks the login button
    Then an error message should be displayed
    And the error message should contain "Username is required"

  @Regression
  Scenario: Login with performance glitch user
    When the user enters username "performance_glitch_user" and password "secret_sauce"
    And the user clicks the login button
    Then the user should be redirected to the inventory page

  @Regression
  Scenario: Logout after successful login
    When the user enters username "standard_user" and password "secret_sauce"
    And the user clicks the login button
    Then the user should be redirected to the inventory page
    When the user logs out
    Then the user should be redirected to the login page

  @Regression @DataDriven
  Scenario Outline: Login with multiple user types
    When the user enters username "<username>" and password "<password>"
    And the user clicks the login button
    Then the "<expectedResult>" should occur

    Examples:
      | username              | password     | expectedResult  |
      | standard_user         | secret_sauce | login_success   |
      | locked_out_user       | secret_sauce | login_error     |
      | problem_user          | secret_sauce | login_success   |
      | performance_glitch_user | secret_sauce | login_success  |
      | error_user            | secret_sauce | login_success   |
      | visual_user           | secret_sauce | login_success   |
