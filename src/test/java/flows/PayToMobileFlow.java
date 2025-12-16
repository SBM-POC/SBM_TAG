package flows;

import base.BasePage;
import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.PayToMobilePage;
import utils.ElementActions;
import utils.ExchangeRateUtils;

import java.time.Duration;
import java.util.Map;

public class PayToMobileFlow extends BasePage {
    private final PayToMobilePage pay = new PayToMobilePage();

    private final ExchangeRateUtils fxUtils;

    public PayToMobileFlow() {
        this.actions = new ElementActions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.fxUtils = new ExchangeRateUtils();
    }

    // Positive Scenario
    public void payToMobilePositiveFlow(Map<String, String> data) throws Exception {

        ExtentTest test = BaseTest.getTest().get();
        test.info("Starting Pay to Mobile - Positive Flow");

        try {
            //Navigate to Pay to Mobile
            pay.openPayToMobile();

            // Enter mobile number
            pay.enterMobileNumber(data.get("MobileNumber"));

            // Search User
            pay.searchUser();

            // Verify +230 auto prefix for Mauritius numbers
            if (data.get("MobileNumber").startsWith("5") && data.get("MobileNumber").length() == 8) {
                String expectedPrefix = "+230" + data.get("MobileNumber");
                boolean prefixVisible = isVisible("//android.widget.EditText[@text='" + expectedPrefix + "']");

                if (!prefixVisible) {
                    test.fail("Auto prefix +230 was not applied");
                    throw new Exception("Prefix not applied");
                }
                test.pass("Verified auto prefix applied: " + expectedPrefix);
            }

            // Wait for transition to next screen
            Thread.sleep(1500);

            // Select From Account
            pay.selectFromAccount(data.get("FromAccount"));

            // Verify currency matches expected
            if (data.get("Currency") != null) {
                boolean isCurrencyPresent =
                        isVisible("//android.widget.EditText[@text='" + data.get("Currency") + "']");
                if (!isCurrencyPresent) {
                    test.fail("Currency mismatch. Expected: " + data.get("Currency"));
                    throw new Exception("Currency mismatch");
                }
                test.pass("Currency verified: " + data.get("Currency"));
            }

            // Enter Amount
            pay.enterAmount(data.get("Amount"));

            // Enter Remarks
            pay.enterRemarks(data.get("Remarks"));

            // Continue
            pay.tapContinue();

            // Confirmation Screen
            waitAndClick("//android.widget.Button[@text=\"Confirm\"]");

            //Check exchange rate being applied
            fxUtils.validateExchangeRate(data);

            // Success Screen
            //Click on generate receipt
            waitAndClick("//android.widget.Button[@text=\"Generate and Share\"]");

            //Verify receipt - Need to update Receipt Validator Method

            //Verify Transaction History - Skeleton for Method Transaction History already created in TransactionVerificationUtils

        } catch (Exception e) {
            test.fail("Pay to Mobile Positive Flow failed: " + e.getMessage());
            throw e;
        }
    }

    public void payToMobileNegativeFlow(Map<String, String> data) throws Exception {

        ExtentTest test = BaseTest.getTest().get();
        String scenario = data.get("Scenario");
        String expectedError = data.get("ExpectedError");

        test.info("Starting Pay to Mobile - Negative Scenario: " + scenario);

        try {
            pay.openPayToMobile();

            pay.enterMobileNumber(data.get("MobileNumber"));
            pay.searchUser();

            // ------------------------------------------------
            // ERRORS BEFORE USER FOUND
            // ------------------------------------------------
            switch (scenario) {

                case "InvalidMobile":
                case "MobileLessThan8":
                case "MobileNotStartingWith5":
                    validateError(pay.isInvalidMobileErrorVisible(), expectedError, test);
                    return;

                case "MobileNotRegistered":
                    validateError(pay.isMobileNotEligibleErrorVisible(), expectedError, test);
                    return;
            }

            // ------------------------------------------------
            // ERRORS AFTER USER FOUND
            // ------------------------------------------------
            Thread.sleep(1500);

            if (!scenario.equalsIgnoreCase("AmountRequired")) {
                pay.enterAmount(data.get("Amount"));
            }

            if (!scenario.equalsIgnoreCase("RemarksRequired")) {
                pay.enterRemarks(data.get("Remarks"));
            }

            pay.tapContinue();

            switch (scenario) {

                case "AmountRequired":
                    validateError(pay.isAmountRequiredErrorVisible(), expectedError, test);
                    break;

                case "RemarksRequired":
                    validateError(pay.isRemarksRequiredErrorVisible(), expectedError, test);
                    break;

                case "AmountFormat":
                    validateError(pay.isAmountFormatErrorVisible(), expectedError, test);
                    break;

                case "AmountGreaterThanBalance":
                    validateError(pay.isAmountGreaterThanBalanceVisible(), expectedError, test);
                    break;

                case "ExceedDailyLimit":
                case "ExceedRemainingLimit":
                    validateError(pay.isDailyLimitErrorVisible(), expectedError, test);
                    break;

                default:
                    test.fail("Scenario not recognized: " + scenario);
            }

        } catch (Exception e) {
            test.fail("Negative flow failed: " + e.getMessage());
            throw e;
        }
    }


    private void validateError(boolean condition, String expectedText, ExtentTest test) {
        if (condition) {
            test.pass("✅ Error displayed as expected: " + expectedText);
        } else {
            test.fail("❌ Expected error NOT displayed: " + expectedText);
            throw new AssertionError(expectedText);
        }
    }

}



