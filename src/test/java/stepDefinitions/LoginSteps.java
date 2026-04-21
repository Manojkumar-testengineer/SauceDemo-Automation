package stepDefinitions;

import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.DriverManager;

public class LoginSteps {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;

    private LoginPage getLoginPage() {
        if (loginPage == null) loginPage = new LoginPage(DriverManager.getDriver());
        return loginPage;
    }

    private InventoryPage getInventoryPage() {
        if (inventoryPage == null) inventoryPage = new InventoryPage(DriverManager.getDriver());
        return inventoryPage;
    }

    @Given("the user is on the SauceDemo login page")
    public void theUserIsOnTheSauceDemoLoginPage() {
        Assert.assertTrue(getLoginPage().isLoginPageDisplayed(),
                "Login page is not displayed");
    }

    @When("the user enters username {string} and password {string}")
    public void theUserEntersUsernameAndPassword(String username, String password) {
        if (!username.isEmpty()) {
            getLoginPage().enterUsername(username);
        }
        if (!password.isEmpty()) {
            getLoginPage().enterPassword(password);
        }
    }

    @When("the user clicks the login button")
    public void theUserClicksTheLoginButton() {
        getLoginPage().clickLogin();
    }

    @Then("the user should be redirected to the inventory page")
    public void theUserShouldBeRedirectedToTheInventoryPage() {
        Assert.assertTrue(getInventoryPage().isInventoryPageDisplayed(),
                "User was NOT redirected to the inventory page");
    }

    @Then("the page heading should be {string}")
    public void thePageHeadingShouldBe(String expectedHeading) {
        Assert.assertEquals(getInventoryPage().getPageHeading(), expectedHeading,
                "Page heading mismatch");
    }

    @Then("an error message should be displayed")
    public void anErrorMessageShouldBeDisplayed() {
        Assert.assertTrue(getLoginPage().isErrorDisplayed(),
                "Error message was NOT displayed");
    }

    @Then("the error message should contain {string}")
    public void theErrorMessageShouldContain(String expectedError) {
        String actualError = getLoginPage().getErrorMessage();
        Assert.assertTrue(actualError.contains(expectedError),
                "Expected error: '" + expectedError + "' but got: '" + actualError + "'");
    }

    @Given("the user logs in with valid credentials")
    public void theUserLogsInWithValidCredentials() {
        getLoginPage().login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());
        Assert.assertTrue(getInventoryPage().isInventoryPageDisplayed(),
                "Login failed with valid credentials");
    }

    @When("the user logs out")
    public void theUserLogsOut() {
        getInventoryPage().logout();
    }

    @Then("the user should be redirected to the login page")
    public void theUserShouldBeRedirectedToTheLoginPage() {
        Assert.assertTrue(getLoginPage().isLoginPageDisplayed(),
                "User was NOT redirected back to login page after logout");
    }

    @Then("the {string} should occur")
    public void theExpectedResultShouldOccur(String expectedResult) {
        if (expectedResult.equals("login_success")) {
            Assert.assertTrue(getInventoryPage().isInventoryPageDisplayed(),
                    "Expected login success but inventory page not displayed");
        } else if (expectedResult.equals("login_error")) {
            Assert.assertTrue(getLoginPage().isErrorDisplayed(),
                    "Expected login error but no error message displayed");
        }
    }
}
