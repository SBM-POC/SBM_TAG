package pages;

import base.BasePage;

public class LogOutPage extends BasePage {

    // ---------------------------------------------------------
    // MAIN ELEMENTS
    // ---------------------------------------------------------

    public static final String PROFILE_ICON =
            "//android.view.View[contains(@resource-id,'open-profile-menu')]";

    public static final String LOGOUT_BUTTON =
            "//android.widget.TextView[contains(@text,'Logout')]/ancestor::android.view.View";

    public static final String LOGOUT_POPUP_MESSAGE =
            "//android.widget.TextView[contains(@text,'logged out successfully')]";

    public static final String POPUP_CLOSE =
            "//android.widget.Button[contains(@text,'Close')]";

    public static final String LOGIN_BUTTON =
            "//android.widget.Button[contains(@text,'Login')]";

    // ---------------------------------------------------------
    // NAVIGATION FALLBACK BUTTONS
    // ---------------------------------------------------------

    public static final String BACK_WITH_TEXT =
            "//android.widget.Button[contains(@content-desc,'Back')]";

    public static final String BACK_WITHOUT_TEXT =
            "//android.widget.Button[contains(@content-desc,'back')]";

    public static final String SMALL_CLOSE =
            "//android.widget.Button[contains(@text,'close')]";

    public static final String ACCOUNT_CARD_CLOSE =
            "//android.widget.Button[contains(@resource-id,'acc-menu-btn')]";

    public static final String GREY_CLOSE_DOWNLOAD =
            "//android.view.View[contains(@resource-id,'download_txn_details_btn')]//android.widget.Button";

    public static final String CANCEL =
            "//android.widget.Button[contains(@text,'Cancel')]";

    // ---------------------------------------------------------
    // VISIBLE CHECKS
    // ---------------------------------------------------------

    public boolean isProfileIconVisible() {
        return isVisible(PROFILE_ICON);
    }

    public boolean isLogoutPopupVisible() {
        return isVisible(LOGOUT_POPUP_MESSAGE);
    }

    public boolean isLoginButtonVisible() {
        return isVisible(LOGIN_BUTTON);
    }

    // ---------------------------------------------------------
    // ACTIONS
    // ---------------------------------------------------------

    public void tapProfileIcon() {
        waitAndClick(PROFILE_ICON);
    }

    public void tapLogout() {
        waitAndClick(LOGOUT_BUTTON);
    }

    public void closeLogoutPopup() {
        clickIfPresent(POPUP_CLOSE);
    }

    // ---------------------------------------------------------
    // NAVIGATION CLEANUP
    // ---------------------------------------------------------

    public boolean tryNavigateBack() {
        if (clickIfPresent(BACK_WITH_TEXT)) return true;
        if (clickIfPresent(BACK_WITHOUT_TEXT)) return true;
        if (clickIfPresent(ACCOUNT_CARD_CLOSE)) return true;
        if (clickIfPresent(SMALL_CLOSE)) return true;
        if (clickIfPresent(GREY_CLOSE_DOWNLOAD)) return true;
        if (clickIfPresent(CANCEL)) return true;
        if (clickIfPresent(POPUP_CLOSE)) return true;
        return false;
    }

    // Universal helper

    public boolean clickIfPresent(String xpath) {
        if (isElementPresent(xpath)) {
            waitAndClick(xpath);
            return true;
        }
        return false;
    }

    // ---------------------------------------------------------
    // SAFE NAVIGATION (BACK UNTIL HOME)
    // ---------------------------------------------------------

    public void navigateBackToHome(int max) {

        navigateBackToSafeScreen(PROFILE_ICON, max);

    }
}

