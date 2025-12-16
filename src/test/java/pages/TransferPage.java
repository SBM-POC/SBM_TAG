package pages;

import base.BasePage;
import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;

public class TransferPage extends BasePage {

    // ---------------------------------------------------------
    //  MAIN NAVIGATION
    // ---------------------------------------------------------
    private static final String TAB_TRANSFER = "//android.view.View[contains(@text, 'Transfer')]";
    private static final String TAB_ACCOUNT_TRANSFER = "//android.widget.TextView[contains(@text, 'Account Transfers')]";
    private static final String TAB_LOCAL_BANK = "//android.widget.Button[contains(@text, 'Other Local Bank Transfer')]";
    private static final String TAB_SBM = "//android.widget.Button[contains(@text, 'Other SBM Account Transfer')]";
    private static final String TAB_OWN_ACCOUNT = "//android.widget.Button[contains(@text, 'Own Account Transfer')]";
    private static final String TAB_SWIFT = "//android.widget.Button[contains(@text, 'SWIFT Transfer')]";

    private static final String BTN_CLOSE = "//android.widget.Button[contains(@text, 'Close')]";

    // ---------------------------------------------------------
    //  COMMON FIELDS FOR LOCAL/SBM/SWIFT
    // ---------------------------------------------------------
    private static final String GENERIC_FROM_ACCOUNT = "(//android.widget.Button[@index='0'])[3]";
    private static final String BENEF_ACC = "(//android.widget.EditText[@index='0'])[1]";
    private static final String BENEF_NAME = "(//android.widget.EditText[@index='0'])[2]";
    private static final String BENEF_BANK = "(//android.widget.EditText[@index='0'])[3]";
    private static final String GENERIC_BENEF_BANK = "//android.widget.TextView[contains(@text, 'Beneficiary Bank')]/parent::android.view.View[contains(@resource-id, 'ion-input')]/following-sibling::android.view.View[contains(@resource-id, 'BW_select')]/descendant::android.widget.EditText";
    private static final String BENEF_AMOUNT = "(//android.widget.EditText[@index='0'])[5]";
    private static final String BENEF_REMARK = "(//android.widget.EditText[@index='0'])[6]";
    private static final String GENERIC_CURRENCY = "(//android.widget.EditText[@index='0'])[4]";

    private static final String BTN_CONTINUE = "//android.widget.Button[contains(@text, 'Continue')]";
    private static final String TITLE_CONFIRM = "//android.widget.TextView[contains(@text, 'Please confirm')]";
    private static final String BTN_CONFIRM = "//android.widget.Button[contains(@text, 'Confirm')]";

    private static final String BTN_RECEIPT = "//android.widget.Button[contains(@text,'Generate')]";
    private static final String MSG_RECEIPT = "//android.widget.TextView[contains(@text,'eBanking Ref') or contains(@text,'submitted')]";
    private static final String ERROR_LIMIT = "//android.widget.TextView[contains(@text,'Single Amount limit')]";

    // ---------------------------------------------------------
    //  OWN ACCOUNT TRANSFER FIELDS
    // ---------------------------------------------------------
    private static final String OAT_FROM = "//android.widget.TextView[@text='From Account']/following-sibling::android.view.View//android.widget.Button";
    private static final String OAT_TO = "//android.widget.TextView[@text='To Account']/following-sibling::android.view.View//android.widget.Button";
    private static final String OAT_CURRENCY = "//android.widget.TextView[@text='Currency']/following-sibling::android.view.View//android.widget.EditText";
    private static final String OAT_AMOUNT = "//android.view.View[@resource-id='account-transfer-page-amount']//android.widget.EditText";
    private static final String OAT_REMARK = "//android.view.View[@resource-id='fundstransfer_srcnarrative']//android.widget.EditText";
    private static final String OAT_CONTINUE = "//android.widget.Button[@text='Continue']";

    // ---------------------------------------------------------
    //  SBM ONLY
    // ---------------------------------------------------------
    private static final String SBM_CURRENCY = "(//android.widget.EditText[@index='0'])[2]";
    private static final String SBM_AMOUNT = "(//android.widget.EditText[@index='0'])[3]";
    private static final String SBM_REMARK = "(//android.widget.EditText[@index='0'])[4]";

    // ---------------------------------------------------------
    //  SWIFT ONLY
    // ---------------------------------------------------------
    private static final String SWIFT_CURRENCY = "(//android.widget.EditText[@index='0'])[1]";
    private static final String SWIFT_AMOUNT = "(//android.widget.EditText[@index='0'])[2]";
    private static final String SWIFT_COUNTRY = "(//android.widget.EditText[@index='0'])[3]";
    private static final String SWIFT_TO_ACC = "//android.widget.EditText[@hint='Beneficiary Account / IBAN *']";
    private static final String SWIFT_NAME = "(//android.widget.EditText[@index='0'])[5]";

    private static final String SWIFT_ADDR1 = "(//android.widget.EditText[@index='0'])[3]";
    private static final String SWIFT_ADDR2 = "(//android.widget.EditText[@index='0'])[4]";
    private static final String SWIFT_BIC = "(//android.widget.EditText[@index='0'])[5]";

    private static final String SWIFT_BANK_NAME = "(//android.widget.EditText[@index='0'])[3]";
    private static final String SWIFT_BANK_ADDR = "(//android.widget.EditText[@index='0'])[4]";
    private static final String SWIFT_CHARGE = "(//android.widget.EditText[@index='0'])[3]";


    // ---------------------------------------------------------
    //  MODULE OPEN
    // ---------------------------------------------------------
    public void openTransferModule() {
        ExtentTest test = BaseTest.getTest().get();
        try {
            waitAndClick(TAB_TRANSFER);
            waitAndClick(TAB_ACCOUNT_TRANSFER);
            System.out.println("Navigated to Transfer page");
            test.info("Navigated to Transfer page");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Error navigating to Transfer page");
            test.fail("Error navigating to Transfer page");
        }
    }

    public void openLocalBank() {
        ExtentTest test = BaseTest.getTest().get();
        try {
            waitAndClick(TAB_LOCAL_BANK);
            System.out.println("Navigated to OLB page");
            test.info("Navigated to OLB page");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Error navigating to OLB Account transfer page");
            test.fail("Error navigating to OLB Account transfer page");
        }
    }
    public void openSBM() { waitAndClick(TAB_SBM); }
    public void openOwnAccount() { waitAndClick(TAB_OWN_ACCOUNT); }
    public void openSwift() { waitAndClick(TAB_SWIFT); }

    public void closePopupIfVisible() {
        if (isVisible(BTN_CLOSE)) waitAndClick(BTN_CLOSE);
    }

    // ---------------------------------------------------------
    //  GENERIC DROPDOWN SELECTION
    // ---------------------------------------------------------
    public void selectDropdownValue(String value) {

        try {
            driver.findElement(AppiumBy.xpath("//*[contains(@text,'" + value + "')]")).click();
        }
        catch (Exception e) {
            // fallback UIAutomator scroll
            String scroll =
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                            ".scrollIntoView(new UiSelector().textContains(\"" + value + "\"));";

            driver.findElement(AppiumBy.androidUIAutomator(scroll)).click();
        }
    }

    public void openGenericFromAccount() {
        waitAndClick(GENERIC_FROM_ACCOUNT);
    }
    public void openGenericBeneficiaryBank() {
        waitAndClick(GENERIC_BENEF_BANK);
    }


    // ---------------------------------------------------------
    //  OWN ACCOUNT ACTIONS
    // ---------------------------------------------------------
    public void oatSelectFrom() { waitAndClick(OAT_FROM); }
    public void oatSelectTo() { waitAndClick(OAT_TO); }
    public void oatTypeCurrency(String v) { waitAndType(OAT_CURRENCY, v); }
    public void oatTypeAmount(String v) { waitAndType(OAT_AMOUNT, v); }
    public void oatTypeRemarks(String v) { waitAndType(OAT_REMARK, v); }
    public void oatTapContinue() { waitAndClick(OAT_CONTINUE); }

    // ---------------------------------------------------------
    //  LOCAL/SBM COMMON TYPING
    // ---------------------------------------------------------
    public void typeBeneficiaryAccount(String v) { waitAndType(BENEF_ACC, v); }
    public void typeBeneficiaryName(String v) { waitAndType(BENEF_NAME, v); }
    public void typeBeneficiaryBank(String v) { waitAndType(BENEF_BANK, v); }
    public void typeCurrency(String v) { waitAndType(GENERIC_CURRENCY, v); }
    public void typeAmount(String v) { waitAndType(BENEF_AMOUNT, v); }
    public void typeRemark(String v) { waitAndType(BENEF_REMARK, v); }

    // SBM-only fields
    public void sbmCurrency(String v) { waitAndType(SBM_CURRENCY, v); }
    public void sbmAmount(String v) { waitAndType(SBM_AMOUNT, v); }
    public void sbmRemark(String v) { waitAndType(SBM_REMARK, v); }

    // ---------------------------------------------------------
    //  SWIFT TYPING
    // ---------------------------------------------------------
    public void swiftTypeCurrency(String v) { waitAndType(SWIFT_CURRENCY, v); }
    public void swiftTypeAmount(String v) { waitAndType(SWIFT_AMOUNT, v); }
    public void swiftTypeCountry(String v) { waitAndType(SWIFT_COUNTRY, v); }
    public void swiftTypeAccount(String v) { waitAndType(SWIFT_TO_ACC, v); }
    public void swiftTypeName(String v) { waitAndType(SWIFT_NAME, v); }

    public void swiftTypeAddr1(String v) { waitAndType(SWIFT_ADDR1, v); }
    public void swiftTypeAddr2(String v) { waitAndType(SWIFT_ADDR2, v); }
    public void swiftTypeBIC(String v) { waitAndType(SWIFT_BIC, v); }

    public void swiftTypeBankName(String v) { waitAndType(SWIFT_BANK_NAME, v); }
    public void swiftTypeBankAddr(String v) { waitAndType(SWIFT_BANK_ADDR, v); }
    public void swiftTypeCharge(String v) { waitAndType(SWIFT_CHARGE, v); }

    // ---------------------------------------------------------
    //  SCROLL SUPPORT
    // ---------------------------------------------------------
    public void scrollToEnd() {
        try {
            Thread.sleep(1000);
            ((AndroidDriver) driver).findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollToEnd(5)"
            ));
        } catch (Exception ignored) {}
    }

    public void scrollToText(String text) {
        try {
            ((AndroidDriver) driver).findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                            ".scrollIntoView(new UiSelector().textContains(\"" + text + "\"))"
            ));
        } catch (Exception ignored) {}
    }

    // ---------------------------------------------------------
    //  DATE SELECTION
    // ---------------------------------------------------------
    public void selectCalendarDate(LocalDate date) {
        waitAndClick("//android.widget.Button[contains(@text,'202')]");

        String day = String.valueOf(date.getDayOfMonth());

        waitAndClick("//android.view.View[contains(@content-desc,'" + day + "')]");
    }

    // ---------------------------------------------------------
    //  RECEIPT / CONFIRMATION
    // ---------------------------------------------------------
    public boolean isConfirmPageVisible() { return isVisible(TITLE_CONFIRM); }

    public void tapContinue() { waitAndClick(BTN_CONTINUE); }
    public void tapConfirm() { waitAndClick(BTN_CONFIRM); }

    public boolean isReceiptVisible() {
        return isVisible(BTN_RECEIPT) || isVisible(MSG_RECEIPT);
    }

    // Generate receipt
    public void generateReceipt() {
        waitAndClick(BTN_RECEIPT);
    }

    public boolean isLimitError() {
        return isVisible(ERROR_LIMIT);
    }
}
