package utils;

import base.BasePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class TransactionVerificationUtils extends BasePage {

    // -------------------- TRANSACTION HISTORY --------------------
    public void verifyTransactionHistory(Map<String, String> data) {

        String date = data.get("TransactionDate");
        String currency = data.get("CurrencySymbol");
        String amount = data.get("Amount");
        String remarks = data.get("Remarks");

        // Currency in transaction history
        String expectedAmount = currency + " -" + amount;

        try {
            // 1) Navigate history screen
            openTransactionHistory();

            // 2) Find all rows for that date
            String rowXPath =
                    "//android.widget.TextView[@text='" + date + "']" + "/parent::android.view.View" + "/parent::android.view.View";

            List<WebElement> rows = driver.findElements(AppiumBy.xpath(rowXPath));

            Assert.assertFalse(rows.isEmpty(), "No transaction rows found in history for date: " + date);

            boolean matchFound = false;

            for (WebElement row : rows) {
                String rowText = row.getText();

                // Verify MobilePay, Remarks and Amount
                if (rowText.contains("MobilePay")
                        && rowText.contains(expectedAmount)
                        && rowText.contains(remarks)) {

                    matchFound = true;
                    System.out.println("Pay to Mobile transaction found in history: " + rowText);
                    break;
                }
            }

            Assert.assertTrue(matchFound,
                    "No Pay to Mobile history entry matching [date=" + date + ", amount=" + expectedAmount + ", remarks=" + remarks + ", MobilePay]");

        } catch (Exception e) {
            throw new AssertionError("Transaction history verification failed: " + e.getMessage());
        }
    }

    // -------------------- ACTIVITY CALENDAR VALIDATION --------------------
    public void verifyActivityCalendar(Map<String, String> data) {

    }

    // -------------------- NAVIGATION HELPERS --------------------

    private void openTransactionHistory() {
        // TODO — Add xpath to navigate
        //Click on Home Page
        waitAndClick("//android.view.View[@text='Home']");

        //Select Account - Locator elements are broken

        //Expand the Transaction History List
        waitAndClick("(//android.view.View[@resource-id='history_filter_button'])[2]/android.widget.Button");
    }

}
