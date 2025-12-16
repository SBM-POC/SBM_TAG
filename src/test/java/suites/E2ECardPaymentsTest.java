package suites;

import base.BaseTest;
import dataprovider.ExcelFile;
import dataprovider.GenericDataProvider;
import flows.E2ECardPaymentsFlow;
import org.testng.annotations.Test;

import java.util.Map;

public class E2ECardPaymentsTest extends BaseTest {
    @ExcelFile(file = "/STD/TestData_RechargePrepaidCard - Test.xlsx", sheet = "Positive")
    @Test(dataProvider = "excelData", dataProviderClass = GenericDataProvider.class)
    public void runE2ECardPaymentsPositive(Map<String, String> data) throws Exception {

        E2ECardPaymentsFlow flow = new E2ECardPaymentsFlow();
        flow.runRMPC(data);
    }

//    @ExcelFile(file = "/STD/TestData_RechargePrepaidCard - Test.xlsx", sheet = "Negative")
//    @Test(dataProvider = "excelData", dataProviderClass = GenericDataProvider.class)
//    public void runE2ECardPaymentsNegative(Map<String, String> data) throws Exception {
//
//        E2ECardPaymentsFlow flow = new E2ECardPaymentsFlow();
//        flow.runRMPCNegative(data);
//    }
}
