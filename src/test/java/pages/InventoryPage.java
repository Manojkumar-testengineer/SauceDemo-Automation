package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class InventoryPage extends BasePage {

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement burgerMenu;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(css = ".product_sort_container")
    private WebElement sortDropdown;

    // Add first product to cart
    @FindBy(css = "[data-test='add-to-cart-sauce-labs-backpack']")
    private WebElement addBackpackToCart;

    @FindBy(css = "[data-test='add-to-cart-sauce-labs-bike-light']")
    private WebElement addBikeLightToCart;

    @FindBy(css = "[data-test='add-to-cart-sauce-labs-bolt-t-shirt']")
    private WebElement addBoltTshirtToCart;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isInventoryPageDisplayed() {
        try {
            waitHelper.waitForVisible(pageTitle);
            return pageTitle.getText().equals("Products");
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageHeading() {
        waitHelper.waitForVisible(pageTitle);
        return pageTitle.getText();
    }

    public int getInventoryItemCount() {
        return inventoryItems.size();
    }

    public void addFirstProductToCart() {
        waitHelper.clickWhenReady(addBackpackToCart);
    }

    public void addBikeLightToCart() {
        waitHelper.clickWhenReady(addBikeLightToCart);
    }

    public void addBoltTshirtToCart() {
        waitHelper.clickWhenReady(addBoltTshirtToCart);
    }

    public String getCartCount() {
        try {
            return cartBadge.getText();
        } catch (Exception e) {
            return "0";
        }
    }

    public void goToCart() {
        waitHelper.clickWhenReady(cartIcon);
    }

    public void logout() {
        waitHelper.clickWhenReady(burgerMenu);
        waitHelper.clickWhenReady(logoutLink);
    }
}
