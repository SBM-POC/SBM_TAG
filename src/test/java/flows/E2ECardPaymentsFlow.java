package flows;

import com.aventstack.extentreports.ExtentTest;
import utils.ReportManager;

import java.util.Map;

public class E2ECardPaymentsFlow {
    private final LoginFlow login = new LoginFlow();
    private final CardPaymentsFlow cardPayments = new CardPaymentsFlow();
    private final LogOutFlow logout = new LogOutFlow();

    public void runRMPC(Map<String, String> data) {
        // 1. Create suite and scenario for Card Payments
        ReportManager.suite("Card Payments - Recharge My Prepaid Card (Positive Flow)");
        ReportManager.scenario(data.getOrDefault("Description", "Recharge My prepaid Card"));
        // 2. Login
        login.login(data.get("LoginID"), data.get("Password"));
        // 3. RMPC flow
        cardPayments.rechargePrepaidCard(data);
        // 4. Logout
        logout.logout();
    }

    public void runRMPCNegative(Map<String, String> data) {
        // 1. Create suite and scenario for Card Payments
        ReportManager.suite("Card Payments - Recharge My Prepaid Card (Negative flow)");
        ReportManager.scenario(data.getOrDefault("Description", "Recharge My prepaid Card"));
        // 2. Login
        login.login(data.get("LoginID"), data.get("Password"));
        // 3. RMPC flow
        cardPayments.rechargePrepaidCardNegative(data);
        // 4. Logout
        logout.logout();
    }
}
