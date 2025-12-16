package utils;

import base.BasePage;
import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.ReceiptPage;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class ReceiptValidator {

    private final ReceiptPage page = new ReceiptPage();
    private boolean hasFailure = false;

    // ---------------------------------------------------------
    //  PUBLIC VALIDATION ENTRY POINT
    // ---------------------------------------------------------
    public void validateReceipt(Map<String, String> expected) {
        ExtentTest test = BaseTest.getTest().get();
        // Wait for receipt's element to appear
        page.waitForVisibility("//android.widget.TextView[@text='From :']/following-sibling::android.widget.TextView[1]");
        // Auto-mask account numbers before comparing
        if (expected.containsKey("FromAccount"))
            expected.put("FromAccountMasked", maskAccount(expected.get("FromAccount")));

        if (expected.containsKey("ToAccount"))
            expected.put("ToAccountMasked", maskAccount(expected.get("ToAccount")));

        assertMatch("Date", page.getDate(), resolveToReceiptString(expected.get("Date")));
        assertMatch("Currency", page.getCurrency(), expected.get("Currency"));
        assertMatch("Amount", page.getAmount(), expected.get("Amount"));
        assertMatch("From Account", page.getFromAccount(), expected.get("FromAccountMasked"));
        assertMatch("To Account", page.getToAccount(), expected.get("ToAccountMasked"));
        assertMatch("Reference", page.getReference(), expected.get("Reference"));
        assertMatch("Transaction Type", page.getTransactionType(), expected.get("TransactionType"));
        assertMatch("Remarks", page.getRemarks(), expected.get("Remarks"));

        if (hasFailure) {
            test.fail("❌ Receipt Validation FAILED — one or more fields mismatched.");
            System.out.println("❌ Receipt Validation FAILED — one or more fields mismatched.");
            throw new AssertionError("Receipt validation failed");
        } else {
            test.pass("✔ Receipt validation completed successfully.");
            System.out.println("✔ Receipt validation completed successfully.");
        }

    }

    // ---------------------------------------------------------
    //  INTERNAL MATCHER
    // ---------------------------------------------------------
    private void assertMatch(String field, String actual, String expected) {
        ExtentTest test = BaseTest.getTest().get();

        if (expected == null || expected.isEmpty())
            return;
        try {
            Assert.assertTrue(actual.contains(expected));
            System.out.println("✔ " + field + " validated: " + actual);
            test.pass("✔ " + field + " validated: " + actual);
        } catch (AssertionError ae) {
            test.fail("❌ " + field + " mismatch! Expected: " + expected + " | Actual: " + actual);
            System.out.println("❌ " + field + " mismatch! Expected: " + expected + " | Actual: " + actual);
            hasFailure = true;
        }
    }

    private String maskAccount(String accountNumber) {

        if (accountNumber == null || accountNumber.isEmpty())
            return null;

        if (accountNumber.length() <= 8)
            return "*".repeat(accountNumber.length());

        String start = accountNumber.substring(0, 4);
        String end = accountNumber.substring(accountNumber.length() - 4);
        String masked = "*".repeat(accountNumber.length() - 8);

        return start + masked + end;
    }

    // ------------------------------
    // DATE FORMATTING AND PARSING
    // ------------------------------

    private static final DateTimeFormatter RECEIPT_FORMATTER = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

    /**
     * Parse Excel-like expressions such as:
     *  - "TODAY()"
     *  - "=TODAY()"
     *  - "TODAY()+2"
     *  - "=TODAY()+10"
     *
     * Returns the LocalDate, or null if expression not recognised.
     */
    public static LocalDate resolveToDate(String raw) {
        if (raw == null) return null;

        String normalized = raw.replace(" ", "").toUpperCase();

        if (normalized.equals("TODAY()") || normalized.equals("=TODAY()")) {
            return LocalDate.now();
        }

        if (normalized.startsWith("TODAY()+") || normalized.startsWith("=TODAY()+")) {
            String numPart = normalized
                    .replace("TODAY()+", "")
                    .replace("=TODAY()+", "");
            try {
                int days = Integer.parseInt(numPart);
                return LocalDate.now().plusDays(days);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        // Not a TODAY-expression → caller can decide what to do
        return null;
    }

    /**
     * Same as resolveToDate, but returns the string formatted
     * like the receipt: "4 December 2025".
     */
    public static String resolveToReceiptString(String raw) {
        LocalDate date = resolveToDate(raw);
        if (date == null) return null;
        return date.format(RECEIPT_FORMATTER);
    }
}
