package base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    public static AppiumDriver getDriver() {
        return driver.get();
    }

    private static void setDriver(AppiumDriver d) {
        driver.set(d);
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    public static AppiumDriver createDriver(String deviceName,
                                            String platformVersion,
                                            String environment,
                                            int portNumber,
                                            String platformType,
                                            String appType) throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();

        boolean isBrowserStack = System.getenv("BROWSERSTACK_USERNAME") != null;

        // -------- COMMON CAPS --------
        caps.setCapability("deviceName", deviceName);
        caps.setCapability("platformVersion", platformVersion);
        caps.setCapability("platformName", platformType);
        caps.setCapability("newCommandTimeout", 300);

        if (platformType.equalsIgnoreCase("Android")) {
            caps.setCapability("automationName", "UiAutomator2");

            // --------------------------------------------------
            // 🔥 APP CONFIGURATION (LOCAL vs BROWSERSTACK)
            // --------------------------------------------------
            if (isBrowserStack) {

                // 👉 BrowserStack App (bs://...)
                caps.setCapability("app", System.getenv("BROWSERSTACK_APP"));

                Map<String, Object> bstackOptions = new HashMap<>();
                bstackOptions.put("userName", System.getenv("BROWSERSTACK_USERNAME"));
                bstackOptions.put("accessKey", System.getenv("BROWSERSTACK_ACCESS_KEY"));
                bstackOptions.put("projectName", "SBM TAG Mobile Automation");
                bstackOptions.put("buildName", "GitHub Appium Build");
                bstackOptions.put("sessionName", "Android Tests");
                bstackOptions.put("debug", true);
                bstackOptions.put("networkLogs", true);

                caps.setCapability("bstack:options", bstackOptions);

            } else {

                // 👉 LOCAL APK LOGIC (UNCHANGED)
                String base = System.getProperty("user.dir");
                String appPath;

                switch (environment.toLowerCase()) {
                    case "dev":
                        appPath = base + "/TestData/sbmDEV.apk";
                        break;
                    case "preprod":
                        appPath = base + "/STD/sbmPreprod.apk";
                        break;
                    case "uat":
                        appPath = base + "/STD/sbmUAT.apk";
                        break;
                    case "prod":
                        appPath = base + "/TestData/sbmPROD.apk";
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid environment: " + environment);
                }

                caps.setCapability("appPackage", "com.sbmbank.mb");
                caps.setCapability("appWaitActivity", "*");
                caps.setCapability("app", appPath);
            }

        } else if (platformType.equalsIgnoreCase("iOS")) {
            caps.setCapability("automationName", "XCUITest");
            // Same pattern applies later for iOS
        }

        // --------------------------------------------------
        // 🔥 SERVER URL (LOCAL vs BROWSERSTACK)
        // --------------------------------------------------
        URL serverURL;

        if (isBrowserStack) {
            serverURL = new URL("https://hub.browserstack.com/wd/hub");
        } else {
            serverURL = new URL("http://127.0.0.1:" + portNumber);
        }

        AppiumDriver appiumDriver;

        if (platformType.equalsIgnoreCase("Android")) {
            appiumDriver = new AndroidDriver(serverURL, caps);
        } else if (platformType.equalsIgnoreCase("iOS")) {
            appiumDriver = new IOSDriver(serverURL, caps);
        } else {
            throw new IllegalArgumentException("Unsupported platformType: " + platformType);
        }

        setDriver(appiumDriver);
        return appiumDriver;
    }
}
