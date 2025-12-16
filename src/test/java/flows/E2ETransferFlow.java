package flows;

import com.aventstack.extentreports.ExtentTest;
import flows.LoginFlow;
import flows.TransferFlow;
import flows.LogOutFlow;
import utils.ReportManager;

import java.util.Map;

public class E2ETransferFlow {

    private final LoginFlow login = new LoginFlow();
    private final TransferFlow transfer = new TransferFlow();
    private final LogOutFlow logout = new LogOutFlow();

    // =====================================================
    // MAIN E2E FLOW
    // =====================================================
    public void execute(Map<String, String> data) throws Exception {
        // -----------------------------
        // 1. TRANSFER TYPE SELECTION
        // -----------------------------
        String type = "LOCAL"; // OWN / SBM / LOCAL / SWIFT

        switch (type.toUpperCase()) {
            case "OWN":
                ReportManager.suite("Account Transfers - Own Account Transfer");
                ReportManager.scenario(data.get("Description"));
                // --------------------- Login -----------------------
                login.login(data.get("LoginID"), data.get("Password"));
                transfer.transferOwnAccount(data);
                break;

            case "SBM":
                ReportManager.suite("Account Transfers - Other SBM Transfer");
                ReportManager.scenario(data.get("Description"));
                // --------------------- Login -----------------------
                login.login(data.get("LoginID"), data.get("Password"));
                transfer.transferSBM(data);
                break;

            case "LOCAL":
                ReportManager.suite("Account Transfers - Other Local Bank Transfer");
                ReportManager.scenario(data.get("Description"));
                // --------------------- Login -----------------------
                login.login(data.get("LoginID"), data.get("Password"));
                transfer.transferLocalBank(data);
                break;

            case "SWIFT":
                ReportManager.suite("Account Transfers - SWIFT Transfer");
                ReportManager.scenario(data.get("Description"));
                // --------------------- Login -----------------------
                login.login(data.get("LoginID"), data.get("Password"));
                transfer.transferSwift(data);
                break;

            default:
//                test.fail("❌ Invalid TransferType: " + type);
                return;
        }

        // -----------------------------
        // 3. LOGOUT
        // -----------------------------
        logout.logout();

        //test.pass("🎉 E2E Flow Completed Successfully");
    }
}
