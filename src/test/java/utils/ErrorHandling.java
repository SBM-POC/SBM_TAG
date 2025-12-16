package utils;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.LogOutPage;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ErrorHandling {

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    private ExtentTest log() {
        return BaseTest.getTest().get();
    }

    public ErrorHandling(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ---------------------------------------------------------
    // MAIN ERROR HANDLING - POSITIVE FLOW
    // ---------------------------------------------------------

    public boolean handleErrorMessages() {
        TestUtils.sleep(1000);
        boolean hasErrors = false;

        // 1️⃣ Inline / textfield errors
        List<WebElement> errorMessages = driver.findElements(AppiumBy.xpath(
                "//android.widget.TextView[contains(@resource-id,'error') or contains(@text,'required')" +
                        "or contains(translate(@text, 'ENTER', 'enter'), 'enter') or contains(@text, 'Please') " +
                        "or contains(translate(@text, 'SELECT', 'select'), 'select') or contains(@text, 'cannot')]"
        ));

        for (WebElement error : errorMessages) {
            hasErrors = true;
            String errorText = error.getText().trim();
            System.out.println("Inline Error: " + errorText);
            log().fail("Inline Error: " + errorText);
        }

        // 2️⃣ Popup errors
        List<WebElement> popupContainers = driver.findElements(By.xpath(
                "//android.view.View[contains(@resource-id, 'alert-popup-container')]"
        ));

        if (!popupContainers.isEmpty()) {
            hasErrors = true;
            handlePopupErrors();
        }
        return hasErrors;
    }

    private void handlePopupErrors() {
        try {
            WebElement popupTextElement = driver.findElement(By.xpath(
                    "//android.view.View[contains(@resource-id, 'alert-popup-container')]/descendant::android.widget.TextView"
            ));
            String popupText = popupTextElement.getText().trim();
            System.out.println("Popup Error: " + popupText);
            log().fail("Popup Error: " + popupText);
            WebElement closeBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath(
                            "//android.widget.Button[contains(@text, 'Close')]"))
            );
            closeBtn.click();
        } catch (Exception e) {
            System.out.println("Popup detected but failed to read/close: " + e.getMessage());
            log().fail("Popup detected but failed to read/close: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // NAVIGATE USING BACK BUTTON TO SAFE SCREEN
    // ---------------------------------------------------------
    public void navigateBackToSafeScreen(int maxBackPresses) {
        for (int i = 0; i < maxBackPresses; i++) {

            // 👉 1. Check BEFORE pressing back
            if (isOnSafeScreen()) {
                System.out.println("✅ Already on a safe screen, stopping back navigation.");
                return;
            }

            System.out.println("↩ Pressing BACK (" + (i + 1) + "/" + maxBackPresses + ")");
            driver.navigate().back();

            try {
                Thread.sleep(1000); // let UI settle
            } catch (InterruptedException ignored) {}

            // 👉 2. Check AFTER pressing back
            if (isOnSafeScreen()) {
                System.out.println("✅ Reached a safe screen after BACK.");
                return;
            }
        }

        System.out.println("⚠️ Max BACK presses reached, still not on a known safe screen.");
    }

    public boolean isOnSafeScreen() {
        try {
            WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // 1. Wait until the elements are present in the DOM
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(LogOutPage.PROFILE_ICON)));

            // 2. Now that the wait is over, safely retrieve the list of elements
            List<WebElement> profileIcons = driver.findElements(By.xpath(LogOutPage.PROFILE_ICON));

            return !profileIcons.isEmpty() && profileIcons.get(0).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    /// --------------------
    /// ERROR HANDLING - NEGATIVE FLOWS
    /// --------------------

    /**
     * Same detection logic as handleErrorMessages(), but
     * - treats errors as EXPECTED (for negative test cases)
     * - logs PASS when errors are found
     * - logs FAIL if NO error is found
     */
    public int handleExpectedErrors(Map<String, String> data, String feature) {
        TestUtils.sleep(1000);
        int hasFieldErrors = 0;
        int hasPopupErrors = 0;

        // 1️⃣ Inline / textfield errors (red text under fields)
        List<WebElement> errorMessages = driver.findElements(AppiumBy.xpath(
                "//android.widget.TextView[contains(@resource-id,'error') or contains(@text,'required')" +
                        "or contains(translate(@text, 'ENTER', 'enter'), 'enter') or contains(@text, 'Please') " +
                        "or contains(translate(@text, 'SELECT', 'select'), 'select') or contains(@text, 'cannot')]"
        ));

        if (!errorMessages.isEmpty()) {
            for (WebElement error : errorMessages) {
                hasFieldErrors = 1;
                String errorText = error.getText().trim();
                assertNegativeFlow(data, error, feature); // Assert and log relevant messages
                return hasFieldErrors;
            }
        }


        // 2️⃣ Popup errors (e.g. transfer limit exceeded)
        List<WebElement> popupContainers = driver.findElements(By.xpath(
                "//android.view.View[contains(@resource-id, 'alert-popup-container')]"
        ));

        if (!popupContainers.isEmpty()) {
            hasPopupErrors = 2;
            try {
                WebElement popupTextElement = driver.findElement(By.xpath(
                        "//android.view.View[contains(@resource-id, 'alert-popup-container')]/descendant::android.widget.TextView"
                ));
                String popupText = popupTextElement.getText().trim();
                assertNegativeFlow(data, popupTextElement, feature); // Assert and log relevant messages

                WebElement closeBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(By.xpath(
                                "//android.widget.Button[contains(@text, 'Close')]"))
                );
                closeBtn.click();
            } catch (Exception e) {
                System.out.println("Popup detected but failed to read/close (negative flow): " + e.getMessage());
                if (log() != null) {
                    log().warning("Popup detected but failed to read/close (negative flow): " + e.getMessage());
                }
            }
            return hasPopupErrors;
        }

        // Final assertion logging
        if (hasFieldErrors != 1 || hasPopupErrors != 2) {
            log().fail("❌ [NEGATIVE] Error message expected, but none found!");
        }
        return 0;
    }

    // Assert method
    public void assertNegativeFlow(Map<String, String> data, WebElement error, String feature) {
        switch (feature) {
            case "Recharge My Prepaid Card":
                String errorAmount = data.get("ErrMsg_Amount");
                String errorRemarks = data.get("ErrMsgRemarks");
                String errorPayTransferTo = data.get("ErrPay/TransferTo");

                if (!errorAmount.isEmpty()) {
                    assertMatch("ErrMsg_Amount", error.getText(), errorAmount);
                } else if (!errorRemarks.isEmpty()) {
                    assertMatch("ErrMsgRemarks", error.getText(), errorRemarks);
                } else if (!errorPayTransferTo.isEmpty()) {
                    assertMatch("ErrPay/TransferTo", error.getText(), errorPayTransferTo);
                }
                break;
        }
    }

    // expectErrors method - METHOD TO BE CALLED
    public boolean expectErrorsAfterContinue(Map<String, String> data, String feature) {
        int error = handleExpectedErrors(data, feature);
        if (error == 0) { // Check if no empty fields have been found
            log().fail("❌ No validation errors");
            navigateBackToSafeScreen(5);
            return false;
        }
        return true;
    }

    public boolean expectErrorsAfterConfirm(Map<String, String> data, String feature) {
        int error = handleExpectedErrors(data, feature);
        if (error == 0) { // Check if no popups have been found
            log().fail("❌ No validation errors");
            navigateBackToSafeScreen(5);
            return false;
        }
        return true;
    }

    // ---------------------------------------------------------
    //  INTERNAL MATCHER
    // ---------------------------------------------------------
    private void assertMatch(String field, String actual, String expected) {
        ExtentTest test = BaseTest.getTest().get();

        if (expected == null || expected.isEmpty())
            return;
        try {
            Assert.assertTrue(actual.contains(expected));
            System.out.println("✔ " + field + " validated: " + actual);
            test.pass("✔ " + field + " validated: " + actual);
        } catch (AssertionError ae) {
            System.out.println("❌ " + field + " mismatch! Expected: " + expected + " | Actual: " + actual);
            test.fail("❌ " + field + " mismatch! Expected: " + expected + " | Actual: " + actual);
        }
    }

    private String maskAccount(String accountNumber) {

        if (accountNumber == null || accountNumber.isEmpty())
            return null;

        if (accountNumber.length() <= 8)
            return "*".repeat(accountNumber.length());

        String start = accountNumber.substring(0, 4);
        String end = accountNumber.substring(accountNumber.length() - 4);
        String masked = "*".repeat(accountNumber.length() - 8);

        return start + masked + end;
    }
}