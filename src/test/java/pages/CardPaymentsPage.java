package pages;

import base.BasePage;
import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumBy;

public class CardPaymentsPage extends BasePage {
    // -------------------------
    // TRANSFER PAGE
    // -------------------------
    private static final String TAB_TRANSFER = "//android.view.View[contains(@text, 'Transfer')]";
    private static final String TRANSFER_CARD_PAYMENTS =
            "//android.widget.ListView/descendant::android.widget.TextView[contains(@text, 'Card Payments')]/parent::android.view.View";
    private static final String TRANSFER_CARD_PAYMENTS_RECHARGEPREPAID =
            "//android.view.View/descendant::android.widget.Button[contains(@text, 'Recharge My Prepaid Card')]";
    private static final String TRANSFER_CARD_PAYMENTS_PAYOWNCREDIT =
            "//android.view.View/descendant::android.widget.Button[contains(@text, 'Pay Own Credit Card')]";
    private static final String TRANSFER_CARD_PAYMENTS_PAYSBMCREDIT =
            "//android.view.View/descendant::android.widget.Button[contains(@text, 'Pay Other SBM Credit Card')]";

    // -----------------------------
    // RECHARGE MY PREPAID CARD (RMPC)
    // -----------------------------
    private static final String RMPC_FROMACCOUNT =
            "//android.widget.TextView[@text='From Account']/following-sibling::android.view.View//android.widget.Button";
    private static final String RMPC_TRANSFERTO =
            "//android.widget.TextView[@text='Pay/Transfer To']/following-sibling::android.view.View//android.widget.Button";
    private static final String RMPC_CURRENCY =
            "//android.widget.TextView[@text='Currency']/following-sibling::android.view.View//android.widget.EditText";
    private static final String RMPC_AMOUNT =
            "//android.widget.TextView[@text='Amount']/following-sibling::android.view.View//android.widget.EditText";
    private static final String RMPC_REMARKS =
            "//android.widget.TextView[@text='Remarks']/ancestor::android.view.View/following-sibling::android.view.View/child::android.widget.EditText";
    private static final String RMPC_CONTINUE =
            "//android.widget.Button[@text='Continue']";

    // -----------------------------
    // PAY OWN CREDIT CARD (POCC)
    // -----------------------------
    private static final String POCC_FROMACCOUNT =
            "//android.widget.TextView[@text='From Account']/following-sibling::android.view.View//android.widget.Button";
    private static final String POCC_TRANSFERTO =
            "//android.widget.TextView[@text='Pay/Transfer To']/following-sibling::android.view.View//android.widget.Button";
    private static final String POCC_CURRENCY =
            "//android.widget.TextView[@text='Currency']/following-sibling::android.view.View//android.widget.EditText";
    private static final String POCC_AMOUNT =
            "//android.widget.TextView[@text='Amount']/following-sibling::android.view.View//android.widget.EditText";
    private static final String POCC_REMARKS =
            "//android.widget.TextView[@text='Remarks']/ancestor::android.view.View/following-sibling::android.view.View/child::android.widget.EditText";
    private static final String POCC_CONTINUE =
            "//android.widget.Button[@text='Continue']";

    // -----------------------------
    // PAY OTHER SBM CREDIT CARD (POSCC)
    // -----------------------------

    // ---------------------------------------------------------
    //  CONFIRMATION / RECEIPT (same pattern as TransferPage)
    // ---------------------------------------------------------
    private static final String TITLE_CONFIRM =
            "//android.widget.TextView[contains(@text, 'Please confirm')]";
    private static final String BTN_CONFIRM =
            "//android.widget.Button[contains(@text, 'Confirm')]";

    private static final String BTN_RECEIPT =
            "//android.widget.Button[contains(@text,'Generate')]";
    private static final String MSG_RECEIPT =
            "//android.widget.TextView[contains(@text,'eBanking Ref') or contains(@text,'submitted')]";
    private static final String ERROR_LIMIT =
            "//android.widget.TextView[contains(@text,'Single Amount limit')]";

    // ---------------------------------------------------------
    //  LOG HELPER
    // ---------------------------------------------------------
    private ExtentTest log() {
        return BaseTest.getTest().get();
    }

    // ---------------------------------------------------------
    //  NAVIGATION
    // ---------------------------------------------------------
    public void openCardPaymentsModule() {
        try {
            waitAndClick(TAB_TRANSFER);
            System.out.println("Navigated to Transfer page");
            log().info("Navigated to Transfer page");
            Thread.sleep(1000);
            waitAndClick(TRANSFER_CARD_PAYMENTS);
            if (log() != null) {
                log().info("Opened Card Payments section");
            }
        } catch (Exception e) {
            System.out.println("Error navigating to Transfer page");
            log().fail("Error navigating to Transfer page");
        }
    }

    public void openRechargeMyPrepaidCard() {
        waitAndClick(TRANSFER_CARD_PAYMENTS_RECHARGEPREPAID);
        if (log() != null) {
            log().info("Opened 'Recharge My Prepaid Card' screen");
        }
    }

    public void openPayOwnCreditCard() {
        waitAndClick(TRANSFER_CARD_PAYMENTS_PAYOWNCREDIT);
    }

    public void openPayOtherSBMCreditCard() {
        waitAndClick(TRANSFER_CARD_PAYMENTS_PAYSBMCREDIT);
    }

    // ---------------------------------------------------------
    //  GENERIC DROPDOWN SELECTION (copied from TransferPage)
    // ---------------------------------------------------------
    public void selectDropdownValue(String value) {
        try {
            driver.findElement(AppiumBy.xpath("//*[contains(@text,'" + value + "')]")).click();
        } catch (Exception e) {
            // Fallback: scroll to find the text
            String scroll =
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                            ".scrollIntoView(new UiSelector().textContains(\"" + value + "\"));";

            driver.findElement(AppiumBy.androidUIAutomator(scroll)).click();
        }
    }

    // ---------------------------------------------------------
    //  RMPC FIELD ACTIONS
    // ---------------------------------------------------------
    public void rmpcOpenFromAccount() {
        waitAndClick(RMPC_FROMACCOUNT);
    }

    public void rmpcOpenTransferTo() {
        waitAndClick(RMPC_TRANSFERTO);
    }

    public void rmpcTypeCurrency(String value) {
        waitAndType(RMPC_CURRENCY, value);
    }

    public void rmpcTypeAmount(String value) {
        waitAndType(RMPC_AMOUNT, value);
    }

    public void rmpcTypeRemarks(String value) {
        waitAndType(RMPC_REMARKS, value);
    }

    public void rmpcTapContinue() {
        waitAndClick(RMPC_CONTINUE);
    }

    // ---------------------------------------------------------
    //  CONFIRM / RECEIPT HELPERS
    // ---------------------------------------------------------
    public boolean isConfirmPageVisible() {
        return isVisible(TITLE_CONFIRM);
    }

    public void tapConfirm() {
        waitAndClick(BTN_CONFIRM);
    }

    public boolean isReceiptVisible() {
        return isVisible(BTN_RECEIPT) || isVisible(MSG_RECEIPT);
    }

    public void generateReceipt() {
        waitAndClick(BTN_RECEIPT);
    }

    public boolean isLimitError() {
        return isVisible(ERROR_LIMIT);
    }
}
