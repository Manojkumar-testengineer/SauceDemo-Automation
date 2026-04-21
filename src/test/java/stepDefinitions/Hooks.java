package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utils.ConfigReader;
import utils.DriverManager;

public class Hooks {

    @Before(value = "@UI", order = 1)
    public void setUp(Scenario scenario) {
        String browser = System.getProperty("browser", ConfigReader.getBrowser());
        System.out.println("[HOOK] Starting Scenario : " + scenario.getName());
        System.out.println("[HOOK] Browser           : " + browser);
        System.out.println("[HOOK] Thread            : " + Thread.currentThread().getId());
        DriverManager.initDriver();
        WebDriver driver = DriverManager.getDriver();
        driver.get(ConfigReader.getBaseUrl());
    }

    @After(value = "@UI", order = 1)
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            if (scenario.isFailed()) {
                // Take screenshot on failure
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot on Failure - " + scenario.getName());
            }
            DriverManager.quitDriver();
        }
    }
}
