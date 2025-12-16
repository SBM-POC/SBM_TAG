package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelUtils {

    /**
     * Reads a sheet and returns each row as Map<ColumnName, Value>.
     * Assumes row 0 is header.
     */

    public static List<Map<String, String>> readSheetAsListOfMaps(String path, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(path);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in file: " + path);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row (0) is missing in sheet: " + sheetName);
            }

            int lastCol = headerRow.getLastCellNum();
            List<String> headers = new ArrayList<>();

            // Read header names
            for (int c = 0; c < lastCol; c++) {
                Cell cell = headerRow.getCell(c);
                String headerName = (cell != null) ? cell.getStringCellValue().trim() : ("Column" + c);
                headers.add(headerName);
            }

            // Read data rows
            int lastRow = sheet.getLastRowNum();
            for (int r = 1; r <= lastRow; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Map<String, String> rowMap = new LinkedHashMap<>();

                for (int c = 0; c < headers.size(); c++) {
                    String colName = headers.get(c);
                    String value = getCellAsString(row, c);
                    rowMap.put(colName, value);
                }

                data.add(rowMap);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + path, e);
        }

        return data;
    }

    /**
     * Convenience method to read a single cell by column name + row number (1-based).
     */
    public static String readCell(String path, String sheetName, String columnName, int rowNumber) {
        try (FileInputStream fis = new FileInputStream(path);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in file: " + path);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row (0) is missing in sheet: " + sheetName);
            }

            int colIndex = -1;
            for (Cell cell : headerRow) {
                if (cell != null && columnName.equalsIgnoreCase(cell.getStringCellValue().trim())) {
                    colIndex = cell.getColumnIndex();
                    break;
                }
            }

            if (colIndex == -1) {
                throw new IllegalArgumentException("Column name not found: " + columnName);
            }

            Row dataRow = sheet.getRow(rowNumber - 1); // 1-based to 0-based
            if (dataRow == null) return "";

            return getCellAsString(dataRow, colIndex);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + path, e);
        }
    }

    private static String getCellAsString(Row row, int idx) {
        Cell cell = row.getCell(idx);
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                double val = cell.getNumericCellValue();
                return (val == (int) val) ? String.valueOf((int) val) : String.valueOf(val);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // Evaluate as string or numeric, depending on use-case. For now:
                return cell.getCellFormula();
            case BLANK:
            default:
                return "";
        }
    }
}
