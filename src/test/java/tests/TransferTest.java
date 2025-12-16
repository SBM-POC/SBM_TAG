package tests;

import base.BaseTest;
import dataprovider.TransferDataProvider;
import flows.TransferFlow;
import org.testng.annotations.Test;

import java.util.Map;

public class TransferTest extends BaseTest {

    // ============================================================
    // OWN ACCOUNT TRANSFER
    // ============================================================
    @Test(
            dataProvider = "ownAccount",
            dataProviderClass = TransferDataProvider.class,
            description = "Own Account Transfer",
            groups = { "smoke"}
    )

    public void testOwnAccountTransfer(Map<String, String> data) throws Exception {
        new TransferFlow().transferOwnAccount(data);
    }

    // ============================================================
    // OTHER SBM TRANSFER
    // ============================================================
    @Test(
            dataProvider = "otherSbm",
            dataProviderClass = TransferDataProvider.class,
            description = "Other SBM Account Transfer"
    )
    public void testOtherSbmTransfer(Map<String, String> data) throws Exception {
        new TransferFlow().transferSBM(data);
    }

    // ============================================================
    // OTHER LOCAL BANK TRANSFER
    // ============================================================
    @Test(
            dataProvider = "localBank",
            dataProviderClass = TransferDataProvider.class,
            description = "Other Local Bank Transfer"
    )
    public void testLocalBankTransfer(Map<String, String> data) throws Exception {
        new TransferFlow().transferLocalBank(data);
    }

    // ============================================================
    // SWIFT TRANSFER
    // ============================================================
    @Test(
            dataProvider = "swift",
            dataProviderClass = TransferDataProvider.class,
            description = "SWIFT Transfer"
    )
    public void testSwiftTransfer(Map<String, String> data) throws Exception {
        new TransferFlow().transferSwift(data);
    }
}
