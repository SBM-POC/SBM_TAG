package pages;

import base.BasePage;

public class LoginPage extends BasePage {

    // ---------------------------------------------------------
    //  LOCATORS (Android Only)
    // ---------------------------------------------------------
    private static final String LOGIN_ID = "(//android.widget.EditText[@index='0'])[1]";
    private static final String PASSWORD = "(//android.widget.EditText[@index='0'])[2]";
    private static final String LOGIN_BUTTON = "//android.widget.Button[contains(@text, 'Login')]";

    private static final String SERVER_ERROR = "//android.widget.TextView[contains(@text, 'Server is not available.')]";
    private static final String LOADER = "(//android.widget.TextView[@index='0'])[1]";
    private static final String HOME_SCREEN_ERROR = "//android.widget.TextView[contains(@text, 'Please enable passcode lock')]";

    private static final String SKIP_BUTTON = "//android.widget.TextView[contains(@text, 'Skip')]";
    private static final String HOME_TAB = "//android.view.View[contains(@text, 'Home')]";
    private static final String CLOSE_BUTTON = "//android.widget.TextView[contains(@text, 'Close')]";

    // ---------------------------------------------------------
    //  USER ACTIONS
    // ---------------------------------------------------------

    public void enterLoginId(String value) {
        waitAndType(LOGIN_ID, value);
    }

    public void enterPassword(String value) {
        waitAndType(PASSWORD, value);
    }

    public void tapLoginButton() {
        waitAndClick(LOGIN_BUTTON);
    }

    public void tapSkipButton() {
        waitAndClick(SKIP_BUTTON);
    }

    public void tapCloseButton() {
        waitAndClick(CLOSE_BUTTON);
    }

    // ---------------------------------------------------------
    //  VALIDATION / STATE CHECKS
    // ---------------------------------------------------------

    public boolean isServerErrorVisible() {
        return isVisible(SERVER_ERROR);
    }

    public boolean isPasscodeErrorVisible() {
        return isVisible(HOME_SCREEN_ERROR);
    }

    public boolean isSkipVisible() {
        return isVisible(SKIP_BUTTON);
    }

    public boolean isHomeVisible() {
        return isVisible(HOME_TAB);
    }

    // ---------------------------------------------------------
    //  LOADING HANDLING
    // ---------------------------------------------------------

    public void waitForLoaderToDisappear() {
        waitUntilInvisibility(LOADER);
    }
}
