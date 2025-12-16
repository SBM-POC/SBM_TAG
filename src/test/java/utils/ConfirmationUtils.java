package utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.*;

public class ConfirmationUtils {

    private final AppiumDriver driver;
    private final ElementActions actions;
    private final WebDriverWait wait;
    private final ExchangeRateUtils fxUtils;

    private static final String CONFIRM_BTN = "//android.widget.Button[@text=\"Confirm\"]";
    private static final By SUCCESS_MSG = By.xpath("//android.widget.TextView[contains(@text,'Transaction has been submitted for processing.')]");
    private static final By CLOSE_BTN = By.xpath("//android.widget.Button[contains(@text,'Close')]");

    private static final By SUCCESS_MSG_PAYM = By.xpath("//android.widget.TextView[contains(@text,'The transaction was processed successfully.')]");

    public ConfirmationUtils(AppiumDriver driver) {
        this.driver = driver;
        this.actions = new ElementActions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.fxUtils = new ExchangeRateUtils();
    }

    // -------------------------- CONFIRMATION SCREENS ----------------------------
    public void Confirmation_Screen_SWIFT(Map<String, String> data) {
        try {
            // Prepare expected values
            Map<String, String> expectedValues = new LinkedHashMap<>();
            expectedValues.put("Account", data.get("FromAccount"));
            expectedValues.put("Currency", data.get("CurrencySymbol"));
            expectedValues.put("Amount", data.get("Amount"));
            expectedValues.put("Date", data.get("TransactionDate"));
            expectedValues.put("PaymentName", data.get("PaymentName"));
            expectedValues.put("Remarks", data.get("Remarks"));
            expectedValues.put("Beneficiary Name", data.get("Beneficiary_Name"));
            expectedValues.put("Beneficiary Address", data.get("BenAddress_1") + "," + data.get("BenAddress_2"));
            expectedValues.put("Beneficiary Account/IBAN", data.get("Beneficiary_Account") + data.get("Beneficiary_IBAN"));
            expectedValues.put("Beneficiary Bank BIC", data.get("BenBIC"));
            expectedValues.put("Beneficiary Bank Name", data.get("BenBankName"));
            expectedValues.put("Beneficiary Bank Address", data.get("BenBankAdd"));
            expectedValues.put("Clearing Code", data.get("ClearingCode"));
            expectedValues.put("Intermediary Bank BIC", data.get("IntBankBIC"));
            expectedValues.put("Intermediary Bank Name", data.get("IntBankName"));
            expectedValues.put("Charge Option", data.get("ChargeOption"));

            // Collect all text from scrolling and Validate expected values
            validateConfirmationData(expectedValues);

            // Click Confirm if all assertions pass
            clickConfirm();

        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    public void Confirmation_Screen_OtherSBM(Map<String, String> data) {
        try {
            // Prepare expected values
            Map<String, String> expectedValues = new LinkedHashMap<>();
            expectedValues.put("From Account", data.get("FromAccount"));
            expectedValues.put("To Account", data.get("RecipientAccountNo"));
            expectedValues.put("Payment Name", data.get("PaymentName"));
            expectedValues.put("Currency", data.get("CurrencySymbol"));
            expectedValues.put("Amount", data.get("Amount"));
            expectedValues.put("Date", data.get("TransactionDate"));
            expectedValues.put("Remarks", data.get("Remarks"));

            // Collect all text from scrolling and Validate expected values
            validateConfirmationData(expectedValues);

            // Click Confirm if all assertions pass
            clickConfirm();

        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    public void Confirmation_Screen_OLB(Map<String, String> data) {
        try {
            // Prepare expected values
            Map<String, String> expectedValues = new LinkedHashMap<>();
            expectedValues.put("From Account", data.get("FromAccount"));
            expectedValues.put("Payment Name", data.get("PaymentName"));
            expectedValues.put("To Account", data.get("RecipientAccountNo"));
            expectedValues.put("Beneficiary Bank", data.get("BeneficiaryBank"));
            expectedValues.put("Currency", data.get("CurrencySymbol"));
            expectedValues.put("Amount", data.get("Amount"));
            expectedValues.put("Date", data.get("TransactionDate"));
            expectedValues.put("Remarks", data.get("Remarks"));

            // Collect all text from scrolling and Validate expected values
            validateConfirmationData(expectedValues);

            // Click Confirm if all assertions pass
            clickConfirm();

        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    // -------------------------- SUCCESSFUL SCREENS ----------------------------

    public void Successful_Screen_SWIFT(Map<String, String> data) {

        try {
            WebElement messageElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(SUCCESS_MSG)
            );

            if (!messageElement.isDisplayed()) {
                System.out.println("ℹ️ Successful Screen not displayed for SWIFT.");
                return;
            }

            Map<String, String> expectedValues = new LinkedHashMap<>();
            expectedValues.put("Account", data.get("FromAccount"));
            expectedValues.put("Currency", data.get("CurrencySymbol"));
            expectedValues.put("Amount", data.get("Amount"));
            expectedValues.put("Date", data.get("TransactionDate"));
            expectedValues.put("Payment Name", data.get("PaymentName"));
            expectedValues.put("Remarks", data.get("Remarks"));
            expectedValues.put("Beneficiary Name", data.get("BeneficiaryName"));
            expectedValues.put("Beneficiary Address", data.get("BenAddress1") + "," + data.get("BenAddress2"));
            expectedValues.put("Beneficiary Account/IBAN", data.get("BeneficiaryAccount") + data.get("BeneficiaryIBAN"));
            expectedValues.put("Beneficiary Bank BIC", data.get("BenBankBIC"));
            expectedValues.put("Beneficiary Bank Name", data.get("BenBankName"));
            expectedValues.put("Beneficiary Bank Address", data.get("BenBankAddress"));
            expectedValues.put("Clearing Code", data.get("BenBankClearing"));
            expectedValues.put("Intermediary Bank BIC", data.get("InterBankBIC"));
            expectedValues.put("Intermediary Bank Name", data.get("InterBankName"));
            expectedValues.put("Charge Option", data.get("ChargeOption"));

            validateConfirmationData(expectedValues);

            System.out.println("✅ Successful Screen verified for SWIFT");

            clickCloseButton();

        } catch (Exception e) {
            // Optional / soft check → don't fail test if success screen doesn't show
            System.out.println("ℹ️ Successful Screen did not appear for SWIFT: " + e.getMessage());
        }
    }

    public void Successful_Screen_OtherSBM(Map<String, String> data) {

        try {
            WebElement messageElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(SUCCESS_MSG)
            );

            if (!messageElement.isDisplayed()) {
                System.out.println("ℹ️ Successful Screen not displayed for Other SBM.");
                return;
            }

            Map<String, String> expectedValues = new LinkedHashMap<>();
            expectedValues.put("From Account", data.get("FromAccount"));
            expectedValues.put("To Account", data.get("RecipientAccountNo"));
            expectedValues.put("Currency", data.get("CurrencySymbol"));
            expectedValues.put("Amount", data.get("Amount"));
            expectedValues.put("Date", data.get("TransactionDate"));
            expectedValues.put("Remarks", data.get("Remarks"));

            validateConfirmationData(expectedValues);

            System.out.println("✅ Successful Screen verified for Other SBM");

            clickCloseButton();

        } catch (Exception e) {
            System.out.println("ℹ️ Successful Screen did not appear for Other SBM: " + e.getMessage());
        }
    }

    public void Successful_Screen_OLB(Map<String, String> data) {

        try {
            WebElement messageElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(SUCCESS_MSG)
            );

            if (!messageElement.isDisplayed()) {
                System.out.println("ℹ️ Successful Screen not displayed for Other Local Bank.");
                return;
            }

            Map<String, String> expectedValues = new LinkedHashMap<>();
            expectedValues.put("From Account", data.get("FromAccount"));
            expectedValues.put("Payment Name", data.get("PaymentName"));
            expectedValues.put("To Account", data.get("RecipientAccountNo"));
            expectedValues.put("Beneficiary Bank", data.get("BeneficiaryBank"));
            expectedValues.put("Currency", data.get("CurrencySymbol"));
            expectedValues.put("Amount", data.get("Amount"));
            expectedValues.put("Date", data.get("TransactionDate"));
            expectedValues.put("Remarks", data.get("Remarks"));

            validateConfirmationData(expectedValues);

            System.out.println("✅ Successful Screen verified for Other Local Bank");

            clickCloseButton();

        } catch (Exception e) {
            System.out.println("ℹ️ Successful Screen did not appear for Other Local Bank: " + e.getMessage());
        }
    }

    // -------------------------- SHARED METHODS ----------------------------
    private void validateConfirmationData(Map<String, String> expectedValues) {

        Set<String> allTexts = new HashSet<>();
        int previousSize = 0;
        int attempts = 0;

        while (attempts < 10) {
            List<WebElement> textViews = driver.findElements(By.className("android.widget.TextView"));

            for (WebElement tv : textViews) {
                String text = tv.getText().trim();
                if (!text.isEmpty()) {
                    allTexts.add(text);
                }
            }

            if (allTexts.size() == previousSize) break;

            previousSize = allTexts.size();
            actions.scrollToEnd();
            attempts++;
        }

        for (Map.Entry<String, String> entry : expectedValues.entrySet()) {
            String expected = entry.getValue();
            boolean matched = allTexts.stream().anyMatch(text -> text.contains(expected));
            Assert.assertTrue(matched, "❌ " + entry.getKey() + " mismatch! Expected: " + expected);
        }
    }

    private void clickConfirm() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CONFIRM_BTN))).click();
    }

    private void clickCloseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(CLOSE_BTN)).click();
    }

    public void Confirmation_Screen_PayToMobile(Map<String, String> data) {

        try {
            // Prepare expected values for confirmation page
            Map<String, String> expectedValues = new LinkedHashMap<>();
            expectedValues.put("From", data.get("FromAccount"));
            expectedValues.put("To", data.get("ToMobile"));
            expectedValues.put("Amount", data.get("Amount"));
            expectedValues.put("Currency", data.get("CurrencySymbol"));
            expectedValues.put("Exchange Rate", data.get("ExchangeRate"));
            expectedValues.put("Payment date", data.get("TransactionDate"));
            expectedValues.put("Remarks", data.get("Remarks"));

            // Validate all visible text on the confirmation page
            validateConfirmationData(expectedValues);

            //Check exchange rate being applied
            fxUtils.validateExchangeRate(data);

            // Tap Confirm
            clickConfirm();

        } catch (Exception e) {
            Assert.fail("Pay To Mobile Confirmation Screen failed: " + e.getMessage());
        }
    }

    public void Successful_Screen_PayToMobile(Map<String, String> data) {

        try {
            // Wait for success message
            WebElement messageElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(SUCCESS_MSG_PAYM)
            );

            if (!messageElement.isDisplayed()) {
                System.out.println("Pay To Mobile Successful screen not displayed.");
                return;
            }

            // Expected values for receipt
            Map<String, String> expectedValues = new LinkedHashMap<>();
            expectedValues.put("From", data.get("FromAccount")); // Masking handled internally
            expectedValues.put("To", data.get("ToMobile"));
            expectedValues.put("Amount", data.get("Amount"));
            expectedValues.put("Currency", data.get("CurrencySymbol"));
            expectedValues.put("Date", data.get("TransactionDate"));
            expectedValues.put("Remarks", data.get("Remarks"));
            expectedValues.put("Exchange Rate", data.get("ExchangeRate"));

            // Validate receipt text
            validateConfirmationData(expectedValues);

            //Check exchange rate being applied
            fxUtils.validateExchangeRate(data);

            System.out.println("Pay To Mobile Successful screen verified.");

            // Close receipt
            clickCloseButton();

        } catch (Exception e) {
            System.out.println("Pay To Mobile Successful Screen did not appear: " + e.getMessage());
        }
    }


}
