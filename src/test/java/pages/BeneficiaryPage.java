package pages;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import utils.ConfirmationUtils;

public class BeneficiaryPage extends BasePage {

    private final ConfirmationUtils confirm;
    public BeneficiaryPage() {
        super();  // initializes driver + ElementActions
        confirm = new ConfirmationUtils(driver);  // pass driver
    }

    // =========================================================
    // COMMON NAVIGATION (My Beneficiaries)
    // =========================================================
    private static final String TAB_TRANSFER       = "//android.widget.TabWidget/child::android.view.View[contains(@resource-id, 'tab-button')]/child::android.view.View[contains(@text, 'Transfer')]";
    private static final String TAB_MY_BENEFICIARIES = "//android.widget.TextView[contains(@text, 'My Beneficiaries')]/parent::android.view.View[contains(@resource-id, 'menu-option')]";

    private static final String BTN_ADD_NEW_1      = "//android.view.View/child::android.widget.Button[@text= 'add outline Add new']";
    private static final String BTN_ADD_NEW_2      = "//android.widget.Button[contains(@text,'add outline Add new')]";

    private static final String POPUP_CLOSE_GENERIC = "//android.view.View/child::android.widget.Button[contains(@text, 'Close')]";

    // Header tabs
    private static final String TAB_OST    = "//android.view.View[contains(@resource-id,'BW_radio')]/descendant::android.view.View[contains(@text, 'Other SBM Transfer')]";
    private static final String TAB_OLB    = "//android.view.View[contains(@resource-id,'BW_radio')]/descendant::android.view.View[contains(@text, 'Other Local Bank')]";
    private static final String TAB_SWIFT  = "//android.view.View[contains(@resource-id, 'BW_radio')]/descendant::android.view.View[contains(@text, 'SWIFT')]";

    // =========================================================
    // SWIFT – ADD / EDIT / PAY / DELETE
    // (all XPaths from your earlier message)
    // =========================================================
    private static final String SWIFT_BUTTON                = "//android.view.View[contains(@text,\"SWIFT\")]";
    private static final String SWIFT_POPUP_CLOSE           = "//android.widget.Button[@text=\"Close\"]";
    private static final String SWIFT_PAYMENT_NAME          = "//android.widget.TextView[contains(@text, 'Payment Name')]/parent::android.view.View[contains(@resource-id, 'ion-input')]/following-sibling::android.view.View[contains(@resource-id, 'BW_text')]/descendant::android.widget.EditText[1]";
    private static final String SWIFT_FROM_ACCOUNT          = "//android.widget.Button[contains(@text,\"Available balance\")]";
    private static final String SWIFT_INCLUDE_AMOUNT_TOGGLE = "//android.widget.ToggleButton[(contains(@text,\"Include Amount\")) and (contains(@resource-id,\"ion\"))]";
    private static final String SWIFT_CURRENCY              = "//android.widget.TextView[contains(@text,\"Currency\")]/following-sibling::android.view.View/android.widget.EditText";
    private static final String SWIFT_REMITTANCE_AMOUNT     = "//android.widget.TextView[contains(@text,'Remittance Amount')]/parent::*/following-sibling::android.view.View[contains(@resource-id,'BW_text')]/descendant::android.widget.EditText";
    private static final String SWIFT_COUNTRY               = "//android.view.View[contains(@resource-id,\"BW_select\")]/android.widget.EditText";
    private static final String SWIFT_IBAN                  = "//android.widget.TextView[contains(@text,\"Beneficiary Account / IBAN\")]/following-sibling::android.view.View[contains(@resource-id,\"BW_text\")]/descendant::android.widget.EditText";
    private static final String SWIFT_BEN_NAME              = "//android.widget.TextView[contains(@text,'Beneficiary Name')]/parent::*/following-sibling::android.view.View[contains(@resource-id,'BW_text')]/descendant::android.widget.EditText";
    private static final String SWIFT_ADDR1                 = "//android.widget.TextView[contains(@text,'Beneficiary Address Line 1')]/parent::*/following-sibling::android.view.View[contains(@resource-id,'BW_text')]/descendant::android.widget.EditText";
    private static final String SWIFT_ADDR2                 = "//android.widget.TextView[contains(@text,'Beneficiary Address Line 2')]/parent::*/following-sibling::android.view.View[contains(@resource-id,'BW_text')]/descendant::android.widget.EditText";
    private static final String SWIFT_BANK_BIC              = "//android.widget.TextView[contains(@text,\"Beneficiary Bank BIC\")]/following-sibling::android.view.View[contains(@resource-id,\"BW_button\")]/android.widget.EditText";
    private static final String SWIFT_BANK_CLEARING         = "//android.widget.TextView[contains(@text,\"Beneficiary Bank Clearing Code\")]/following-sibling::android.view.View[contains(@resource-id,\"BW_text\")]/android.widget.EditText";
    private static final String SWIFT_BANK_NAME             = "//android.widget.TextView[contains(@text,\"Beneficiary Bank Name\")]/following-sibling::android.view.View[contains(@resource-id,\"BW_text\")]/android.widget.EditText";
    private static final String SWIFT_BANK_ADDR             = "//android.widget.TextView[contains(@text,\"Beneficiary Bank Address\")]/following-sibling::android.view.View[contains(@resource-id,\"BW_text\")]/android.widget.EditText";
    private static final String SWIFT_INTER_BIC             = "//android.widget.TextView[contains(@text,\"Intermediary Bank BIC\")]/following-sibling::android.view.View[contains(@resource-id,\"BW_button\")]/android.widget.EditText";
    private static final String SWIFT_INTER_NAME            = "//android.widget.TextView[contains(@text,\"Intermediary Bank Name\")]/following-sibling::android.view.View[contains(@resource-id,\"BW_text\")]/android.widget.EditText";
    public static final String CHECK_CODE_BTN1              = "(//android.widget.Button[contains(@text,\"Check code\")])[1]";
    public static final String CHECK_CODE_BTN2              = "(//android.widget.Button[contains(@text,\"Check code\")])[2]";
    private static final String SWIFT_CHARGE_OPTION         = "//android.widget.TextView[contains(@text,'Charge Option')]/parent::*/following-sibling::android.view.View[contains(@resource-id,'BW_select')]";
    private static final String SWIFT_CATEGORY              = "//android.widget.TextView[contains(@text, 'Add to Category')]/following-sibling::android.view.View/child::android.widget.EditText";
    private static final String SWIFT_REMARKS               = "//android.widget.TextView[contains(@text,'Remarks')]/parent::*/following-sibling::android.view.View[contains(@resource-id,'BW_text')]/descendant::android.widget.EditText";
    private static final String SWIFT_SAVE_BTN              = "//android.widget.Button[@text=\"Save\"]";

    // Common search / edit / pay / delete controls (re-used by all types)
    private static final String SEARCH_BEN          = "//android.widget.EditText";
    private static final String BTN_EDIT            = "//android.widget.Button[contains(@resource-id,\"edit\")]";
    private static final String BTN_PAY             = "//android.widget.Button[@resource-id=\"pay\"]";
    private static final String BTN_CONTINUE        = "//android.widget.Button[@text=\"Continue\"]";
    private static final String PAY_AMOUNT          = "//android.view.View[@resource-id=\"pay-bill-amount\"]/android.widget.EditText";
    private static final String PAY_REMARKS         = "//android.widget.TextView[contains(@text,'Remarks')]/parent::*/following-sibling::android.view.View[contains(@resource-id,'fundstransfer')]/descendant::android.widget.EditText";
    private static final String BTN_DELETE          = "//android.widget.Button[@resource-id=\"remove\"]";
    private static final String BTN_CONFIRM = "//android.widget.Button[@text=\"Confirm\"]";
    private static final String POPUP_SUCCESSFUL   = "//android.widget.TextView[contains(@text,\"Third party template updated successfully.\")]";
    private static final String POPUP_DELETE_SUCCESSFUL = "//android.widget.TextView[contains(@text,\"Third party template was deleted successfully.\")]";
    private static final String POPUP_SUCCESSFUL_CLOSE = "//android.widget.Button[@text=\"Close\"]";

    // =========================================================
    // OLB – ADD (your AddBen_OLB_* locators)
    // =========================================================
    private static final String OLB_PAYMENT_NAME   = "//android.widget.TextView[contains(@text, 'Payment Name')]/parent::android.view.View[contains(@resource-id, 'ion-input')]/following-sibling::android.view.View[contains(@resource-id, 'BW_text')]/descendant::android.widget.EditText[1]";
    private static final String OLB_FROM_ACCOUNT   = "//android.widget.TextView[contains(@text, 'From Account')]/parent::android.view.View/following-sibling::android.view.View/descendant::android.widget.Button[1]";
    private static final String OLB_INCLUDE_AMOUNT = "//android.widget.ToggleButton[contains(@text, 'Include Amount')]/descendant::android.widget.ToggleButton[contains(@resource-id, 'ion-tg')]";
    private static final String OLB_CURRENCY       = "//android.widget.TextView[contains(@text, 'Currency')]/parent::android.view.View/child::android.view.View/child::android.widget.EditText";
    private static final String OLB_AMOUNT         = "//android.widget.TextView[contains(@text, 'Amount')]/parent::android.view.View[contains(@resource-id, 'ion-input')]/following-sibling::android.view.View[contains(@resource-id, 'BW_text')]/child::android.widget.EditText";
    private static final String OLB_RECIPIENT      = "//android.widget.TextView[contains(@text, 'Recipient')]/parent::android.view.View[contains(@resource-id, 'ion-input')]/following-sibling::android.view.View[contains(@resource-id, 'BW_text')]/descendant::android.widget.EditText";
    private static final String OLB_BEN_NAME       = "//android.widget.TextView[contains(@text, 'Beneficiary Name')]/parent::android.view.View[contains(@resource-id, 'ion-input')]/following-sibling::android.view.View[contains(@resource-id, 'BW_text')]/descendant::android.widget.EditText";
    private static final String OLB_REMARKS        = "//android.widget.TextView[contains(@text, 'Remarks')]/parent::android.view.View[contains(@resource-id, 'ion-input')]/following-sibling::android.view.View[contains(@resource-id, 'BW_text')]/descendant::android.widget.EditText";
    private static final String OLB_BEN_BANK       = "//android.widget.TextView[contains(@text, 'Beneficiary Bank')]/parent::android.view.View[contains(@resource-id, 'ion-input')]/following-sibling::android.view.View[contains(@resource-id, 'BW_select')]/descendant::android.widget.EditText";
    private static final String OLB_CATEGORY       = "//android.widget.TextView[contains(@text, 'Add to Category')]/following-sibling::android.view.View/child::android.widget.EditText";
    private static final String OLB_SAVE           = "//android.view.View[contains(@resource-id, 'BW_input')]/child::android.widget.Button[contains(@text, 'Save')]";

    // ---------------------- POPUPS ---------------------- //
    private static final String OLB_POPUP_CLOSE = "//android.view.View/child::android.widget.Button[contains(@text,'Close')]";
    private static final String OLB_POPUP_INSTANT_LINE1 = "//android.widget.TextView[contains(@text,'Charges Free')]";
    private static final String OLB_POPUP_INSTANT_LINE2 = "//android.widget.TextView[contains(@text,'Transfer will be effected instantly')]";
    private static final String OLB_POPUP_NORMAL_LINE1 = "//android.widget.TextView[contains(@text,'Charges Free')]";
    private static final String OLB_POPUP_NORMAL_LINE2 = "//android.widget.TextView[contains(@text,'Cut off time: 15:00 hrs')]";
    private static final String OLB_POPUP_MACSS_LINE1 = "//android.widget.TextView[contains(@text,'Charges Rs 75')]";
    private static final String OLB_POPUP_MACSS_LINE2 = "//android.widget.TextView[contains(@text,'Cut off time: 15:00 hrs')]";

    // ---------------------- TRANSFER TYPE TABS ---------------------- //
    private static final String OLB_TAB_INSTANT = "//android.view.View[contains(@resource-id,'BW_radio')]" + "/child::android.view.View[contains(@text,'Instant')]";
    private static final String OLB_TAB_NORMAL = "//android.view.View[contains(@resource-id,'BW_radio')]" + "/child::android.view.View[contains(@text,'Normal')]";
    private static final String OLB_TAB_MACSS = "//android.view.View[contains(@resource-id,'BW_radio')]" + "/child::android.view.View[contains(@text,'MACSS')]";
    private static final String OLB_TAB_MACSS_ACCOUNT = "//android.view.View[contains(@resource-id,'BW_radio')]" + "/child::android.view.View[contains(@text,'Account')]";
    private static final String OLB_TAB_MACSS_IBAN = "//android.view.View[contains(@resource-id,'BW_radio')]" + "/child::android.view.View[contains(@text,'IBAN')]";

    // =========================================================
    // OST – ADD (your “other sbm beneficiaries” locators)
    // =========================================================
    private static final String OST_PAYMENT_NAME   = "//android.widget.EditText[contains(@hint,'Payment Name *')]";
    private static final String OST_FROM_ACCOUNT   = "(//android.widget.Button[@index='0'])[9]";
    private static final String OST_TOGGLE_INCLUDE = "(//android.widget.ToggleButton[contains(@text,'Include Amount')])[1]";
    private static final String OST_RECIPIENT_ACC  = "//android.widget.EditText[contains(@hint,'Recipient A/C No. *')]";
    private static final String OST_REMARKS        = "//android.widget.EditText[contains(@hint,'Remarks *')]";
    private static final String OST_CATEGORY       = "//android.widget.EditText[contains(@hint,'Add to Category')]";
    private static final String OST_CURRENCY       = "//android.widget.EditText[contains(@hint,'Currency')]";
    private static final String OST_AMOUNT         = "//android.widget.EditText[contains(@hint,'Amount *')]";
    private static final String OST_SAVE           = "//android.widget.Button[contains(@text,'Save')]";

    // =========================================================
    // BASIC HELPERS
    // =========================================================

    public void openMyBeneficiaries() {
        waitAndClick(TAB_TRANSFER);
        waitAndClick(TAB_MY_BENEFICIARIES);
    }

    public void tapAddNew() {
        if (isVisible(BTN_ADD_NEW_1)) {
            waitAndClick(BTN_ADD_NEW_1);
        } else {
            waitAndClick(BTN_ADD_NEW_2);
        }
    }

    public void closePopupIfVisible() {
        if (isVisible(POPUP_CLOSE_GENERIC)) {
            waitAndClick(POPUP_CLOSE_GENERIC);
        }
        if (isVisible(SWIFT_POPUP_CLOSE)) {
            waitAndClick(SWIFT_POPUP_CLOSE);
        }
    }

    public void selectSwiftTab() { waitAndClick(TAB_SWIFT); }

    public void selectOlbTab() { waitAndClick(TAB_OLB); }

    public void selectOstTab() { waitAndClick(TAB_OST); }

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


//        try {
//            driver.findElement(AppiumBy.xpath("//*[contains(@text,'" + value + "')]")).click();
//        } catch (Exception e) {
//            String scroll =
//                    "new UiScrollable(new UiSelector().scrollable(true))" +
//                            ".scrollIntoView(new UiSelector().textContains(\"" + value + "\"));";
//            driver.findElement(AppiumBy.androidUIAutomator(scroll)).click();
//        }
//   }

    // ---------------------------------------------------------
    // Search common for all types
    // ---------------------------------------------------------
    public String searchBeneficiary(String paymentName) {
        waitAndClick(SEARCH_BEN);
        waitAndType(SEARCH_BEN, paymentName);

        String dynamic = String.format("//android.widget.TextView[@text='%s']", paymentName);
        waitForVisibility(dynamic);

        return dynamic;
    }

    // =========================================================
    // SWIFT FLOWS
    // =========================================================
    public void addSwift(Map<String, String> data) {

        openMyBeneficiaries();
        tapAddNew();
        selectSwiftTab();

        // SWIFT popup validation
        if (isVisible(SWIFT_POPUP_CLOSE)) {
            String popupTextXpath = "//android.widget.TextView[contains(@text,'Cut off')]";
            if (isVisible(popupTextXpath)) {
                String popupText = getText(popupTextXpath);
                if (!popupText.contains("15:00")) {
                    throw new AssertionError("Unexpected SWIFT popup text: " + popupText);
                }
            }
            waitAndClick(SWIFT_POPUP_CLOSE);
        }

        // FILL THE FIELDS
        waitAndType(SWIFT_PAYMENT_NAME, data.get("PaymentName"));

        waitAndClick(SWIFT_FROM_ACCOUNT);
        selectDropdownValue(data.get("FromAccount"));

        if ("TRUE".equalsIgnoreCase(data.getOrDefault("IncludeAmount", ""))) {
            waitAndClick(SWIFT_INCLUDE_AMOUNT_TOGGLE);

            waitAndClick(SWIFT_CURRENCY);
            selectDropdownValue(data.get("Currency"));
            waitAndType(SWIFT_REMITTANCE_AMOUNT, data.get("Amount"));
        }

        waitAndClick(SWIFT_COUNTRY);
        selectDropdownValue(data.get("Country"));

        String acc = data.get("BeneficiaryAccount");
        String iban = data.get("BeneficiaryIBAN");

        if (acc != null && !acc.isEmpty()) {
            waitAndType(SWIFT_IBAN, acc);
        } else if (iban != null && !iban.isEmpty()) {
            waitAndType(SWIFT_IBAN, iban);
        } else {
            throw new AssertionError("Both Account & IBAN are empty");
        }

        waitAndType(SWIFT_BEN_NAME,       data.get("BeneficiaryName"));

        waitAndType(SWIFT_ADDR1,          data.get("BenAddress1"));

        scrollToText("Beneficiary Address Line 2");
        waitAndType(SWIFT_ADDR2,          data.get("BenAddress2"));

        scrollToText("Beneficiary Bank BIC");
        waitAndType(SWIFT_BANK_BIC,       data.get("BenBankBIC"));
        waitAndClick(CHECK_CODE_BTN1);

        //VERIFY THAT BENEFICIARY BANK NAME AND ADDRESS ARE AUTO-FILLED
        String bankName = data.get("BenBankName");
        String bankAddr = data.get("BenBankAddress");

        if (bankName == null || bankName.isEmpty() ||
                bankAddr == null || bankAddr.isEmpty()) {
            throw new AssertionError("Bank Name/Address did not auto-fill.");
        }

        waitAndType(SWIFT_BANK_CLEARING,  data.get("BenBankClearing"));

        // INTERMEDIARY BANK BIC (if exists)
        scrollToText("Intermediary Bank BIC");
        if (data.get("InterBankBIC") != null && !data.get("InterBankBIC").isEmpty()) {
            waitAndType(SWIFT_INTER_BIC, data.get("InterBankBIC"));
            waitAndClick(CHECK_CODE_BTN2);

            String interName = data.get("InterBankName");
            if (interName == null || interName.isEmpty()) {
                throw new AssertionError("Intermediary Bank Name not auto-filled");
            }
        }

        scrollToEnd();
        //Charge Option
        waitAndClick(SWIFT_CHARGE_OPTION);
        selectDropdownValue(data.get("ChargeOption"));

        //Remarks
        waitAndType(SWIFT_REMARKS, data.get("Remarks"));

        //Add TO Category
        waitAndClick(SWIFT_CATEGORY);
        selectDropdownValue(data.get("AddToCategory"));

        waitAndClick(SWIFT_SAVE_BTN);

        // Confirmation popup
        waitForVisibility(POPUP_SUCCESSFUL);

        // Close popup
        waitAndClick(SWIFT_POPUP_CLOSE);

        //VERIFY IF BENEFICIARY ADDED SUCCESSFULLY
        searchBeneficiary(data.get("PaymentName"));
    }

    public void editSwift(Map<String, String> data) {

        openMyBeneficiaries();

        String benSearched = searchBeneficiary(data.get("PaymentName"));
        waitAndClick(benSearched);

        waitAndClick(BTN_EDIT);

        // Close popup if appears
        if (isVisible(SWIFT_POPUP_CLOSE)) {
            waitAndClick(SWIFT_POPUP_CLOSE);
        }

        // Payment Name
        waitAndType(SWIFT_PAYMENT_NAME, data.get("PaymentName"));

        // Edit Currency & Amount only if visible
        if (isVisible(SWIFT_CURRENCY)) {
            waitAndClick(SWIFT_CURRENCY);
            selectDropdownValue(data.get("Currency"));
        }
        if (isVisible(SWIFT_REMITTANCE_AMOUNT)) {
            waitAndType(SWIFT_REMITTANCE_AMOUNT, data.get("Amount"));
        }

        // Verify Beneficiary Account/IBAN cannot be edited

        // Edit Beneficiary Name
        waitAndType(SWIFT_BEN_NAME, data.get("BeneficiaryName"));

        // Edit Address
        waitAndType(SWIFT_ADDR1, data.get("BenAddress1"));

        scrollToText("Beneficiary Address Line 2");
        waitAndType(SWIFT_ADDR2, data.get("BenAddress2"));

        // BIC Check Code
        scrollToText("Beneficiary Bank BIC");
        waitAndType(SWIFT_BANK_BIC, data.get("BenBankBIC"));
        waitAndClick("//android.widget.Button[contains(@text,'Check Code')]");

        // Auto-fill validation
        if (data.get("BenBankName").isEmpty() || data.get("BenBankAddress").isEmpty()) {
            throw new AssertionError("Beneficiary Bank auto-fill missing");
        }

        // Clearing code
        waitAndType(SWIFT_BANK_CLEARING, data.get("BenBankClearing"));

        // Intermediary BIC
        scrollToText("Intermediary Bank BIC");
        if (data.get("InterBankBIC") != null && !data.get("InterBankBIC").isEmpty()) {
            waitAndType(SWIFT_INTER_BIC, data.get("InterBankBIC"));
            waitAndClick("//android.widget.Button[contains(@text,'Check Code')]");
            if (data.get("InterBankName").isEmpty()) {
                throw new AssertionError("Intermediary Bank Name missing");
            }
        }

        scrollToEnd();
        // Charge Option
        waitAndClick(SWIFT_CHARGE_OPTION);
        selectDropdownValue(data.get("ChargeOption"));

        // Category
        waitAndClick(SWIFT_CATEGORY);
        selectDropdownValue(data.get("AddToCategory"));

        // Save
        waitAndClick(SWIFT_SAVE_BTN);

        // Confirmation popup
        waitForVisibility(POPUP_SUCCESSFUL);

        // Close popup
        waitAndClick(SWIFT_POPUP_CLOSE);
    }

    public void paySwift(Map<String, String> data) {

        openMyBeneficiaries();

        String benSearched = searchBeneficiary(data.get("PaymentName"));
        waitAndClick(benSearched);

        waitAndClick(BTN_PAY);

        // Close popup
        if (isVisible(SWIFT_POPUP_CLOSE)) {
            waitAndClick(SWIFT_POPUP_CLOSE);
        }

        if (isVisible("//android.widget.TextView[@text='Amount is required']")) {
            // Currency
            if (data.get("Currency") != null) {
                waitAndClick(SWIFT_CURRENCY);
                selectDropdownValue(data.get("Currency"));
            }
            // Amount
            waitAndType(PAY_AMOUNT, data.get("Amount"));
        }

        // Charge Option
        waitAndClick(SWIFT_CHARGE_OPTION);
        selectDropdownValue(data.get("ChargeOption"));

        // Calendar Logic
        selectDateFromCalendar(data.get("TransactionDate"));

        // Remarks
        waitAndType(PAY_REMARKS, data.get("Remarks"));

        // Continue
        waitAndClick(BTN_CONTINUE);

        // Confirmation screen
        confirm.Confirmation_Screen_SWIFT(data);

        // Success screen
        confirm.Successful_Screen_SWIFT(data);
    }

    public void deleteSwift(Map<String, String> data) {

        openMyBeneficiaries();

        String benSearched = searchBeneficiary(data.get("PaymentName"));
        waitAndClick(benSearched);

        waitAndClick(BTN_DELETE);

        // Confirm page
        waitForVisibility("//android.widget.TextView[contains(@text,'Please confirm')]");

        waitAndClick(BTN_CONFIRM);

        // Success popup
        waitForVisibility(POPUP_DELETE_SUCCESSFUL);

        waitAndClick(POPUP_SUCCESSFUL_CLOSE);
    }

    // =========================================================
    // OLB FLOWS
    // =========================================================
    public void addOlb(Map<String, String> data) {

        openMyBeneficiaries();
        tapAddNew();
        selectOlbTab();
        closePopupIfVisible();

        waitAndType(OLB_PAYMENT_NAME, data.get("PaymentName"));

        waitAndClick(OLB_FROM_ACCOUNT);
        selectDropdownValue(data.get("FromAccount"));

        // Transfer Type (Instant / Normal / MACSS)
        // ---------------------------------------------------------
        String transferType = data.get("TransferType");

        switch (transferType.toUpperCase()) {

            case "INSTANT":
                waitAndClick(OLB_TAB_INSTANT);
                validateOlbPopup(
                        OLB_POPUP_INSTANT_LINE1,
                        OLB_POPUP_INSTANT_LINE2,
                        "Instant"
                );
                break;

            case "NORMAL":
                waitAndClick(OLB_TAB_NORMAL);
                validateOlbPopup(
                        OLB_POPUP_NORMAL_LINE1,
                        OLB_POPUP_NORMAL_LINE2,
                        "Normal"
                );
                break;

            case "MACSS":
                waitAndClick(OLB_TAB_MACSS);
                validateOlbPopup(
                        OLB_POPUP_MACSS_LINE1,
                        OLB_POPUP_MACSS_LINE2,
                        "MACSS"
                );
                break;

            default:
                throw new AssertionError("❌ Invalid TransferType provided: " + transferType);
        }

        // MACSS inner logic → Account / IBAN
        if ("MACSS".equalsIgnoreCase(transferType)) {

            String macssType = data.get("AccountType");

            if ("ACCOUNT".equalsIgnoreCase(macssType)) {

                waitAndClick(OLB_TAB_MACSS_ACCOUNT);
                waitAndType(OLB_RECIPIENT, data.get("RecipientAccountNo"));

            } else if ("IBAN".equalsIgnoreCase(macssType)) {

                waitAndClick(OLB_TAB_MACSS_IBAN);
                waitAndType(OLB_RECIPIENT, data.get("RecipientIBAN"));

            } else {
                // For Instant & Normal → Recipient always AccountNumber
                waitAndType(OLB_RECIPIENT, data.get("RecipientAccountNo"));
            }
        }

        if ("TRUE".equalsIgnoreCase(data.getOrDefault("IncludeAmount", ""))) {
            waitAndClick(OLB_INCLUDE_AMOUNT);
            waitAndType(OLB_CURRENCY, data.get("Currency"));
            waitAndType(OLB_AMOUNT,   data.get("Amount"));
        }

        waitAndType(OLB_BEN_NAME,  data.get("Beneficiary_Name"));

        scrollToEnd();

        waitAndType(OLB_REMARKS,   data.get("Remarks"));

        waitAndClick(OLB_BEN_BANK);
        selectDropdownValue(data.get("Beneficiary_Bank"));

        waitAndClick(OLB_CATEGORY);
        selectDropdownValue(data.get("Category"));

        waitAndClick(OLB_SAVE);

        // Success popup
        waitForVisibility(POPUP_SUCCESSFUL);

        // Close popup
        waitAndClick(POPUP_SUCCESSFUL_CLOSE);
    }

    public void editOlb(Map<String, String> data) {

        openMyBeneficiaries();
        searchBeneficiary(data.get("PaymentName"));
        waitAndClick(BTN_EDIT);

        closePopupIfVisible();

        waitAndType(OLB_PAYMENT_NAME, data.get("PaymentName"));

        waitAndClick(OLB_FROM_ACCOUNT);
        selectDropdownValue(data.get("FromAccount"));

        // Edit Transfer Type (same logic as ADD)
        String transferType = data.get("TransferType");

        switch (transferType.toUpperCase()) {

            case "INSTANT":
                waitAndClick(OLB_TAB_INSTANT);
                validateOlbPopup(
                        OLB_POPUP_INSTANT_LINE1,
                        OLB_POPUP_INSTANT_LINE2,
                        "Instant"
                );
                break;

            case "NORMAL":
                waitAndClick(OLB_TAB_NORMAL);
                validateOlbPopup(
                        OLB_POPUP_NORMAL_LINE1,
                        OLB_POPUP_NORMAL_LINE2,
                        "Normal"
                );
                break;

            case "MACSS":
                waitAndClick(OLB_TAB_MACSS);
                validateOlbPopup(
                        OLB_POPUP_MACSS_LINE1,
                        OLB_POPUP_MACSS_LINE2,
                        "MACSS"
                );
                break;

            default:
                throw new AssertionError("❌ Invalid TransferType provided: " + transferType);
        }

        // Edit MACSS → Account / IBAN
        if ("MACSS".equalsIgnoreCase(transferType)) {

            String macssType = data.get("AccountType");

            if ("ACCOUNT".equalsIgnoreCase(macssType)) {

                waitAndClick(OLB_TAB_MACSS_ACCOUNT);
                waitAndType(OLB_RECIPIENT, data.get("RecipientAccountNo"));

            } else if ("IBAN".equalsIgnoreCase(macssType)) {

                waitAndClick(OLB_TAB_MACSS_IBAN);
                waitAndType(OLB_RECIPIENT, data.get("RecipientIBAN"));

            } else {
                // For Instant & Normal → Recipient always AccountNumber
                waitAndType(OLB_RECIPIENT, data.get("RecipientAccountNo"));
            }
        }
        waitAndType(OLB_BEN_NAME,  data.get("Beneficiary_Name"));

        waitAndType(OLB_REMARKS,      data.get("Remarks"));

        scrollToEnd();

        waitAndClick(OLB_BEN_BANK);
        selectDropdownValue(data.get("Beneficiary_Bank"));

        waitAndClick(OLB_CATEGORY);
        selectDropdownValue(data.get("Category"));

        waitAndClick(OLB_SAVE);

        // Success popup
        waitForVisibility(POPUP_SUCCESSFUL);

        // Close popup
        waitAndClick(POPUP_SUCCESSFUL_CLOSE);
    }

    public void payOlb(Map<String, String> data) {

        openMyBeneficiaries();
        searchBeneficiary(data.get("PaymentName"));
        waitAndClick(BTN_PAY);

        if (isVisible("//android.widget.TextView[@text='Amount is required']")) {
            // Currency
            if (data.get("Currency") != null) {
                waitAndClick(OLB_CURRENCY);
                selectDropdownValue(data.get("Currency"));
            }
            // Amount
            waitAndType(PAY_AMOUNT, data.get("Amount"));
        }

        waitAndType(PAY_REMARKS, data.get("Remarks"));

        waitAndClick(BTN_CONTINUE);

        // Confirmation screen
        confirm.Confirmation_Screen_OLB(data);

        // Success screen
        confirm.Successful_Screen_OLB(data);

        // Success popup
        waitForVisibility(POPUP_SUCCESSFUL);

        // Close popup
        waitAndClick(POPUP_SUCCESSFUL_CLOSE);

    }

    public void deleteOlb(Map<String, String> data) {

        openMyBeneficiaries();

        String benSearched = searchBeneficiary(data.get("PaymentName"));
        waitAndClick(benSearched);

        waitAndClick(BTN_DELETE);

        // Confirm page
        waitForVisibility("//android.widget.TextView[contains(@text,'Please confirm')]");

        waitAndClick(BTN_CONFIRM);

        // Success popup
        waitForVisibility(POPUP_DELETE_SUCCESSFUL);

        waitAndClick(POPUP_SUCCESSFUL_CLOSE);
    }

    // =========================================================
    // OST FLOWS
    // =========================================================
    public void addOst(Map<String, String> data) {

        openMyBeneficiaries();
        tapAddNew();
        selectOstTab();
        closePopupIfVisible();

        waitAndType(OST_PAYMENT_NAME, data.get("PaymentName"));

        waitAndClick(OST_FROM_ACCOUNT);
        selectDropdownValue(data.get("FromAccount"));

        if ("TRUE".equalsIgnoreCase(data.getOrDefault("IncludeAmount", ""))) {
            waitAndClick(OST_TOGGLE_INCLUDE);
            waitAndType(OST_CURRENCY, data.get("Currency"));
            waitAndType(OST_AMOUNT,   data.get("Amount"));
        }

        waitAndType(OST_RECIPIENT_ACC, data.get("Beneficiary_Account"));
        waitAndType(OST_REMARKS,       data.get("Remarks"));
        waitAndType(OST_CATEGORY,      data.get("Category"));

        waitAndClick(OST_SAVE);

        // Confirmation popup
        waitForVisibility(POPUP_SUCCESSFUL);

        // Close popup
        waitAndClick(POPUP_SUCCESSFUL_CLOSE);
    }

    public void editOst(Map<String, String> data) {

        openMyBeneficiaries();
        searchBeneficiary(data.get("PaymentName"));
        waitAndClick(BTN_EDIT);

        // Edit Payment Name
        waitAndType(OST_PAYMENT_NAME, data.get("PaymentName"));

        //Edit From Account
        waitAndClick(OST_FROM_ACCOUNT);
        selectDropdownValue(data.get("FromAccount"));

        // Edit Currency & Amount only if visible
        if (isVisible(OST_CURRENCY)) {
            waitAndClick(OST_CURRENCY);
            selectDropdownValue(data.get("Currency"));
        }
        if (isVisible(OST_AMOUNT)) {
            waitAndType(OST_AMOUNT, data.get("Amount"));
        }

        // Verify Recipient Account cannot be edited

        // Edit Remarks
        waitAndType(OST_REMARKS,      data.get("Remarks"));

        // Edit Category
        waitAndClick(OST_CATEGORY);
        selectDropdownValue(data.get("AddToCategory"));

        waitAndClick(OST_SAVE);

        // Confirmation popup
        waitForVisibility(POPUP_SUCCESSFUL);

        // Close popup
        waitAndClick(POPUP_SUCCESSFUL_CLOSE);
    }

    public void payOst(Map<String, String> data) {

        openMyBeneficiaries();
        searchBeneficiary(data.get("PaymentName"));
        waitAndClick(BTN_PAY);

        if (isVisible("//android.widget.TextView[@text='Amount is required']")) {
            // Currency
            if (data.get("Currency") != null) {
                waitAndClick(OST_CURRENCY);
                selectDropdownValue(data.get("Currency"));
            }
            // Amount
            waitAndType(PAY_AMOUNT, data.get("Amount"));
        }

        // Transaction Date
        selectDateFromCalendar(data.get("TransactionDate"));

        // Remarks
        waitAndType(PAY_REMARKS, data.get("Remarks"));

        waitAndClick(BTN_CONTINUE);

        // Confirmation screen
        confirm.Confirmation_Screen_OtherSBM(data);

        // Success screen
        confirm.Successful_Screen_OtherSBM(data);

        // Confirmation popup
        waitForVisibility(POPUP_SUCCESSFUL);

        // Close popup
        waitAndClick(POPUP_SUCCESSFUL_CLOSE);
    }

    public void deleteOst(Map<String, String> data) {

        openMyBeneficiaries();

        String benSearched = searchBeneficiary(data.get("PaymentName"));
        waitAndClick(benSearched);

        waitAndClick(BTN_DELETE);

        // Confirm page
        waitForVisibility("//android.widget.TextView[contains(@text,'Please confirm')]");

        waitAndClick(BTN_CONFIRM);

        // Success popup
        waitForVisibility(POPUP_DELETE_SUCCESSFUL);

        waitAndClick(POPUP_SUCCESSFUL_CLOSE);
    }

    public void selectDateFromCalendar(String excelDate) {

        // Parse date (adjust format if needed)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate targetDate = LocalDate.parse(excelDate, formatter);

        int targetDay = targetDate.getDayOfMonth();
        int targetMonth = targetDate.getMonthValue();
        int targetYear = targetDate.getYear();

        // 1. Open the calendar
        driver.findElement(By.xpath(
                "//android.widget.TextView[contains(@text,'Transaction Date')]/parent::android.view.View//android.view.View[contains(@resource-id,'BW_input')]"
        )).click();

        // 2. Open Month/Year Selector Popup
        driver.findElement(By.xpath("//android.view.View[contains(@resource-id,\"current-date\")]")).click();

        // 3. Select Year
        while (true) {
            String displayedYearText = driver.findElement(By.xpath(
                    "//android.view.View[@resource-id='prev-year']/parent::android.view.View/following-sibling::android.widget.TextView[1]"
            )).getText();

            int displayedYear = Integer.parseInt(displayedYearText);

            if (displayedYear == targetYear) {
                break;
            }

            if (displayedYear < targetYear) {
                driver.findElement(By.xpath("//android.view.View[contains(@resource-id,\"next-year\")]")).click();
            } else {
                driver.findElement(By.xpath("//android.view.View[contains(@resource-id,\"prev-year\")]")).click();
            }
        }

        // 4. Select Month
        String targetMonthShort =
                targetDate.getMonth().name().substring(0, 1).toUpperCase() +
                        targetDate.getMonth().name().substring(1, 3).toLowerCase();

        String monthButtonXpath = "//android.widget.TextView[@text='" + targetMonthShort + "']";

        driver.findElement(By.xpath(monthButtonXpath)).click();

        // 5. Select Day (universal selector)
        String dayXpath =
                "//android.view.View[@content-desc='" + targetDay + "'] | " +
                        "//android.widget.TextView[@text='" + targetDay + "']/parent::android.view.View";

        driver.findElement(By.xpath(dayXpath)).click();
        System.out.println("Day selected: " + targetDay);
    }

    private void validateOlbPopup(String line1, String line2, String popupName) {

        if (!isVisible(line1)) {
            throw new AssertionError("❌ " + popupName + " popup missing line 1: " + line1);
        }
        if (!isVisible(line2)) {
            throw new AssertionError("❌ " + popupName + " popup missing line 2: " + line2);
        }

        waitAndClick(OLB_POPUP_CLOSE);
    }
}
