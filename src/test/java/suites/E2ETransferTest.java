package suites;

import base.BaseTest;
import dataprovider.ExcelFile;
import dataprovider.GenericDataProvider;
import flows.E2ETransferFlow;
import org.testng.annotations.Test;
import java.util.Map;

public class E2ETransferTest extends BaseTest {

    @ExcelFile(file = "/STD/OtherLocalBank.xlsx", sheet = "OtherLocalBank_UAT")
    @Test(dataProvider = "excelData", dataProviderClass = GenericDataProvider.class)
    public void runE2ETransfer(Map<String, String> data) throws Exception {

        E2ETransferFlow flow = new E2ETransferFlow();
        flow.execute(data);
    }
}
