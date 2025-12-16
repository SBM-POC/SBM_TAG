package flows;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import pages.CardPaymentsPage;
import pages.TransferPage;
import utils.ErrorHandling;
import utils.ReceiptValidator;
import utils.ReportManager;

import java.util.Map;

public class CardPaymentsFlow {
    private final TransferPage transferPage = new TransferPage();
    private final CardPaymentsPage cardPage = new CardPaymentsPage();
    private final ErrorHandling errorHandling = new ErrorHandling(BaseTest.getDriverStatic());
    private final ReceiptValidator receiptValidator = new ReceiptValidator();

    private ExtentTest log() {
        return BaseTest.getTest().get();  // may be null if no step set yet
    }

    public void rechargePrepaidCard(Map<String, String> data) {
        log(); // ensure ExtentTest is initialized

        // -----------------------------
        // 1. NAVIGATION
        // -----------------------------
        try {
            ReportManager.step("Navigating to 'Recharge My Prepaid Card' page");
            cardPage.openCardPaymentsModule(); // Go to transfer module and Open card payments section
            cardPage.openRechargeMyPrepaidCard(); // Tap and open RMPC screen
        } catch (Exception e) {
            log().fail("Unexpected error in navigating to RMPC's form: " + e.getMessage());
            e.printStackTrace();
            errorHandling.navigateBackToSafeScreen(5);
        }

        // -----------------------------
        // 2. FILL FORM
        // -----------------------------
        try {
            ReportManager.step("Filling RMPC form");
            //From Account
            cardPage.rmpcOpenFromAccount();
            cardPage.selectDropdownValue(data.get("FromAccount"));
            // Pay/Transfer To card
            cardPage.rmpcOpenTransferTo();
            cardPage.selectDropdownValue(data.get("TransferTo"));
            // Currency is prefilled upon selecting card, and cannot be changed
            // Amount
            cardPage.rmpcTypeAmount(data.get("Amount"));
            // Remarks
            cardPage.rmpcTypeRemarks(data.get("Remarks"));
        } catch (Exception e) {
            log().fail("Unexpected error in filling RMPC's form: " + e.getMessage());
            e.printStackTrace();
            errorHandling.navigateBackToSafeScreen(5);
        }

        // -----------------------------
        // 3. CONTINUE TO CONFIRMATION PAGE
        // -----------------------------
        try {
            cardPage.rmpcTapContinue();
            // Error handling
            if (errorHandling.handleErrorMessages()) {
                System.out.println("Errors found on screen after Continue. Skipping this row.");
                if (log() != null) {
                    log().fail("Errors found on screen after Continue. Skipping this row.");
                }
                errorHandling.navigateBackToSafeScreen(5);
                return;
            }
        } catch (Exception e) {
            log().fail("Unexpected error in continuing to RMPC's confirmation page: " + e.getMessage());
            e.printStackTrace();
            errorHandling.navigateBackToSafeScreen(5);
        }

        // -----------------------------
        // 4. CONFIRM PAGE
        // -----------------------------
        try {
            ReportManager.step("Confirming 'Recharge My Prepaid Card' transaction");
            cardPage.tapConfirm();

            // After Confirm, check for any errors (insufficient funds, limit exceeded, etc.)
            if (errorHandling.handleErrorMessages()) {
                System.out.println("Errors found on screen after Confirm. Skipping this row.");
                if (log() != null) {
                    log().fail("Errors found on screen after Confirm. Skipping this row.");
                }
                errorHandling.navigateBackToSafeScreen(5);
                return;
            }
        } catch (Exception e) {
            log().fail("Unexpected error in RMPC's receipt generation and validation: " + e.getMessage());
            e.printStackTrace();
            errorHandling.navigateBackToSafeScreen(5);
        }

        // -----------------------------
        // 5. RECEIPT & VALIDATION
        // -----------------------------
        try {
            // Check if receipt page page appears
            if (!cardPage.isReceiptVisible()) {
                log().fail("❌ Receipt NOT visible after RMPC Confirm");
                errorHandling.navigateBackToSafeScreen(5);
                return;
            }

            // Generate receipt
            ReportManager.step("Generating and validating RMPC receipt");
            cardPage.generateReceipt();
            // Handle error popups if any
            if (errorHandling.handleErrorMessages()) {
                System.out.println("Errors found on screen after Generate Receipt. Skipping this row.");
                log().warning("Errors found on screen after Generate Receipt. Skipping this row.");
                errorHandling.navigateBackToSafeScreen(5);
                return;
            }

            // Validate receipt contents
            receiptValidator.validateReceipt(data);
        } catch (Exception e) {
            log().fail("Unexpected error in RMPC's receipt generation and validation: " + e.getMessage());
            e.printStackTrace();
            errorHandling.navigateBackToSafeScreen(5);
        }
    }

    // ------------------
    // NEGATIVE FLOWS
    // ------------------
    public void rechargePrepaidCardNegative(Map<String, String> data) {
        log(); // ensure ExtentTest is initialized

        // -----------------------------
        // 1. NAVIGATION
        // -----------------------------
        try {
            ReportManager.step("Navigating to 'Recharge My Prepaid Card' page");
            transferPage.openTransferModule(); // Go to transfer module
            cardPage.openCardPaymentsModule(); // Open card payments section
            cardPage.openRechargeMyPrepaidCard(); // Tap and open RMPC screen
        } catch (Exception e) {
            log().fail("Unexpected error in navigating to RMPC's form: " + e.getMessage());
            e.printStackTrace();
            errorHandling.navigateBackToSafeScreen(5);
        }

        // -----------------------------
        // 2. FILL FORM
        // -----------------------------
        try {
            ReportManager.step("Filling RMPC form");
            //From Account
            cardPage.rmpcOpenFromAccount();
            cardPage.selectDropdownValue(data.get("FromAccount"));
            // Pay/Transfer To card
            cardPage.rmpcOpenTransferTo();
            cardPage.selectDropdownValue(data.get("TransferTo"));
            // Currency is prefilled upon selecting card, and cannot be changed
            // Amount
            cardPage.rmpcTypeAmount(data.get("Amount"));
            // Remarks
            cardPage.rmpcTypeRemarks(data.get("Remarks"));
        } catch (Exception e) {
            log().fail("Unexpected error in filling RMPC's form (negative flow): " + e.getMessage());
            e.printStackTrace();
            errorHandling.navigateBackToSafeScreen(5);
        }

        // -----------------------------
        // 3. TAP ON 'CONTINUE' FOR FIELD VALIDATORS
        // -----------------------------
        try {
            cardPage.rmpcTapContinue();
            // Check and assert, if no errors have been found when it should, it will fail the test
            if (!errorHandling.expectErrorsAfterContinue(data, "Recharge My Prepaid Card")) {
                return;
            }
        } catch (Exception e) {
            log().fail("Unexpected error in continuing to RMPC's confirmation page: " + e.getMessage());
            e.printStackTrace();
            errorHandling.navigateBackToSafeScreen(5);
        }

        // -----------------------------
        // 4. CONFIRM PAGE
        // -----------------------------
        try {
            ReportManager.step("Confirming 'Recharge My Prepaid Card' transaction (Negative Flow)");
            cardPage.tapConfirm();

            // Check and assert, if no errors have been found when it should, it will fail the test
            if (!errorHandling.expectErrorsAfterConfirm(data, "Recharge My Prepaid Card")) {
                return;
            }
        } catch (Exception e) {
            log().fail("Unexpected error in RMPC's receipt generation and validation: " + e.getMessage());
            e.printStackTrace();
            errorHandling.navigateBackToSafeScreen(5);
        }

        // -----------------------------
        // 5. RECEIPT & VALIDATION
        // -----------------------------
        try {
            // Check if receipt page page appears
            if (!cardPage.isReceiptVisible()) {
                log().fail("❌ Receipt NOT visible after RMPC Confirm");
                errorHandling.navigateBackToSafeScreen(5);
                return;
            }

            // Generate receipt
            ReportManager.step("Generating and validating RMPC receipt");
            cardPage.generateReceipt();
            // Handle error popups if any
            if (errorHandling.handleErrorMessages()) {
                System.out.println("Errors found on screen after Generate Receipt. Skipping this row.");
                log().warning("Errors found on screen after Generate Receipt. Skipping this row.");
                errorHandling.navigateBackToSafeScreen(5);
                return;
            }

            // Validate receipt contents
            receiptValidator.validateReceipt(data);
        } catch (Exception e) {
            log().fail("Unexpected error in RMPC's receipt generation and validation: " + e.getMessage());
            e.printStackTrace();
            errorHandling.navigateBackToSafeScreen(5);
        }
    }
}
