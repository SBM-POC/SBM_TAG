package suites;

import base.BaseTest;
import dataprovider.ExcelFile;
import dataprovider.GenericDataProvider;
import flows.E2EBeneficiaryFlow;
import org.testng.annotations.Test;

import java.util.Map;

public class E2EBeneficiarySWIFTTest extends BaseTest {

    @ExcelFile(file = "./STD/dataset_STP-325_MyBeneficiaries.xlsx", sheet = "SWIFT-PreProd")
    @Test(dataProvider = "excelData", dataProviderClass = GenericDataProvider.class)
    public void runSwiftBeneficiary(Map<String, String> data) throws Exception {
        new E2EBeneficiaryFlow().execute(data, "SWIFT");
    }
}
