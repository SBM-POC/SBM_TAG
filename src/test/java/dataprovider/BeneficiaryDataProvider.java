package dataprovider;

import org.testng.annotations.DataProvider;
import utils.ExcelUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeneficiaryDataProvider {

    private static final String DEFAULT_FILE =
            System.getProperty("user.dir") + "/STD/MyBeneficiaries.xlsx";

    private static List<Map<String, String>> OTHER_SBM_ALL;
    private static List<Map<String, String>> OTHER_LOCAL_ALL;
    private static List<Map<String, String>> SWIFT_ALL;

    private static List<Map<String, String>> getOtherSbmAll() {
        if (OTHER_SBM_ALL == null) {
            OTHER_SBM_ALL = ExcelUtils.readSheetAsListOfMaps(
                    DEFAULT_FILE,
                    "OtherSBMTransfer"
            );
        }
        return OTHER_SBM_ALL;
    }

    private static List<Map<String, String>> getOtherLocalAll() {
        if (OTHER_LOCAL_ALL == null) {
            OTHER_LOCAL_ALL = ExcelUtils.readSheetAsListOfMaps(
                    DEFAULT_FILE,
                    "OtherLocalBank"
            );
        }
        return OTHER_LOCAL_ALL;
    }

    private static List<Map<String, String>> getSwiftAll() {
        if (SWIFT_ALL == null) {
            SWIFT_ALL = ExcelUtils.readSheetAsListOfMaps(
                    DEFAULT_FILE,
                    "SWIFT"
            );
        }
        return SWIFT_ALL;
    }

    // -------- SBM: Add / Edit / Pay / Delete (Action column) --------

    @DataProvider(name = "otherSbmAdd")
    public static Object[][] otherSbmAdd() {
        return toDataArray(
                filterByAction(getOtherSbmAll(), "Add")
        );
    }

    @DataProvider(name = "otherSbmEdit")
    public static Object[][] otherSbmEdit() {
        return toDataArray(
                filterByAction(getOtherSbmAll(), "Edit")
        );
    }

    @DataProvider(name = "otherSbmPay")
    public static Object[][] otherSbmPay() {
        return toDataArray(
                filterByAction(getOtherSbmAll(), "Pay")
        );
    }

    @DataProvider(name = "otherSbmDelete")
    public static Object[][] otherSbmDelete() {
        return toDataArray(
                filterByAction(getOtherSbmAll(), "Delete")
        );
    }

    // -------- Other Local Bank (usually Add only – adjust if needed) --------

    @DataProvider(name = "localBankAdd")
    public static Object[][] localBankAdd() {
        return toDataArray(
                filterByAction(getOtherLocalAll(), "Add")
        );
    }

    // -------- SWIFT (Add only for now) --------

    @DataProvider(name = "swiftBankAdd")
    public static Object[][] swiftBankAdd() {
        return toDataArray(
                filterByAction(getSwiftAll(), "Add")
        );
    }

    // -------- Helpers --------

    private static List<Map<String, String>> filterByAction(List<Map<String, String>> source,
                                                            String action) {
        if (source == null) return List.of();
        return source.stream()
                .filter(m -> action.equalsIgnoreCase(m.getOrDefault("Action", "")))
                .collect(Collectors.toList());
    }

    private static Object[][] toDataArray(List<Map<String, String>> list) {
        Object[][] data = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i);
        }
        return data;
    }
}
