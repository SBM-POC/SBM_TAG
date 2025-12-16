package dataprovider;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.util.*;

public class TransferDataProvider {

    private static final String BASE_PATH =
            System.getProperty("user.dir") + "/src/test/resources/data/";

    // ================================
    // OWN ACCOUNT TRANSFER
    // ================================
    @DataProvider(name = "ownAccount")
    public Object[][] ownAccountData() throws Exception {
        return readToDataProvider("OwnAccountTransfer.xlsx");
    }

    // ================================
    // OTHER SBM TRANSFER
    // ================================
    @DataProvider(name = "otherSbm")
    public Object[][] otherSbmData() throws Exception {
        return readToDataProvider("dataset_STP-139_OtherSBMTransfer_PreProd_sameDay.xlsx");
    }

    // ================================
    // OTHER LOCAL BANK TRANSFER
    // ================================
    @DataProvider(name = "localBank")
    public Object[][] localBankData() throws Exception {
        return readToDataProvider("OtherLocalBankTransfer.xlsx"); // Update when you give me the file
    }

    // ================================
    // SWIFT TRANSFER
    // ================================
    @DataProvider(name = "swift")
    public Object[][] swiftData() throws Exception {
        return readToDataProvider("dataset_STP-264_SWIFT Transfer_PreProd.xlsx");
    }

    // ========================================================================
    // Converts Excel → Object[][] for TestNG
    // ========================================================================
    private Object[][] readToDataProvider(String fileName) throws Exception {

        List<Map<String, String>> rows = readExcel(BASE_PATH + fileName);

        Object[][] dp = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            dp[i][0] = rows.get(i);
        }
        return dp;
    }

    // ========================================================================
    // Read Excel → List<Map<String, String>>
    // ========================================================================
    private List<Map<String, String>> readExcel(String filePath) throws Exception {

        List<Map<String, String>> dataset = new ArrayList<>();

        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();

        // Header row
        Row headerRow = rowIterator.next();
        List<String> headers = new ArrayList<>();
        headerRow.forEach(cell -> headers.add(cell.toString().trim()));

        // Data rows
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Map<String, String> map = new LinkedHashMap<>();

            for (int i = 0; i < headers.size(); i++) {
                Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                map.put(headers.get(i), getCellValue(cell));
            }

            dataset.add(map);
        }

        workbook.close();
        return dataset;
    }

    // ========================================================================
    // Safe Cell → String conversion
    // ========================================================================
    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {

            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue()).replace(".0", "").trim();

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                return cell.getCellFormula();

            default:
                return "";
        }
    }
}
