package base;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

//import reports.ExtentManager;
import utils.TestUtils;
import utils.ReportManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class BaseTest {

    // ------------------- THREAD-SAFE OBJECTS -------------------
    protected static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    protected static ExtentReports extent;

    protected TestUtils utils = new TestUtils();

    // ------------------- GLOBAL CONTEXT -------------------
    protected static String PLATFORM;     // Android / iOS
    protected static String ENVIRONMENT;  // dev / uat / preprod / prod
    protected static String APP_TYPE;     // SBMBANK / etc.

    // Toggle: If true → create method-level nodes in Extent
    protected static final boolean CREATE_METHOD_NODE = false;

    // ------------------------ PUBLIC GETTERS ------------------------
    public static ExtentReports getExtent()           { return extent; }
    public static ThreadLocal<ExtentTest> getTest()   { return test; }

    public static AppiumDriver getDriverStatic()      { return DriverFactory.getDriver(); }
    public AppiumDriver getDriver()                   { return DriverFactory.getDriver(); }

    public static String getPlatform()                { return PLATFORM; }
    public static String getEnvironmentName()         { return ENVIRONMENT; }
    public static String getAppType()                 { return APP_TYPE; }

    // ------------------------ PUBLIC SETTERS ------------------------
    public static void setExtent(ExtentReports setExtent)           { extent = setExtent; }
    public static void setTest(ThreadLocal<ExtentTest> setTest)           { test = setTest; }

    // ------------------------ BEFORE SUITE ------------------------
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        // 1) Create Extent instance
        ReportManager rm = new ReportManager();
        rm.setupExtentReports();

        // 2) Start Appium server (single instance for the suite)
        AppiumServerManager.startServer();
    }

    // ------------------------ BEFORE TEST ------------------------
    @Parameters({ "deviceName", "platformVersion", "environment", "portNumber", "platformType", "appType" })
    @BeforeTest(alwaysRun = true)
    public void launchApp(String deviceName,
                          String platformVersion,
                          String environment,
                          int portNumber,
                          String platformType,
                          @Optional("SBMBANK") String appType) throws Exception {

        // Save context for later use
        PLATFORM    = platformType;
        ENVIRONMENT = environment;
        APP_TYPE    = appType;

        // Create driver via DriverFactory (it stores the driver itself)
        DriverFactory.createDriver(
                deviceName,
                platformVersion,
                environment,
                portNumber,
                platformType,
                appType
        );
    }

    // ------------------------ BEFORE METHOD ------------------------
//    @BeforeMethod(alwaysRun = true)
//    public void beforeMethod(Method m) {
//
//        if (CREATE_METHOD_NODE) {
//            String testName = m.getDeclaringClass().getSimpleName() + " :: " + m.getName();
//
//            ExtentTest methodNode = extent.createTest(testName);
//            test.set(methodNode);
//        } else {
//            // We let test classes create their own nodes if they want to
//            test.remove();
//        }
//    }

    // ------------------------ AFTER METHOD ------------------------
//    @AfterMethod(alwaysRun = true)
//    public void afterMethod(ITestResult result) {
//
//        ExtentTest currentTest = test.get();
//
//        // 1) Log status in Extent (only if node exists)
//        if (CREATE_METHOD_NODE && currentTest != null) {
//            switch (result.getStatus()) {
//                case ITestResult.SUCCESS:
//                    currentTest.pass("Test passed");
//                    break;
//                case ITestResult.FAILURE:
//                    currentTest.fail(result.getThrowable());
//                    attachScreenshotIfPossible(currentTest, result.getName());
//                    break;
//                case ITestResult.SKIP:
//                    currentTest.skip(result.getThrowable());
//                    break;
//            }
//        } else if (result.getStatus() == ITestResult.FAILURE) {
//            // Even if we don't use method nodes, still take screenshot for debugging
//            captureScreenshotToFile(result.getName());
//        }
//    }

    // ------------------------ AFTER TEST ------------------------
    @AfterTest(alwaysRun = true)
    public void afterTest() {
        DriverFactory.quitDriver();
    }

    // ------------------------ AFTER SUITE ------------------------
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        // Stop Appium server
        AppiumServerManager.stopServer();

        // Flush Extent once at the very end
        if (extent != null) {
            extent.flush();
        }
    }

    // ------------------------ SCREENSHOT HELPERS ------------------------
    private void attachScreenshotIfPossible(ExtentTest currentTest, String testName) {
        try {
            String path = captureScreenshotToFile(testName);
            if (path != null) {
                currentTest.addScreenCaptureFromPath(path);
            }
        } catch (Exception e) {
            // Silent fail: screenshot is just a bonus for debugging
            e.printStackTrace();
        }
    }

    private String captureScreenshotToFile(String testName) {
        AppiumDriver d = getDriver();
        if (d == null) return null;

        try {
            String timestamp = utils.getDateTime(); // e.g. 2025-11-24_14-12-00
            String screenshotDir = System.getProperty("user.dir") + File.separator + "Screenshots";
            File dir = new File(screenshotDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = testName + "_" + timestamp + ".png";
            File destFile = new File(dir, fileName);

            File srcFile = ((TakesScreenshot) d).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return destFile.getAbsolutePath();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
