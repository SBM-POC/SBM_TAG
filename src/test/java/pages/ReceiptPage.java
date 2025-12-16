package pages;

import base.BasePage;

public class ReceiptPage extends BasePage {

    // ---------------------------------------------------------
    //  LOCATORS — Android (Based on inspected hierarchy)
    // ---------------------------------------------------------
    private static final String RECEIPT_HEADERS =
            "//android.view.View/descendant::android.widget.TextView[contains(@text, 'Remittance') or contains(@text, 'Transaction Successful')]";

    private static final String DATE_VALUE =
            "//android.widget.TextView[@text='Date :']/following-sibling::android.widget.TextView[2]";

    private static final String CURRENCY_VALUE =
            "//android.widget.TextView[@text='Transaction Currency :']/following-sibling::android.widget.TextView[1]";

    private static final String AMOUNT_VALUE =
            "//android.widget.TextView[@text='Transaction amount :']/following-sibling::android.widget.TextView[2]";

    private static final String FROM_ACCOUNT_VALUE =
            "//android.widget.TextView[@text='From :']/following-sibling::android.widget.TextView[1]";

    private static final String TO_ACCOUNT_VALUE =
            "//android.widget.TextView[@text='To :']/following-sibling::android.widget.TextView[1]";

    private static final String REFERENCE_VALUE =
            "//android.widget.TextView[@text='eBanking Reference :']/following-sibling::android.widget.TextView[2]";

    private static final String TYPE_VALUE =
            "//android.widget.TextView[@text='Transaction Type :']/following-sibling::android.widget.TextView[1]";

    private static final String REMARKS_VALUE =
            "//android.widget.TextView[@text='Remarks :']/following-sibling::android.widget.TextView[2]";

    // ---------------------------------------------------------
    //  GETTERS (Direct data extractors)
    // ---------------------------------------------------------

    public String getDate() {
        return getText(DATE_VALUE);
    }

    public String getCurrency() {
        return getText(CURRENCY_VALUE);
    }

    public String getAmount() {
        return getText(AMOUNT_VALUE);
    }

    public String getFromAccount() {
        return getText(FROM_ACCOUNT_VALUE);
    }

    public String getToAccount() {
        return getText(TO_ACCOUNT_VALUE);
    }

    public String getReference() {
        return getText(REFERENCE_VALUE);
    }

    public String getTransactionType() {
        return getText(TYPE_VALUE);
    }

    public String getRemarks() {
        return getText(REMARKS_VALUE);
    }
}
