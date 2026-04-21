package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BasePage {

    // Step One
    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // Step Two (Overview)
    @FindBy(css = ".summary_info")
    private WebElement summaryInfo;

    @FindBy(css = ".summary_total_label")
    private WebElement totalLabel;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    // Confirmation
    @FindBy(css = ".complete-header")
    private WebElement confirmationHeader;

    @FindBy(css = ".complete-text")
    private WebElement confirmationText;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // Step One methods
    public boolean isCheckoutStepOneDisplayed() {
        try {
            waitHelper.waitForVisible(pageTitle);
            return pageTitle.getText().equals("Checkout: Your Information");
        } catch (Exception e) {
            return false;
        }
    }

    public void enterFirstName(String firstName) {
        waitHelper.waitForVisible(firstNameField);
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
    }

    public void enterPostalCode(String postalCode) {
        postalCodeField.clear();
        postalCodeField.sendKeys(postalCode);
    }

    public void fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }

    public void clickContinue() {
        waitHelper.clickWhenReady(continueButton);
    }

    public String getCheckoutError() {
        waitHelper.waitForVisible(errorMessage);
        return errorMessage.getText();
    }

    // Step Two methods
    public boolean isCheckoutOverviewDisplayed() {
        try {
            waitHelper.waitForVisible(pageTitle);
            return pageTitle.getText().equals("Checkout: Overview");
        } catch (Exception e) {
            return false;
        }
    }

    public String getTotalAmount() {
        waitHelper.waitForVisible(totalLabel);
        return totalLabel.getText();
    }

    public void clickFinish() {
        waitHelper.clickWhenReady(finishButton);
    }

    public void clickCancel() {
        waitHelper.clickWhenReady(cancelButton);
    }

    // Confirmation methods
    public boolean isOrderConfirmed() {
        try {
            waitHelper.waitForVisible(confirmationHeader);
            return confirmationHeader.getText().equals("Thank you for your order!");
        } catch (Exception e) {
            return false;
        }
    }

    public String getConfirmationHeader() {
        waitHelper.waitForVisible(confirmationHeader);
        return confirmationHeader.getText();
    }

    public String getConfirmationText() {
        return confirmationText.getText();
    }

    public void backToProducts() {
        waitHelper.clickWhenReady(backToProductsButton);
    }
}
