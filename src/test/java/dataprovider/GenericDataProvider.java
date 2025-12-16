package dataprovider;

import org.testng.annotations.DataProvider;
import utils.ExcelUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


public class GenericDataProvider {

    @DataProvider(name = "excelData")
    public static Object[][] excelData(Method method) {

        // Fetch @ExcelFile annotation on the test method
        ExcelFile meta = method.getAnnotation(ExcelFile.class);

        if (meta == null) {
            throw new RuntimeException(
                    "❌ Missing @ExcelFile annotation on test method: " + method.getName()
            );
        }

        String path = System.getProperty("user.dir") + meta.file();
        String sheet = meta.sheet();

        List<Map<String, String>> rows = ExcelUtils.readSheetAsListOfMaps(path, sheet);

        Object[][] data = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i);
        }

        return data;
    }


}
