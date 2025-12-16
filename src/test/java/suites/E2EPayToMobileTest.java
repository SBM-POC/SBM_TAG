package suites;

import base.BaseTest;
import dataprovider.ExcelFile;
import dataprovider.GenericDataProvider;
import flows.E2EPayToMobileFlow;
import org.testng.annotations.Test;

import java.util.Map;

public class E2EPayToMobileTest extends BaseTest {
    @ExcelFile(file = "", sheet = "")
    @Test(dataProvider = "excelData", dataProviderClass = GenericDataProvider.class)
    public void runPayToMobilePositive(Map<String, String> data) throws Exception {
        new E2EPayToMobileFlow().execute(data);
    }

    @ExcelFile(file = "", sheet = "")
    @Test(dataProvider = "excelData", dataProviderClass = GenericDataProvider.class)
    public void runPayToMobileNegative(Map<String, String> data) throws Exception {
        new E2EPayToMobileFlow().execute(data);
    }
}

