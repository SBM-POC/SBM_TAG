package dataprovider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelFile {
    String file();   // e.g., "/STD/LoginData.xlsx"
    String sheet();  // e.g., "DEV"
}