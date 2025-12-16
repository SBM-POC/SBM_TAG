package pages;

import base.BasePage;

public class PayToMobilePage extends BasePage {

    // ---------------------------------------------------------
    // NAVIGATION
    // ---------------------------------------------------------
    public static final String TAB_TRANSFER       = "//android.widget.TabWidget/child::android.view.View[contains(@resource-id, 'tab-button')]/child::android.view.View[contains(@text, 'Transfer')]";
    public static final String TAB_PAYM = "//android.widget.TextView[contains(@text, 'Pay to Mobile')]/parent::android.view.View[contains(@resource-id, 'menu-option')]";
    // ---------------------------------------------------------
    // PAY TO MOBILE
    // ---------------------------------------------------------
    public static final String PAYM_TO_RECIPIENT = "//android.widget.TextView[contains(@text,'To Recipient')]/following-sibling::android.view.View";
    public static final String PAYM_SEARCH_USER = "//android.widget.Button[@text='Search User']";
    public static final String PAYM_FROM_ACCOUNT = "//android.widget.Button[contains(@text,'Available balance')]";
    public static final String PAYM_CURRENCY = "//android.widget.TextView[contains(@text,'Currency')]/following-sibling::android.view.View";
    public static final String PAYM_AMOUNT = "//android.view.View[contains(@resource-id,'send-money-page-amount')]/android.widget.EditText";
    public static final String PAYM_REMARKS = "//android.view.View[contains(@resource-id,'srcnarrative')]/android.widget.EditText";
    public static final String PAYM_CONTINUE = "//android.widget.Button[@text='Continue']";

    // ---------------------- ERROR MESSAGES ----------------------
    public static final String ERR_INVALID_MOBILE = "//android.widget.TextView[contains(@text,'Please enter a valid mobile number with country code')]";
    public static final String ERR_NOT_ELIGIBLE_MOBILE = "//android.widget.TextView[@text='This mobile number is not eligible for Pay to Mobile services.']";
    public static final String ERR_AMOUNT_REQUIRED = "//android.widget.TextView[contains(@text,'Amount is required')]";
    public static final String ERR_REMARK_REQUIRED = "//android.widget.TextView[contains(@text,'Remarks is required')]";
    public static final String ERR_AMOUNT_FORMAT = "//android.widget.TextView[contains(@text,'Please enter the amount in the correct format.')]";
    public static final String ERR_AMOUNT_BALANCE = "//android.widget.TextView[contains(@text,'Amount cannot be greater than available balance')]";
    public static final String ERR_DAILY_LIMIT = "//android.widget.TextView[contains(@text,'daily transfer limit')]";
    public static final String ERR_REMAINING_LIMIT = "//android.widget.TextView[contains(@text,'remaining limit')]";

    public void openPayToMobile() {
        waitAndClick(TAB_TRANSFER);
        waitAndClick(TAB_PAYM);
    }

    public void enterMobileNumber(String mobile) {
        waitAndType(PAYM_TO_RECIPIENT, mobile);
    }

    public void searchUser () {
        waitAndClick(PAYM_SEARCH_USER);
    }

    public void selectFromAccount(String account) {
        waitAndClick(PAYM_FROM_ACCOUNT);
        selectDropdownValue(account);
    }

    public void enterAmount(String amount) {
        waitAndType(PAYM_AMOUNT, amount);
    }

    public void enterRemarks(String remarks) {
        waitAndType(PAYM_REMARKS, remarks);
    }

    public void tapContinue() {
        waitAndClick(PAYM_CONTINUE);
    }


    // ERROR CHECKS

    public boolean isInvalidMobileErrorVisible() {
        return isVisible(ERR_INVALID_MOBILE);
    }

    public boolean isMobileNotEligibleErrorVisible() {
        return isVisible(ERR_NOT_ELIGIBLE_MOBILE);
    }

    public boolean isAmountRequiredErrorVisible() {
        return isVisible(ERR_AMOUNT_REQUIRED);
    }

    public boolean isRemarksRequiredErrorVisible() {
        return isVisible(ERR_REMARK_REQUIRED);
    }

    public boolean isAmountFormatErrorVisible() {
        return isVisible(ERR_AMOUNT_FORMAT);
    }

    public boolean isAmountGreaterThanBalanceVisible() {
        return isVisible(ERR_AMOUNT_BALANCE);
    }

    public boolean isDailyLimitErrorVisible() {
        return isVisible(ERR_DAILY_LIMIT) || isVisible(ERR_REMAINING_LIMIT);
    }

}
