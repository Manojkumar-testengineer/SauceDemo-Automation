package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CartPage extends BasePage {

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".cart_item .inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isCartPageDisplayed() {
        try {
            waitHelper.waitForVisible(pageTitle);
            return pageTitle.getText().equals("Your Cart");
        } catch (Exception e) {
            return false;
        }
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public String getFirstCartItemName() {
        if (!cartItemNames.isEmpty()) {
            return cartItemNames.get(0).getText();
        }
        return "";
    }

    public void clickCheckout() {
        waitHelper.clickWhenReady(checkoutButton);
    }

    public void continueShopping() {
        waitHelper.clickWhenReady(continueShoppingButton);
    }
}
