package flows;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import pages.LogOutPage;
import utils.ReportManager;
import utils.TestUtils;

public class LogOutFlow {

    private final LogOutPage page = new LogOutPage();
    private final TestUtils utils = new TestUtils();

    public void logout() {
        // Setting up Extent Report for Logout
        ReportManager.step("Logout");
        ExtentTest test = BaseTest.getTest().get();

        test.info("🔐 Starting LOGOUT process");

        // Step 1 — Navigate back until profile icon visible
        int attempts = 10;
        while (!page.isProfileIconVisible() && attempts > 0) {

            if (test != null) {
                test.info("↩ Attempting navigation cleanup (" + attempts + " left)");
            }

            boolean clicked = page.tryNavigateBack();

            attempts--;               // always guard against infinite loop
            if (!clicked) {
                // Nothing else to click; stop trying
                break;
            }

            utils.sleep(500); // prevent fast loop
        }

        // Step 2 — If still not visible → fail
        if (!page.isProfileIconVisible()) {
            test.fail("❌ Profile icon NOT found — cannot perform logout");
            utils.takeScreenshot("Logout_Profile_Not_Found");
            return;
        }

        test.info("👤 Profile icon found → opening profile menu");
        page.tapProfileIcon();

        test.info("🚪 Tapping Logout button");
        page.tapLogout();

        // Step 3 — Popup check
        if (page.isLogoutPopupVisible()) {
            test.pass("✔ Logout confirmation popup shown");
            page.closeLogoutPopup();
        }

        // Step 4 — Final validation
        if (page.isLoginButtonVisible()) {
            test.pass("🎉 Logout Successful — back on Login Screen");
        } else {
            test.fail("❌ Logout FAILED — Login screen NOT detected");
            utils.takeScreenshot("Logout_Failed");
        }
    }
}
