package pages;

import base.BasePage;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ElementActions;
import utils.ExchangeRateUtils;

import java.time.Duration;
import java.util.Map;

public class MobileTopUpPage extends BasePage {

    private final ExchangeRateUtils fxUtils;

    public MobileTopUpPage() {
        this.actions = new ElementActions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.fxUtils = new ExchangeRateUtils();
    }

    //Locators
    private static final String TAB_TRANSFER = "//android.widget.TabWidget/child::android.view.View[contains(@resource-id, 'tab-button')]/child::android.view.View[contains(@text, 'Transfer')]";
    private static final String TAB_MOBILE_TOPUP = "//android.widget.TextView[contains(@text, 'Mobile TopUp')]/parent::android.view.View[contains(@resource-id, 'menu-option')]";

    private static final String EMTEL = "//android.widget.TextView[@text=\"Emtel\"]";
    private static final String MTML = "//android.widget.TextView[@text=\"MTML\"]";
    private static final String MYT = "//android.widget.TextView[@text=\"My.T\"]";

    private static final String MOBILE_TOPUP_FROM_ACCOUNT = "//android.widget.Button[contains(@text,\"Available balance\")]";
    private static final String MOBILE_NUMBER = "//android.widget.TextView[contains(@text,'Mobile number')]/parent::*/descendant::android.widget.EditText";
    private static final String MOBILE_TOPUP_CURRENCY = "//android.widget.TextView[contains(@text,'Currency')]/following-sibling::android.view.View[contains(@resource-id,'multi-currency-btn')]\t\n" + "/android.widget.EditText[@text=\"MUR\"]";
    private static final String MOBILE_TOPUP_AMOUNT = "//android.widget.TextView[contains(@text,'Amount')]/following-sibling::android.view.View[contains(@resource-id,'multi-currency-btn')]";
    private static final String MOBILE_TOPUP_TOGGLE = "//android.widget.ToggleButton[@resource-id=\"ion-tg-2\"]";
    private static final String MOBILE_TOPUP_SAVEAS_NAME = "//android.view.View[@resource-id=\"save_as\"]/android.widget.EditText";
    private static final String MOBILE_TOPUP_SAVE_CONTINUE = "//android.widget.Button[@text=\"Save & Continue\"]";
    private static final String MOBILE_TOPUP_SAVE = "//android.widget.Button[@text=\"Save\"]";
    private static final String MOBILE_TOPUP_CANCEL = "//android.widget.Button[@text=\"Cancel\"]";
    private static final String MOBILE_TOPUP_CONFIRM = "//android.widget.Button[@text=\"Confirm\"]";

    // ---------------- NAVIGATION ----------------
    public void openMobileTopUp() {
        waitAndClick(TAB_TRANSFER);
        waitAndClick(TAB_MOBILE_TOPUP);
    }

    // ---------------- OPERATOR ----------------
    public void selectOperator(String operator) {
        switch (operator.toUpperCase()) {
            case "EMTEL": waitAndClick(EMTEL); break;
            case "MTML": waitAndClick(MTML); break;
            case "MYT":   waitAndClick(MYT); break;
        }
    }

    // ---------------- FORM ACTIONS ----------------
    public void selectFromAccount(String account) {
        waitAndClick(MOBILE_TOPUP_FROM_ACCOUNT);
        selectDropdownValue(account);
    }

    public void enterMobileNumber(String mobile) {
        waitAndType(MOBILE_NUMBER, mobile);
    }

    public void selectAmount(String amount) {
        waitAndClick(MOBILE_TOPUP_AMOUNT);
        selectDropdownValue(amount);
    }
    public void saveAndContinue() {
        waitAndClick(MOBILE_TOPUP_SAVE_CONTINUE);
    }


    public void enableSaveAs(String name) {
        waitAndClick(MOBILE_TOPUP_TOGGLE);
        waitAndType(MOBILE_TOPUP_SAVEAS_NAME, name);
        waitAndClick(MOBILE_TOPUP_SAVE_CONTINUE);
    }

    public void saveWithoutAlias() {
        waitAndClick(MOBILE_TOPUP_SAVE);
    }

    public void tapConfirm() {
        waitAndClick(MOBILE_TOPUP_CONFIRM);
    }

    public void generateReceipt() {
        waitAndClick("//android.widget.Button[@text=\"Generate and Share\"]");
    }

    // ---------------- ERROR CHECKS ----------------
    public boolean isMobileBlankErrorVisible() {
        return isVisible("//android.widget.TextView[@text='Enter recipient mobile number or identifier']");
    }

    public boolean isInvalidMobileErrorVisible() {
        return isVisible("//android.widget.TextView[@text='Enter recipient mobile number']");
    }

    public boolean isAmountRequiredErrorVisible() {
        return isVisible("//android.widget.TextView[@text='Amount is required']");
    }

    public boolean isLimitErrorVisible() {
        return isVisible("//android.widget.TextView[contains(@text,'limit')]");
    }

    // ----------------------------------------------------------
    //  METHODS
    // ----------------------------------------------------------

    public void validateOperatorsDisplayed(ExtentTest test) throws Exception {

        boolean emtelVisible = isVisible(EMTEL);
        boolean mtmlVisible  = isVisible(MTML);
        boolean mytVisible   = isVisible(MYT);

        if (!emtelVisible || !mtmlVisible || !mytVisible) {
            test.fail("One or more operators NOT displayed on Mobile TopUp screen.");
            throw new Exception("Operators missing → Emtel: " + emtelVisible +
                    ", MTML: " + mtmlVisible +
                    ", MyT: " + mytVisible);
        }
        test.pass("All operators displayed: Emtel, MTML, MyT");
    }

    public void validateOperatorDenominations(String operator, ExtentTest test) throws Exception {

        String[] expectedValues;

        switch (operator.toUpperCase()) {
            case "EMTEL":
                expectedValues = new String[]{"50", "125", "225", "475"};
                break;

            case "MYT":
                expectedValues = new String[]{"50", "100", "300", "500", "1000"};
                break;

            case "MTML":
                expectedValues = new String[]{"25", "50", "100", "300"};
                break;

            default:
                throw new Exception("Invalid operator for denomination validation: " + operator);
        }

        // Loop & verify UI contains the expected values
        for (String value : expectedValues) {
            boolean found = isVisible("//*[contains(@text,'" + value + "')]");
            if (!found) {
                test.fail("Expected denomination not found for " + operator + ": " + value);
                throw new Exception("Denomination missing: " + value);
            }
        }
        test.pass("Validated all denominations for " + operator);
    }

    public void validateFromAccountDetails(Map<String, String> data, ExtentTest test) throws Exception {

        // Step 1 — Get the FULL text from the selected account button
        String accountText = getText(MOBILE_TOPUP_FROM_ACCOUNT);
        test.info("Extracted From Account text: " + accountText);

        // Verify Account Currency
        String expectedCurrency = data.get("Currency");

        if (!accountText.startsWith(expectedCurrency)) {
            test.fail("Account currency mismatch! Expected currency: " + expectedCurrency + " | Found: " + accountText);
            throw new Exception("Account currency validation failed.");
        }
        test.pass("Verified Account Currency: " + expectedCurrency);

        // Verify Account Nickname
        String expectedNickname = data.get("Nickname");

        if (expectedNickname != null && !accountText.contains(expectedNickname)) {
            test.fail("Account nickname mismatch! Expected: " + expectedNickname + " | Found: " + accountText);
            throw new Exception("Account nickname validation failed.");
        }
        test.pass("Verified Account Nickname: " + expectedNickname);

        // Verify available balance
        String expectedBalance = data.get("Balance");

        if (expectedBalance != null && !accountText.contains(expectedBalance)) {
            test.fail("Available balance mismatch! Expected: " + expectedBalance + " | Found: " + accountText);
            throw new Exception("Balance validation failed.");
        }
        test.pass("Verified Available Balance: " + expectedBalance);
    }

    public void validateTopUpCurrencyIsMUR(ExtentTest test) throws Exception {

        boolean MUR = isVisible(MOBILE_TOPUP_CURRENCY);

        if (!MUR) {
            test.fail("Currency should be MUR for Mobile TopUp!");
            throw new Exception("Mobile TopUp must always be in MUR.");
        }

        test.pass("Verified: Transfer is being done in MUR only.");
    }

    public void validateAutoPrefix(String mobile, ExtentTest test) throws Exception {
        if (mobile.startsWith("5") && mobile.length() == 8) {
            String expectedValue = "+230" + mobile;

            boolean prefixApplied =
                    isVisible("//android.widget.EditText[@text='" + expectedValue + "']");

            if (!prefixApplied) {
                test.fail("Auto prefix +230 NOT applied for: " + mobile);
                throw new Exception("Auto prefix validation failed");
            }

            test.pass("Verified Auto Prefix applied: " + expectedValue);
        }
    }

    public void handleSaveAsToggle(Map<String, String> data, ExtentTest test) throws Exception {
        String saveAs = data.get("SaveAs");

        if (saveAs.equalsIgnoreCase("YES")) {
            waitAndClick(MOBILE_TOPUP_TOGGLE); // enable toggle
            waitAndType(MOBILE_TOPUP_SAVEAS_NAME, data.get("SaveName"));
            waitAndClick(MOBILE_TOPUP_SAVE_CONTINUE);
            test.pass("Save As enabled with name: " + data.get("SaveName"));
        } else {
            // If SaveAs = NO, click Save button directly
            test.info("Save As disabled");
            waitAndClick(MOBILE_TOPUP_SAVE);
        }
    }

    public void assertErrorVisible(String xpath, String expectedText, ExtentTest test) {

        if (isVisible(xpath)) {
            test.pass("Error message displayed as expected: " + expectedText);
        } else {
            test.fail("Expected error NOT displayed: " + expectedText);
            throw new AssertionError("Error not visible: " + expectedText);
        }
    }



    // ----------------------------------------------------------
    // PLACEHOLDER FUNCTIONS FOR FUTURE IMPLEMENTATION
    // ----------------------------------------------------------

    public void verifyBalanceUpdated(Map<String, String> data) {
        // TODO — integrate logic
    }

    public void verifyTransactionHistory(Map<String, String> data) {
        // TODO — integrate logic
    }
}

