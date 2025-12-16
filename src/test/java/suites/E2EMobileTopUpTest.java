package suites;

import dataprovider.ExcelFile;
import dataprovider.GenericDataProvider;
import flows.E2EPayToMobileFlow;
import org.testng.annotations.Test;

import java.util.Map;

public class E2EMobileTopUpTest {

    @ExcelFile(file = "", sheet = "")
    @Test(dataProvider = "excelData", dataProviderClass = GenericDataProvider.class)
    public void runMobileTopUpPositive(Map<String, String> data) throws Exception {
        new E2EPayToMobileFlow().execute(data);
    }

    @ExcelFile(file = "", sheet = "")
    @Test(dataProvider = "excelData", dataProviderClass = GenericDataProvider.class)
    public void runMobileTopUpNegative(Map<String, String> data) throws Exception {
        new E2EPayToMobileFlow().execute(data);
    }

}
