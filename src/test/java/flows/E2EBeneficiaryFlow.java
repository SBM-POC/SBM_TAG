package flows;

import utils.ReportManager;

import java.util.Map;

public class E2EBeneficiaryFlow {

    private final LoginFlow      login  = new LoginFlow();
    private final BeneficiaryFlow flow  = new BeneficiaryFlow();
    private final LogOutFlow     logout = new LogOutFlow();

    public void execute(Map<String, String> data, String type) throws Exception {

        String action = data.get("Action");
        String desc   = data.get("Description");

        ReportManager.suite("My Beneficiaries - " + type);
        ReportManager.scenario(desc);

        // LOGIN
        login.login(data.get("LoginID"), data.get("Password"));

        switch (action.toUpperCase()) {
            case "ADD":
                flow.addBeneficiary(data, type);
                break;
            case "EDIT":
                flow.editBeneficiary(data, type);
                break;
            case "PAY":
                flow.payBeneficiary(data, type);
                break;
            case "DELETE":
                flow.deleteBeneficiary(data, type);
                break;
            default:
                throw new RuntimeException("Unknown Action: " + action);
        }

        // LOGOUT
        logout.logout();
    }
}
