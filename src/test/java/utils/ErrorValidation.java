package utils;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class ErrorValidation {

    private final AppiumDriver driver;

    private ExtentTest log() {
        return BaseTest.getTest().get();
    }

    public ErrorValidation(AppiumDriver driver) {
        this.driver = driver;
    }

    // ---------------------------------------------
    // GENERIC PASS
    // ---------------------------------------------
    private void record(String message, boolean success) {
        if (log() != null) {
            if (success) log().pass(message);
            else log().fail(message);
        }
        System.out.println(message);
    }

    // ---------------------------------------------
    // 1️⃣ verifyErrorMessage
    // ---------------------------------------------
    public void verifyErrorMessage(By locator, String expectedText) {
        try {
            WebElement element = driver.findElement(locator);
            String actualText = element.getText().trim();

            Assert.assertEquals(actualText, expectedText);

            String msg = "[SUCCESS] - Expected: '" + expectedText + "' vs Actual: '" + actualText + "'";
            record(msg, true);
        }
        catch (Exception e) {
            String msg = "[FAILURE] - Expected: '" + expectedText + "' but mismatch or element not found.";
            record(msg, false);
            throw new AssertionError(msg, e);
        }
    }

    // ---------------------------------------------
    // 2️⃣ verifyBannerMessage — popup validation
    // ---------------------------------------------
    public void verifyBannerMessage(String expectedText) {
        By popupLocator = By.xpath("//android.widget.TextView");

        try {
            WebElement popup = driver.findElement(popupLocator);
            String actualText = popup.getText().trim();

            Assert.assertEquals(actualText, expectedText);

            String msg = "[SUCCESS] - Banner Match: '" + expectedText + "' vs Actual: '" + actualText + "'";
            record(msg, true);
        }
        catch (Exception e) {
            String msg = "[FAILURE] - Banner text does not match expected: '" + expectedText + "'";
            record(msg, false);
            throw new AssertionError(msg, e);
        }
    }

    // ---------------------------------------------
    // 3️⃣ Generic text validation anywhere in the UI
    // ---------------------------------------------
    public void verifyTextPresent(String expectedText) {
        try {
            String xpath = "//*[contains(@text,'" + expectedText + "')]";
            WebElement el = driver.findElement(By.xpath(xpath));
            String actual = el.getText().trim();

            Assert.assertTrue(actual.contains(expectedText));

            String msg = "[SUCCESS] - Text found: '" + expectedText + "'";
            record(msg, true);
        }
        catch (Exception e) {
            String msg = "[FAILURE] - Could not find text: '" + expectedText + "'";
            record(msg, false);
            throw new AssertionError(msg, e);
        }
    }
}
