package stepDefinitions;

import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import utils.DriverManager;

public class CheckoutSteps {

    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    private InventoryPage getInventoryPage() {
        if (inventoryPage == null) inventoryPage = new InventoryPage(DriverManager.getDriver());
        return inventoryPage;
    }

    private CartPage getCartPage() {
        if (cartPage == null) cartPage = new CartPage(DriverManager.getDriver());
        return cartPage;
    }

    private CheckoutPage getCheckoutPage() {
        if (checkoutPage == null) checkoutPage = new CheckoutPage(DriverManager.getDriver());
        return checkoutPage;
    }

    @When("the user adds the first product to the cart")
    public void theUserAddsTheFirstProductToTheCart() {
        getInventoryPage().addFirstProductToCart();
    }

    @Then("the cart count should be {string}")
    public void theCartCountShouldBe(String expectedCount) {
        Assert.assertEquals(getInventoryPage().getCartCount(), expectedCount,
                "Cart count mismatch");
    }

    @When("the user navigates to the cart")
    public void theUserNavigatesToTheCart() {
        getInventoryPage().goToCart();
    }

    @Then("the cart page should be displayed")
    public void theCartPageShouldBeDisplayed() {
        Assert.assertTrue(getCartPage().isCartPageDisplayed(),
                "Cart page is not displayed");
    }

    @Then("the cart should contain {int} item")
    public void theCartShouldContainItem(int expectedCount) {
        Assert.assertEquals(getCartPage().getCartItemCount(), expectedCount,
                "Cart item count mismatch");
    }

    @When("the user clicks checkout")
    public void theUserClicksCheckout() {
        getCartPage().clickCheckout();
    }

    @Then("the checkout information page should be displayed")
    public void theCheckoutInformationPageShouldBeDisplayed() {
        Assert.assertTrue(getCheckoutPage().isCheckoutStepOneDisplayed(),
                "Checkout step one page is not displayed");
    }

    @When("the user fills in checkout information with {string} {string} {string}")
    public void theUserFillsInCheckoutInformationWith(String firstName, String lastName, String postalCode) {
        getCheckoutPage().fillCheckoutInfo(firstName, lastName, postalCode);
    }

    @When("the user clicks continue")
    public void theUserClicksContinue() {
        getCheckoutPage().clickContinue();
    }

    @Then("the checkout overview page should be displayed")
    public void theCheckoutOverviewPageShouldBeDisplayed() {
        Assert.assertTrue(getCheckoutPage().isCheckoutOverviewDisplayed(),
                "Checkout overview page is not displayed");
    }

    @When("the user clicks finish")
    public void theUserClicksFinish() {
        getCheckoutPage().clickFinish();
    }

    @Then("the order confirmation page should be displayed")
    public void theOrderConfirmationPageShouldBeDisplayed() {
        Assert.assertTrue(getCheckoutPage().isOrderConfirmed(),
                "Order confirmation page is not displayed");
    }

    @Then("the confirmation message should be {string}")
    public void theConfirmationMessageShouldBe(String expectedMessage) {
        Assert.assertEquals(getCheckoutPage().getConfirmationHeader(), expectedMessage,
                "Confirmation message mismatch");
    }

    @When("the user clicks continue shopping")
    public void theUserClicksContinueShopping() {
        getCartPage().continueShopping();
    }

    @Then("the user should be on the inventory page")
    public void theUserShouldBeOnTheInventoryPage() {
        Assert.assertTrue(getInventoryPage().isInventoryPageDisplayed(),
                "User is not on the inventory page");
    }

    @Then("a checkout error should be displayed containing {string}")
    public void aCheckoutErrorShouldBeDisplayedContaining(String expectedError) {
        String actualError = getCheckoutPage().getCheckoutError();
        Assert.assertTrue(actualError.contains(expectedError),
                "Expected checkout error: '" + expectedError + "' but got: '" + actualError + "'");
    }

    @When("the user cancels the order")
    public void theUserCancelsTheOrder() {
        getCheckoutPage().clickCancel();
    }
}
