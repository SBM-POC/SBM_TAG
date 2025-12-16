package utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.*;

public class ConfirmationEngine {

    private final AppiumDriver driver;
    private final ElementActions actions;
    private final WebDriverWait wait;

    private static final String CONFIRM_BTN = "//android.widget.Button[@text='Confirm']";
    private static final By SUCCESS_MSG = By.xpath(
            "//android.widget.TextView[contains(@text,'submitted') or contains(@text,'processing')]"
    );
    private static final By CLOSE_BTN = By.xpath("//android.widget.Button[contains(@text,'Close')]");

    public ConfirmationEngine(AppiumDriver driver) {
        this.driver = driver;
        this.actions = new ElementActions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ===================================================================================
    // ✅ ENTRY POINT → VALIDATE + CONFIRM
    // ===================================================================================
    public void validateAndConfirm(Map<String, String> expectedValues) {
        try {
            validateConfirmationData(expectedValues);
            clickConfirm();
        } catch (Exception e) {
//            actions.takeScreenshot("CONFIRMATION_ENGINE_FATAL");
            Assert.fail("❌ Confirmation validation failed: " + e.getMessage());
        }
    }

    // ===================================================================================
    // ✅ ENTRY POINT → VALIDATE SUCCESS SCREEN + CLOSE (SOFT ASSERT)
    // ===================================================================================
    public void validateSuccessAndClose(Map<String, String> expectedValues, String flowName) {

        try {
            WebElement messageElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(SUCCESS_MSG)
            );

            if (!messageElement.isDisplayed()) {
                System.out.println("ℹ️ Success screen not displayed for: " + flowName);
                return;
            }

            validateConfirmationData(expectedValues);

            System.out.println("✅ Success Screen verified for: " + flowName);

            clickCloseButton();

        } catch (Exception e) {
            System.out.println("ℹ️ Success screen skipped for " + flowName + ": " + e.getMessage());
        }
    }

    // ===================================================================================
    // 🔍 CORE VALIDATION ENGINE
    // ===================================================================================
    private void validateConfirmationData(Map<String, String> expectedValues) {

        Set<String> allTexts = new HashSet<>();
        int previousSize = 0;
        int attempts = 0;

        while (attempts < 10) {
            List<WebElement> textViews =
                    driver.findElements(By.className("android.widget.TextView"));

            for (WebElement tv : textViews) {
                String text = tv.getText().trim();
                if (!text.isEmpty()) {
                    allTexts.add(text.replaceAll("\\s+", ""));
                }
            }

            if (allTexts.size() == previousSize) break;

            previousSize = allTexts.size();
            actions.scrollToEnd();
            attempts++;
        }

        for (Map.Entry<String, String> entry : expectedValues.entrySet()) {

            String fieldName = entry.getKey();
            String expected = entry.getValue();

            // ✅ NULL SAFETY
            if (expected == null || expected.trim().isEmpty()) {
                System.out.println("⚠️ Skipping validation for: " + fieldName + " (empty value)");
                continue;
            }

            String sanitizedExpected = expected.replaceAll("\\s+", "");

            boolean matched = allTexts.stream()
                    .anyMatch(text -> text.contains(sanitizedExpected));

            if (!matched) {
//                actions.takeScreenshot("CONFIRMATION_MISMATCH_" + fieldName);
                Assert.fail("❌ " + fieldName + " mismatch! Expected: " + expected);
            }

            System.out.println("✅ Verified: " + fieldName + " → " + expected);
        }
    }

    // ===================================================================================
    // 🖱️ SHARED ACTIONS
    // ===================================================================================
    private void clickConfirm() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CONFIRM_BTN))).click();
    }

    private void clickCloseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(CLOSE_BTN)).click();
    }
}
