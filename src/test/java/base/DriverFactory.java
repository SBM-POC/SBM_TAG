package base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class DriverFactory {

    // One single ThreadLocal that owns the driver for the whole framework
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

    /**
     * CREATE DRIVER — called from BaseTest.launchApp()
     */
    public static AppiumDriver createDriver(String deviceName,
                                            String platformVersion,
                                            String environment,
                                            int portNumber,
                                            String platformType,
                                            String appType) throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();

        // -------- COMMON CAPS --------
        caps.setCapability("deviceName", deviceName);
        caps.setCapability("platformVersion", platformVersion);
        caps.setCapability("platformName", platformType);          // Android / iOS
        caps.setCapability("newCommandTimeout", 300);

        if (platformType.equalsIgnoreCase("Android")) {
            caps.setCapability("automationName", "UiAutomator2");
            caps.setCapability("noReset", false);
            caps.setCapability("fullReset", true);
            caps.setCapability("unicodeKeyboard", true);
            caps.setCapability("resetKeyboard", true);

            // ---------- APP UNDER TEST (SBMBANK) ----------
            if (appType != null && appType.equalsIgnoreCase("SBMBANK")) {

                String base = System.getProperty("user.dir");
                String appPath;

                switch (environment.toLowerCase()) {
                    case "dev":
                        appPath = base + "/TestData/sbmDEV.apk";
                        break;
                    case "preprod":
                        appPath = base + "/STD/sbmPreprod.apk";  // STD folder at project root
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
            // ---------- iOS BASELINE ----------
            caps.setCapability("automationName", "XCUITest");
            // TODO: Add bundleId / app path when needed
        }

        // -------- APPIUM SERVER URL --------
        URL serverURL = new URL("http://127.0.0.1:" + portNumber);

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
