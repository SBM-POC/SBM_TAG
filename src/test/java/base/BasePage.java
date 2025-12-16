package base;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ElementActions;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import utils.*;

import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.By;


public abstract class BasePage {
    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected ElementActions actions;
    protected TransferHelper transferHelper;

    public BasePage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.actions = new ElementActions(driver);  //Universal action engine
        this.transferHelper = new TransferHelper(driver);
    }

    // ---------------------------------------------------------
    // SHORTCUTS TO ELEMENTACTIONS
    // ---------------------------------------------------------

    protected void waitAndClick(String xpath) {
        actions.click(xpath);
    }

    protected void waitAndType(String xpath, String value) {
        actions.type(xpath, value);
    }

    protected boolean isVisible(String xpath) {
        return actions.isVisible(xpath);
    }

    protected boolean isElementPresent(String xpath) {
        return actions.isPresent(xpath);
    }

    protected void waitUntilInvisibility(String xpath) {
        actions.waitForInvisible(xpath);
    }

    protected String getText(String xpath) {
        return actions.getText(xpath);
    }

    protected void selectDropdown(String trigger, String value) {
        actions.selectDropdown(trigger, value);
    }

    protected void hideKeyboard() {
        actions.hideKeyboard();
    }

    // ---------------------------------------------------------
    // Platform Helpers
    // ---------------------------------------------------------

    protected boolean isAndroid() {
        return BaseTest.getPlatform().equalsIgnoreCase("Android");
    }

    protected boolean isIOS() {
        return BaseTest.getPlatform().equalsIgnoreCase("iOS");
    }

    // ---------------------------------------------------------
    // GENERIC SCROLLING & CLICKING
    // ---------------------------------------------------------

    protected void clickWithScroll(String xpath) {
        try {
            waitAndClick(xpath);
        } catch (Exception ignored) {
            scrollToTextUsingUiAutomator(getLastTextFromXpath(xpath));
            waitAndClick(xpath);
        }
    }

    protected void scrollToText(String text) {
        scrollToTextUsingUiAutomator(text);
    }

    protected void scrollToEnd() {
        try {
            ((io.appium.java_client.android.AndroidDriver) driver).findElement(
                    AppiumBy.androidUIAutomator(
                            "new UiScrollable(new UiSelector().scrollable(true)).scrollToEnd(10)"
                    )
            );

        } catch (Exception ignored) {}
    }

    private void scrollToTextUsingUiAutomator(String text) {

        try {
            ((io.appium.java_client.android.AndroidDriver) driver).findElement(
                    AppiumBy.androidUIAutomator(
                            "new UiScrollable(new UiSelector().scrollable(true))" +
                                    ".scrollIntoView(new UiSelector().textContains(\"" + text + "\"))"
                    )
            );
        } catch (Exception ignored) {}
    }

    private String getLastTextFromXpath(String xpath) {

        try {
            int start = xpath.indexOf("text','") + 7;
            int end = xpath.indexOf("')", start);
            return xpath.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }

    // ---------------------------------------------------------
    // SAFE BACK NAVIGATION ENGINE
    // ---------------------------------------------------------

    public void navigateBackToSafeScreen(String safeIconXpath, int maxPresses) {
        for (int i = 0; i < maxPresses; i++) {
            if (isOnSafeScreen(safeIconXpath)) {
                System.out.println("✅ Already on a safe screen.");
                return;
            }

            System.out.println("↩ Press BACK (" + (i + 1) + "/" + maxPresses + ")");
            driver.navigate().back();
            sleep(600);

            if (isOnSafeScreen(safeIconXpath)) {
                System.out.println("✅ Safe screen reached.");
                return;
            }
        }
        System.out.println("⚠ Max BACK presses reached — still not safe.");
    }

    public boolean isOnSafeScreen(String safeIconXpath) {
        try {
            return isElementPresent(safeIconXpath) && isVisible(safeIconXpath);
        } catch (Exception ignored) {
            return false;
        }
    }

    protected void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    public void waitForVisibility(String locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
    }

    //Select Dropdown value
    public void selectDropdownValue(String value) {
        boolean valueFound = false;

        int index = 0;
        int maxScrolls = 7;
        while (!valueFound && index < maxScrolls) {
            try {
                WebElement ScrollValue = driver.findElement(AppiumBy.xpath("//*[contains(@text,'" + value + "')]"));

                String currentValue = ScrollValue.getText().trim();
                System.out.println("Checking value at index " + index + ": " + currentValue);

                if (currentValue.contains(value)) {
                    if (wait.until(ExpectedConditions.visibilityOf(ScrollValue)).isDisplayed()) {
                        wait.until(ExpectedConditions.elementToBeClickable(ScrollValue)).click();
                        System.out.println("Clicked on dropdown value");
                        valueFound = true;
                        break;
                    }
                }

            } catch (Exception e) {
                // Scroll
                try {
                    String scroll =
                            "new UiScrollable(new UiSelector().scrollable(true))" +
                                    ".scrollIntoView(new UiSelector().text(\"" + value + "\"));";
                    driver.findElement(AppiumBy.androidUIAutomator(scroll));
                    Thread.sleep(100);
                    System.out.println("✅ Scrolled to value");
                    index++;
                } catch (Exception err) {
                    System.out.println("⚠️ Failed to scroll to value");

                }
            }
        }
        if (!valueFound) {
            System.out.println("❌ Value not found within max scrolls: " + index);
        }
    }

}

