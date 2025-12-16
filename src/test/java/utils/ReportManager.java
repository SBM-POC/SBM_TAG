package utils;

import base.BaseTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import base.BaseTest;

public class ReportManager {

    /* The hierarchy is broken as such:
     * - suite
     * -- scenario
     * --- step
     **/

    public ExtentSparkReporter sparkReporter;
    public static Logger logger = LogManager.getLogger(BaseTest.class);

    // --------------- Setups ------------------
    public void setupExtentReports() {
        try {
            String reportDirPath = System.getProperty("user.dir") + File.separator + "Reports";
            File reportDir = new File(reportDirPath);
            if (!reportDir.exists()) reportDir.mkdirs();

            String reportFile = reportDirPath + File.separator + "ExtentReport.html";
            sparkReporter = new ExtentSparkReporter(reportFile);
            sparkReporter.config().setDocumentTitle("SBM TAG Automation Report");
            sparkReporter.config().setReportName("Mobile Automation Results");
            sparkReporter.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.DARK);

            // ---- Custom JS to tweak dashboard labels ----
            String customJs =
                    "document.addEventListener('DOMContentLoaded', function() {"
                            + "  // --- 1. Side nav: Tests -> Suites ---\n"
                            + "  var navTitle = document.querySelector('#nav-test .title');"
                            + "  if (navTitle && navTitle.textContent.trim() === 'Tests') {"
                            + "    navTitle.textContent = 'Suites';"
                            + "  }"
                            + "\n"
                            + "  // --- 2. Index page (test view): header label 'Tests' -> 'Suites' ---\n"
                            + "  document.querySelectorAll('.test-list-tools .font-size-14').forEach(function(el) {"
                            + "    if (el.textContent.trim() === 'Tests') {"
                            + "      el.textContent = 'Suites';"
                            + "    }"
                            + "  });"
                            + "\n"
                            + "  // --- 3. Dashboard page tweaks ---\n"
                            + "  if (document.body.classList.contains('dashboard-view')) {"
                            + "    // 3.1. Top donut cards titles: 'Tests' -> 'Suites', 'Steps' -> 'Tests'\n"
                            + "    document.querySelectorAll('.card .card-header .card-title').forEach(function(t) {"
                            + "      var txt = t.textContent.trim();"
                            + "      if (txt === 'Tests') {"
                            + "        t.textContent = 'Suites';"
                            + "      } else if (txt === 'Steps') {"
                            + "        t.textContent = 'Tests';"
                            + "      }"
                            + "    });"
                            + "\n"
                            + "    // 3.2. Small stat cards headings (bottom row): same mapping\n"
                            + "    document.querySelectorAll('.card .card-body p').forEach(function(p) {"
                            + "      var txt = p.textContent.trim();"
                            + "      if (txt === 'Tests') {"
                            + "        p.textContent = 'Suites';"
                            + "      } else if (txt === 'Steps') {"
                            + "        p.textContent = 'Tests';"
                            + "      }"
                            + "    });"
                            + "\n"
                            + "    // 3.3. Donut card footers: '0 tests passed' -> '0 suites passed',\n"
                            + "    //      '2 steps passed' -> '2 tests passed', etc.\n"
                            + "    var footers = document.querySelectorAll('.card .card-footer');"
                            + "    if (footers[0]) {"
                            + "      footers[0].querySelectorAll('small').forEach(function(s) {"
                            + "        s.textContent = s.textContent.replace(/tests/g, 'suites');"
                            + "      });"
                            + "    }"
                            + "    if (footers[1]) {"
                            + "      footers[1].querySelectorAll('small').forEach(function(s) {"
                            + "        s.textContent = s.textContent.replace(/steps/g, 'tests');"
                            + "      });"
                            + "    }"
                            + "  }"
                            + "});";

            sparkReporter.config().setJS(customJs);

            ExtentReports extent = new ExtentReports();
            BaseTest.setExtent(extent);
            BaseTest.getExtent().attachReporter(sparkReporter);
            BaseTest.getExtent().setSystemInfo("HostName", "FM"); // CHANGE
            BaseTest.getExtent().setSystemInfo("UserName", "Fadil"); // CHANGE
            BaseTest.getExtent().setSystemInfo("OS Name", System.getProperty("os.name"));
            BaseTest.getExtent().setSystemInfo("Java Version", System.getProperty("java.version"));
            BaseTest.getExtent().setSystemInfo("PlatformName", "Android");
            BaseTest.getExtent().setSystemInfo("AppName", "SBMBANK");
        } catch (Exception e) {
            logger.error("Failed to initialize ExtentReports: " + e.getMessage(), e);
            throw e;
        }
    }

    // Cache parent nodes (so multiple tests can reuse the same parent without duplicating the suite/scenario)
    private static final ConcurrentMap<String, ExtentTest> SUITES = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, ExtentTest> SCENARIOS = new ConcurrentHashMap<>();

    // context per thread (handles parallel="tests")
    private static final ThreadLocal<ExtentTest> CURRENT_SUITE 	= new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> CURRENT_SCENARIO 	= new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> CURRENT_STEP 	= new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> CURRENT_CNL3 	= new ThreadLocal<>();

    // Main test suite
    // Get/Create a top-level parent in the left pane
    // Example of main test suite "Account Transfer - Other SBM Transfer"
    public static ExtentTest suite(String suiteName) {
        ExtentTest suite = SUITES.computeIfAbsent(
                suiteName,
                name -> BaseTest.getExtent().createTest(name).assignCategory("SUITE")); // If suite name is not present in concurrent map, will create suite
        CURRENT_SUITE.set(suite);
        BaseTest.setTest(CURRENT_SUITE);
        return suite;
    }

    // Child node layer 1
    // Create a step node under the main test
    // Example: "Successful transfer - MUR to MUR"
    public static ExtentTest scenario(String scenarioName) {
        ExtentTest suite = CURRENT_SUITE.get();
        if (suite == null) {
            // defensive: create a default suite if suite has not been created
            suite = BaseTest.getExtent().createTest("Default Suite").assignCategory("SUITE");
            CURRENT_SUITE.set(suite);
        }

        // Creating a key per suite + scenario, so same scenario name under different suites is allowed
        String suiteName = suite.getModel().getName();
        String key = suiteName + "::" + scenarioName;

        final ExtentTest finalSuite = suite;

        ExtentTest scenario = SCENARIOS.computeIfAbsent(
                key,
                k -> finalSuite.createNode(scenarioName)
        );
        CURRENT_SCENARIO.set(scenario);
        BaseTest.setTest(CURRENT_SCENARIO);
        return scenario;
    }

    // Child node layer 2
    // Create a step node under the layer 1 child node
    // Example: "App Launch, Login, Navigation to.., ..., Logout"
    public static ExtentTest step(String stepName) {
        ExtentTest scenario = CURRENT_SCENARIO.get();
        if (scenario == null) {

            // If no suite exist, create suite
            ExtentTest suite = CURRENT_SUITE.get();
            if (suite == null) {
                suite = BaseTest.getExtent()
                        .createTest("Default Suite")
                        .assignCategory("SUITE");
                CURRENT_SUITE.set(suite);
            }
            // defensive case, if scenario null
            scenario = CURRENT_SUITE.get().createNode("Default scenario");
            CURRENT_SCENARIO.set(scenario);
        }

        ExtentTest step = scenario.createNode(stepName);
        CURRENT_STEP.set(step);
        BaseTest.setTest(CURRENT_STEP);
        return step;
    }

    // Child node layer 3
    // Create a step node under the layer 1 child node
    // No example yet, a template in case needed
    public static ExtentTest childNodeLayer3(String step3Name) {
        ExtentTest step = CURRENT_STEP.get();
        if (step == null) {
            // Ensure we have a scenario (and suite) first
            ExtentTest scenario = CURRENT_SCENARIO.get();
            if (scenario == null) {
                ExtentTest suite = CURRENT_SUITE.get();
                if (suite == null) {
                    suite = BaseTest.getExtent()
                            .createTest("Default Suite")
                            .assignCategory("SUITE");
                    CURRENT_SUITE.set(suite);
                }
                scenario = suite.createNode("Default scenario");
                CURRENT_SCENARIO.set(scenario);
            }
            step = scenario.createNode("Default step");
            CURRENT_STEP.set(step);
        }
        ExtentTest childNodeLayer3 = step.createNode(step3Name);
        CURRENT_CNL3.set(childNodeLayer3);
        BaseTest.setTest(CURRENT_CNL3);
        return childNodeLayer3;
    }

}