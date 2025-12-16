package flows;

import utils.ReportManager;
import java.util.Map;

public class E2EPayToMobileFlow {

    private final LoginFlow login = new LoginFlow();
    private final PayToMobileFlow pay = new PayToMobileFlow();
    private final LogOutFlow logout = new LogOutFlow();

    public void execute(Map<String, String> data) throws Exception {

        String scenarioType = data.get("Type"); // POSITIVE / NEGATIVE
        String desc = data.get("Description") != null ?
                data.get("Description") :
                "Pay To Mobile - " + scenarioType;

        ReportManager.suite("Pay To Mobile - " + scenarioType);
        ReportManager.scenario(desc);

        // LOGIN
        login.login(data.get("LoginID"), data.get("Password"));

        // EXECUTE FLOW
        if ("NEGATIVE".equalsIgnoreCase(scenarioType)) {
            pay.payToMobileNegativeFlow(data);
        } else {
            pay.payToMobilePositiveFlow(data);
        }

        // LOGOUT
        logout.logout();
    }

}
