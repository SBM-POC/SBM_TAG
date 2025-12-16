package flows;

import com.aventstack.extentreports.ExtentTest;
import pages.LoginPage;
import utils.ReportManager;
import utils.TestUtils;

public class LoginFlow {

    private final LoginPage page = new LoginPage();
    private final TestUtils utils = new TestUtils();

    public void login(String loginId, String password) {
        ExtentTest test = ReportManager.step("Login");
        test.info("🔐 Logging in with user: <b>" + loginId + "</b>");

        // ---------------------------
        // 1. Fill Login ID & Password
        // ---------------------------
        page.enterLoginId(loginId);
        page.enterPassword(password);

        // ---------------------------
        // 2. Tap Login Button
        // ---------------------------
        page.tapLoginButton();

        // ---------------------------
        // 3. Wait for Loader
        // ---------------------------
        try {
            page.waitForLoaderToDisappear();
        } catch (Exception e) {
            test.warning("Loader wait timeout — continuing...");
        }

        // ---------------------------
        // 4. Server Error
        // ---------------------------
        if (page.isServerErrorVisible()) {
            test.fail("❌ Server Error");
            utils.takeScreenshot("ServerError");
            return;
        }

        // ---------------------------
        // 5. Passcode Not Enabled
        // ---------------------------
        if (page.isPasscodeErrorVisible()) {
            test.fail("⚠ Passcode not enabled");
            utils.takeScreenshot("PasscodeError");
            page.tapCloseButton();
            return;
        }

        // ---------------------------
        // 6. Walkthrough (Skip)
        // ---------------------------
//        if (page.isSkipVisible()) {
//            test.info("➡ Walkthrough detected → skipping");
//            page.tapSkipButton();
//        }

        // ---------------------------
        // 7. Home Visible = Login Success
        // ---------------------------
        if (page.isHomeVisible()) {
            test.pass("🏠 Login Successful");
            return;
        }

        // ---------------------------
        // 8. Unknown State
        // ---------------------------
        test.fail("❌ Unknown login state");
        utils.takeScreenshot("UnknownState");
    }
}
