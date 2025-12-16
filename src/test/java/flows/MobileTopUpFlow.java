package flows;

import base.BasePage;
import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import pages.MobileTopUpPage;
import utils.ExchangeRateUtils;

import java.util.Map;

public class MobileTopUpFlow extends BasePage {

    private final MobileTopUpPage mobile = new MobileTopUpPage();
    private final ExchangeRateUtils fxUtils = new ExchangeRateUtils();

    // ----------------------------------------------------------
    // POSITIVE FLOW
    // ----------------------------------------------------------
    public void MobileTopUpPositiveFlow(Map<String, String> data) throws Exception {

        ExtentTest test = BaseTest.getTest().get();
        test.info("Starting Mobile TopUp - Positive Flow");

        try {
            // Navigate to Mobile TopUp
            mobile.openMobileTopUp();

            // Verify if Operators are displayed
            mobile.validateOperatorsDisplayed(test);

            // Select Operator
            mobile.selectOperator(data.get("Operator"));

            // Select From Account
            mobile.selectFromAccount(data.get("FromAccount"));

            //Verify From Account Details
            mobile.validateFromAccountDetails(data, test);

            // Enter Mobile Number
            mobile.enterMobileNumber(data.get("MobileNumber"));

            // Validate Auto Prefix
            mobile.validateAutoPrefix(data.get("MobileNumber"), test);

            // Select Amount
            mobile.selectAmount(data.get("Amount"));

            //Verify Currency is MUR
            mobile.validateTopUpCurrencyIsMUR(test);

            // Verify the denominations of the operators
            mobile.validateOperatorDenominations(data.get("Operator"), test);

            selectDropdownValue(data.get("Amount"));

            // Handle Save As Toggle
            mobile.handleSaveAsToggle(data, test);

            // Confirmation Screen
            mobile.tapConfirm();

            //Check exchange rate being applied
            fxUtils.validateExchangeRate(data);

            // Success Screen
            //Click on generate receipt
            mobile.generateReceipt();

            // Validate Receipt - Call Receipt Validator Method

            // verifyBalanceUpdated(data);
            // verifyTransactionHistory(data);

            test.pass("Mobile TopUp - Positive Flow Completed Successfully");

        } catch (Exception e) {
            test.fail("Mobile TopUp Positive Flow failed: " + e.getMessage());
            throw e;
        }
    }

    // ----------------------------------------------------------
    // NEGATIVE FLOW
    // ----------------------------------------------------------
    public void MobileTopUpNegativeFlow(Map<String, String> data) throws Exception {

        ExtentTest test = BaseTest.getTest().get();
        String scenario = data.get("Scenario");
        String expectedError = data.get("ExpectedError");

        test.info("Starting Mobile TopUp - Negative Scenario: " + scenario);

        try {
            // Navigate to Mobile TopUp
            mobile.openMobileTopUp();

            // Select Operator
            mobile.selectOperator(data.get("Operator"));

            Thread.sleep(1000);

            // Select From Account
            mobile.selectFromAccount(data.get("FromAccount"));

            // Enter Mobile Number
            mobile.enterMobileNumber(data.get("MobileNumber"));

            // SCENARIO: Required fields empty BEFORE selecting operator
            if (scenario.equalsIgnoreCase("MobileBlank")) {
                mobile.saveAndContinue(); // Try to continue without entering anything
                validateError(mobile.isMobileBlankErrorVisible(), expectedError, test);
                return;
            }

            // SCENARIO: Invalid mobile (landline or international)
            if (scenario.equalsIgnoreCase("InvalidMobile")) {
                mobile.saveAndContinue();
                validateError(mobile.isInvalidMobileErrorVisible(), expectedError, test);
                return;
            }

            // Select Amount
            mobile.selectAmount(data.get("Amount"));

            // SCENARIO: Amount Blank
            if (scenario.equalsIgnoreCase("AmountBlank")) {
                mobile.saveAndContinue();
                validateError(mobile.isAmountRequiredErrorVisible(), expectedError, test);
                return;
            }

            // SCENARIO: Exceed Daily Limit
            if (scenario.equalsIgnoreCase("ExceedDailyLimit")) {
                mobile.selectAmount(data.get("Amount")); // This amount must exceed the remaining limit
                mobile.saveAndContinue();

                // TODO: Update when you get exact message
                validateError(mobile.isLimitErrorVisible(), expectedError, test);
                return;
            }
            test.fail("Scenario not recognized: " + scenario);

        } catch (Exception e) {
            test.fail("Mobile TopUp Negative Flow failed: " + e.getMessage());
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
