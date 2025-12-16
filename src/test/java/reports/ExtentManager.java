package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports createInstance() {
        if (extent == null) {

            String reportPath = System.getProperty("user.dir") + "/Reports/ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setDocumentTitle("SBM TAG Automation Report");
            spark.config().setReportName("Mobile Automation Results");
            spark.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Platform", "Android");
            extent.setSystemInfo("Framework", "Appium + TestNG");
            extent.setSystemInfo("Author", "Yash");

        }
        return extent;
    }
}
