package dataprovider;

import org.testng.annotations.DataProvider;
import utils.ExcelUtils;

import java.util.List;
import java.util.Map;

public class LoginDataProvider {

    // Cache users so we only read Excel once
    private static List<Map<String, String>> USERS;

    @DataProvider(name = "loginUsers")
    public static Object[][] loginUsers() {

        if (USERS == null) {
            String path = System.getProperty("user.dir") + "/STD/Untitled.xlsx";

            // Get ENV, same idea as old BaseClass.environment
            String env = System.getProperty("environment", "DEV").toUpperCase();
            USERS = ExcelUtils.readSheetAsListOfMaps(path, env);
        }

        Object[][] data = new Object[USERS.size()][1];

        for (int i = 0; i < USERS.size(); i++) {
            data[i][0] = USERS.get(i);   // each row = Map<String,String>
        }

        return data;
    }

    // Helper for manual access (multi-user flows)
    public static Map<String, String> getUser(int index) {
        if (USERS == null) {
            String path = System.getProperty("user.dir") + "/STD/Untitled.xlsx";
            String env = System.getProperty("environment", "DEV").toUpperCase();
            USERS = ExcelUtils.readSheetAsListOfMaps(path, env);
        }
        return USERS.get(index);
    }

    public static int getUserCount() {
        if (USERS == null) {
            String path = System.getProperty("user.dir") + "/TestData/login.xlsx";
            String env = System.getProperty("environment", "DEV").toUpperCase();
            USERS = ExcelUtils.readSheetAsListOfMaps(path, env);
        }
        return USERS.size();
    }
}
