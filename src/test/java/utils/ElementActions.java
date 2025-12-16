package utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ElementActions {

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public ElementActions(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ---------------------------------------------------------
    // WAIT HELPERS
    // ---------------------------------------------------------

    public WebElement waitForVisible(String xpath) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath(xpath)));
    }

    public WebElement waitForClickable(String xpath) {
        return wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.xpath(xpath)));
    }

    public boolean waitForInvisible(String xpath) {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(AppiumBy.xpath(xpath)));
        } catch (Exception e) {
            return false;
        }
    }

    // ---------------------------------------------------------
    // CLICK & TYPE
    // ---------------------------------------------------------

    public WebElement click(String xpath) {
        WebElement el = waitForClickable(xpath);
        el.click();
        return el;
    }

    public WebElement type(String xpath, String value) {
        if (value == null || value.isEmpty()) return null;
        WebElement el = waitForVisible(xpath);
        el.clear();
        el.sendKeys(value);
        return el;
    }

    // ---------------------------------------------------------
    // VISIBILITY HELPERS
    // ---------------------------------------------------------

    public boolean isVisible(String xpath) {
        try {
            waitForVisible(xpath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPresent(String xpath) {
        try {
            driver.findElement(AppiumBy.xpath(xpath));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ---------------------------------------------------------
    // SCROLLING
    // ---------------------------------------------------------

    public void scrollToText(String text) {
        String uiScroll =
                "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".scrollIntoView(new UiSelector().textContains(\"" + text + "\"));";

        try {
            ((AndroidDriver) driver).findElement(AppiumBy.androidUIAutomator(uiScroll));
        } catch (Exception ignored) {}
    }

    public void scrollToEnd() {
        try {
            ((AndroidDriver) driver).findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollToEnd(10)"
            ));
        } catch (Exception ignored) {}
    }

    // ---------------------------------------------------------
    // DROPDOWN SELECTION
    // ---------------------------------------------------------

    public void selectDropdown(String triggerXpath, String value) {

        click(triggerXpath);

        // Try visible elements first
        String optionXpath = "//*[contains(@text,'" + value + "')]";
        List<WebElement> options = driver.findElements(AppiumBy.xpath(optionXpath));

        for (WebElement opt : options) {
            if (opt.isDisplayed()) {
                opt.click();
                return;
            }
        }

        // Fallback → scroll
        scrollToText(value);

        driver.findElement(AppiumBy.xpath(optionXpath)).click();
    }

    // ---------------------------------------------------------
    // OTHER ACTIONS
    // ---------------------------------------------------------

    public void hideKeyboard() {
        try {
            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).hideKeyboard();
            } else {
                // iOS (optional): ((IOSDriver) driver).hideKeyboard();
            }
        } catch (Exception ignored) {
            // keyboard is already closed, ignore
        }
    }
    public void safeClick(String xpath) {
        try { click(xpath); } catch (Exception ignored) {}
    }

    public String getText(String xpath) {
        try {
            WebElement el = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath(xpath))
            );
            return el.getText();
        } catch (Exception e) {
            return "";
        }
    }
}
