package tests;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import dataprovider.LoginDataProvider;
import flows.LoginFlow;
import org.testng.annotations.Test;

import java.util.Map;


public class LoginTest extends BaseTest {

    @Test(dataProvider = "loginUsers", dataProviderClass = LoginDataProvider.class)
    public void login_with_all_users(Map<String, String> userData) {

        ExtentTest logger = test.get();
        LoginFlow flow = new LoginFlow();

        String username = userData.get("Username");   // must match Excel header exactly
        String password = userData.get("Password");

        logger.info("====== LOGIN ATTEMPT for user: " + username + " ======");

        flow.login(username, password);
    }
}
