package listeners;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentTest;
import org.testng.*;
import base.BaseTest;
import reports.ExtentManager;

public class ExtentTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
//        ExtentTest extentTest = BaseTest.getExtent()
//                .createTest(result.getMethod().getMethodName());
//
//        BaseTest.getTest().set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        BaseTest.getTest().get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        BaseTest.getTest().get().log(Status.FAIL, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        BaseTest.getTest().get().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        BaseTest.getExtent().flush();
    }
}
