package flows;

import utils.ReportManager;
import java.util.Map;

public class E2EMobileTopUpFlow {

    private final LoginFlow login = new LoginFlow();
    private final MobileTopUpFlow mobile = new MobileTopUpFlow();
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
            mobile.MobileTopUpNegativeFlow(data);
        } else {
            mobile.MobileTopUpPositiveFlow(data);
        }

        // LOGOUT
        logout.logout();
    }
}
