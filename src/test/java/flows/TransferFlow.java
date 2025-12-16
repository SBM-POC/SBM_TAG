package flows;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import pages.LogOutPage;
import pages.TransferPage;
import utils.ErrorHandling;
import utils.ReceiptValidator;
import utils.ReportManager;
import utils.TransferHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

public class TransferFlow {

    private final TransferPage page = new TransferPage();
    private final LogOutPage logout = new LogOutPage();
    private final ErrorHandling errorHandling = new ErrorHandling(BaseTest.getDriverStatic());
    private final ReceiptValidator receiptValidator = new ReceiptValidator();
    private ExtentTest log() {
        return BaseTest.getTest().get();  // may be null if no step set yet
    }
    // ========================================================================
    // OWN ACCOUNT TRANSFER
    // ========================================================================
    public void transferOwnAccount(Map<String, String> data) {
        ExtentTest test = log();
        test.info("🔁 STARTING OWN ACCOUNT TRANSFER");

        page.openTransferModule();
        page.openOwnAccount();
        page.closePopupIfVisible();

        // FROM
        page.oatSelectFrom();
        page.selectDropdownValue(data.get("FromAccount"));

        // TO
        page.oatSelectTo();
        page.selectDropdownValue(data.get("ToAccount"));

        // Fields
        page.oatTypeCurrency(data.get("Currency"));
        page.oatTypeAmount(data.get("Amount"));
        page.oatTypeRemarks(data.get("Remarks"));

        selectDate(data);

        page.oatTapContinue();

        validateAndConfirm(data.get("ToAccount"));
    }

    // ========================================================================
    // OTHER SBM ACCOUNT
    // ========================================================================
    public void transferSBM(Map<String, String> data) {
        ExtentTest test = log();
        test.info("🏦 STARTING OTHER SBM TRANSFER");

        page.openTransferModule();
        page.openSBM();
        page.closePopupIfVisible();

        page.openGenericFromAccount();
        page.selectDropdownValue(data.get("SenderAccount"));

        page.typeBeneficiaryAccount(data.get("RecipientAccount"));
        page.sbmAmount(data.get("Amount"));
        page.sbmCurrency(data.get("Currency"));
        page.sbmRemark(data.get("Remarks"));


        selectDate(data);

        page.tapContinue();
        validateAndConfirm(data.get("RecipientAccount"));
    }

    // ========================================================================
    // LOCAL BANK TRANSFER
    // ========================================================================
    public void transferLocalBank(Map<String, String> data) {
        log();
        // Navigating to OLB page
        // Creating test step for navigating to transfer page
        ReportManager.step("Navigating to Transfer page");
        page.openTransferModule();
        page.openLocalBank();

        // Creating test step for navigating to OLB page
        ReportManager.step("OLB Form page");
        page.closePopupIfVisible();

        // Handle Transfer Type
        TransferHelper.handleLocalBankTransferType(data);

        // Open account
        page.openGenericFromAccount();
        page.selectDropdownValue(data.get("FromAccount"));

        page.typeBeneficiaryAccount(data.get("BeneficiaryAccount"));
        page.typeBeneficiaryName(data.get("BeneficiaryName"));

        // Choose beneficiary bank
        page.openGenericBeneficiaryBank();
        page.selectDropdownValue(data.get("BeneficiaryBank"));

        page.scrollToEnd();

        page.typeCurrency(data.get("Currency"));
        page.typeAmount(data.get("Amount"));
        page.typeRemark(data.get("Remarks"));

        selectDate(data);

        try {
            page.tapContinue();
            if (errorHandling.handleErrorMessages()) {
                System.out.println("Errors found on screen after Continue. Skipping this row.");
                log().fail("Errors found on screen after Continue. Skipping this row.");
                errorHandling.navigateBackToSafeScreen(5);
                return;
            }

            // Creating test step for confirmation and receipt pages
            ReportManager.step("Confirmation pages");

            validateAndConfirm(data.get("BeneficiaryAccount"));
            if (errorHandling.handleErrorMessages()) {
                System.out.println("Errors found on screen after Confirm. Skipping this row.");
                log().fail("Errors found on screen after Confirm. Skipping this row.");
                errorHandling.navigateBackToSafeScreen(5);
                return;
            }

            // Creating test step for receipt generated
            ReportManager.step("Validating Receipt");
            page.generateReceipt();
            if (errorHandling.handleErrorMessages()) {
                System.out.println("Errors found on screen after Confirm. Skipping this row.");
                log().fail("Errors found on screen after Confirm. Skipping this row.");
                errorHandling.navigateBackToSafeScreen(5);
                return;
            }
            receiptValidator.validateReceipt(data);
        } catch (Exception e) {
            log().fail("Unexpected error after Confirm / waiting for receipt: " + e.getMessage());
            e.printStackTrace();
            errorHandling.navigateBackToSafeScreen(5);
            return;
        }

    }

    // ========================================================================
    // SWIFT TRANSFER
    // ========================================================================
    public void transferSwift(Map<String, String> data) {
        ExtentTest test = log();
        test.info("🌍 STARTING SWIFT TRANSFER");

        page.openTransferModule();
        page.openSwift();
        page.closePopupIfVisible();

        page.openGenericFromAccount();
        page.selectDropdownValue(data.get("FromAccount"));

        page.swiftTypeAmount(data.get("Amount"));
        page.swiftTypeCurrency(data.get("Currency"));
        page.swiftTypeCountry(data.get("Country"));
        page.swiftTypeAccount(data.get("ToAccount"));
        page.swiftTypeName(data.get("Name"));

        // Additional sections
        page.scrollToText("Beneficiary Address");
        page.swiftTypeAddr1(data.get("BenAddress1"));
        page.swiftTypeAddr2(data.get("BenAddress2"));
        page.swiftTypeBIC(data.get("BenBIC"));
        page.swiftTypeBankName(data.get("BenBankName"));

        page.scrollToText("Charge Option");
        page.swiftTypeBankAddr(data.get("BenBankAddress"));
        page.swiftTypeCharge(data.get("ChargeOption"));
        page.oatTypeRemarks(data.get("Remarks"));

        selectDate(data);

        page.tapContinue();
        validateAndConfirm(data.get("ToAccount"));
    }

    // ========================================================================
    // UNIVERSAL HELPERS
    // ========================================================================
    private void validateAndConfirm(String accountRef) {
        ExtentTest test = log();
        if (!page.isConfirmPageVisible()) {
            test.fail("❌ Confirm page NOT visible");
            return;
        }

        page.tapConfirm();

        if (page.isReceiptVisible()) {
            test.pass("✅ Transfer SUCCESS → Receipt generated for: " + accountRef);
            return;
        }

        if (page.isLimitError()) {
            test.fail("❌ Limit exceeded!");
        }
    }

    private void selectDate(Map<String, String> data) {
        ExtentTest test = log();

        String raw = data.get("Date");
        if (raw == null || raw.trim().isEmpty()) {
            // No date instruction – just use whatever default the app has
            return;
        }

        System.out.println("Date cell value: " + raw);

        // Normalise: remove spaces and upper-case
        String normalized = raw.replace(" ", "").toUpperCase();

        // =TODAY() or TODAY()
//        if (normalized.equals("=TODAY()") || normalized.equals("TODAY()")) {
//            LocalDate target = LocalDate.now();
//            page.selectCalendarDate(target);
//            if (test != null) {
//                test.info("📅 Selected Current Date: " + target);
//            }
//            return;
//        }

        // =TODAY()+N or TODAY()+N
        if (normalized.startsWith("=TODAY()+") || normalized.startsWith("TODAY()+")) {
            String numPart = normalized
                    .replace("=TODAY()+", "")
                    .replace("TODAY()+", "");

            try {
                int days = Integer.parseInt(numPart);
                LocalDate target = LocalDate.now().plusDays(days);
                page.selectCalendarDate(target);
                if (test != null) {
                    test.info("📅 Selected Future Date: " + target + " (+" + days + " days)");
                }
            } catch (NumberFormatException nfe) {
                if (test != null) {
                    test.warning("⚠ Unable to parse days from Date cell: '" + raw + "'");
                }
            }
            return;
        }

    }

}
