package utils;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import flows.TransferFlow;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TransferHelper {
    private static AppiumDriver driver;
    private static WebDriverWait wait;
    private static ExtentTest test;

    public TransferHelper(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ============ OLB Transfer Type ===============
    // In TransferObjects.java or TransferActions.java
    public static void handleLocalBankTransferType(Map<String, String> data) {

        try {
            // Acquire latest test node
            test = BaseTest.getTest().get();
            // --- Step 1: Get Transfer Type from Excel (column index 3) ---
            String TransferType = data.get("TransferType");
            System.out.println("TransferType from Excel: " + TransferType);
            test.info("TransferType from Excel: " + TransferType);

            // --- Step 3: Check if TransferType element is visible ---
            By transferTypeLocator = By.xpath("//android.view.View[contains(@text,'" + TransferType + "')]");

            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(transferTypeLocator));
                System.out.println(TransferType + " tab is visible on screen.");
                Thread.sleep(1000);
                // --- Step 4: Click on the TransferType ---
                WebElement typeElement = driver.findElement(transferTypeLocator);
                wait.until(ExpectedConditions.elementToBeClickable(typeElement)).click();
                System.out.println("✅ Selected Transfer Type: " + TransferType);
                test.info("✅ Selected Transfer Type: " + TransferType);
                Thread.sleep(3000);
                //                        // Handle if another popup occurs, when another transfer type is selected
                //                        if (!driver.findElements(By.xpath("//android.widget.Button[contains(@text,'Close')]")).isEmpty()) {
                //                            transferObjects.close().click();
                //                            System.out.println("ℹ️ Clicked on Close button...");
                //                            logger.info("ℹ️ Clicked on Close button...");
                //                            test.info("ℹ️ Clicked on Close button...");
                //                            Thread.sleep(500);
                //                        }
                if (!driver.findElements(By.xpath("//android.widget.Button[contains(@text,'Close')]")).isEmpty()) {
                    String popupText = driver.findElement(By.xpath("//android.view.View[contains(@resource-id,'alert-popup-container')]")).getText().replace("Close", "").trim();
                    System.out.println("⚠️ Popup message: " + popupText);
                    test.info("⚠️ Popup message: " + popupText);

                    // Click Close button if available
                    if (!driver.findElements(By.xpath("//android.widget.Button[contains(@text,'Close') or contains(@text,'OK')]")).isEmpty()) {
                        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'Close') or contains(@text,'OK')]")).click();
                        System.out.println("ℹ️ Closed popup successfully.");
                        test.info("ℹ️ Closed popup successfully.");
                    }
                }
                // --- Step 5: Handle popup if displayed after click ---
                try {
                    //                            WebElement popupMessage = driver.findElement(By.xpath(
                    //                                    "//android.widget.TextView[contains(@text,'Close') or " +
                    //                                            "contains(@text,'limit') or contains(@text,'not allowed') or " +
                    //                                            "contains(@text,'Alert')]"
                    //                            ));
                    //                        	wait.until(ExpectedConditions.elementToBeClickable(transferObjects.close())).click();

                    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(
                            "//android.widget.TextView[contains(@text,'Close')]"
                    )))).isDisplayed();
                    WebElement popupMessage = driver.findElement(By.xpath(
                            "//android.widget.TextView[contains(@text,'Close')]"
                    ));
                    if (popupMessage.isDisplayed()) {
                        String popupText = popupMessage.getText();
                        System.out.println("⚠️ Popup message: " + popupText);
                        test.warning("⚠️ Popup message: " + popupText);

                        // Click Close button if available
                        if (!driver.findElements(By.xpath("//android.widget.Button[contains(@text,'Close') or contains(@text,'OK')]")).isEmpty()) {
                            driver.findElement(By.xpath("//android.widget.Button[contains(@text,'Close') or contains(@text,'OK')]")).click();
                            System.out.println("ℹ️ Closed popup successfully.");
                            test.info("ℹ️ Closed popup successfully.");
                        }
                    }
                } catch (Exception e) {
                    // No popup found, continue normally
                }

            } catch (Exception e) {
                // Not visible → Scroll to top
                System.out.println(TransferType + " not visible, scrolling to top...");
                test.info(TransferType + " not visible, scrolling to top...");

                driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true)).scrollToBeginning(5);"
                ));

                wait.until(ExpectedConditions.visibilityOfElementLocated(transferTypeLocator));
                // --- Step 4: Click on the TransferType ---
                WebElement typeElement = driver.findElement(transferTypeLocator);
                wait.until(ExpectedConditions.elementToBeClickable(typeElement)).click();
                Thread.sleep(3000);

                WebElement rr = driver.findElement(By.xpath("//android.widget.Button[contains(@text,'Close')]"));
                if (rr.isDisplayed()) {
                    rr.click();
                    System.out.println("ℹ️ Clicked on Close button...");
                    test.info("ℹ️ Clicked on Close button...");
                    Thread.sleep(200);
                }
                System.out.println("✅ Selected Transfer Type: " + TransferType);
                test.info("✅ Selected Transfer Type: " + TransferType);
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            test.fail("❌ Error in handleLocalBankTransferType: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void selectDateFromCalendar(String excelDate) {

        // Parse date (adjust format if needed)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate targetDate = LocalDate.parse(excelDate, formatter);

        int targetDay = targetDate.getDayOfMonth();
        int targetMonth = targetDate.getMonthValue();
        int targetYear = targetDate.getYear();

        // 1. Open the calendar
        driver.findElement(By.xpath(
                "//android.widget.TextView[contains(@text,'Transaction Date')]/parent::android.view.View//android.view.View[contains(@resource-id,'BW_input')]"
        )).click();

        // 2. Open Month/Year Selector Popup
        driver.findElement(By.xpath("//android.view.View[contains(@resource-id,\"current-date\")]")).click();

        // 3. Select Year
        while (true) {
            String displayedYearText = driver.findElement(By.xpath(
                    "//android.view.View[@resource-id='prev-year']/parent::android.view.View/following-sibling::android.widget.TextView[1]"
            )).getText();

            int displayedYear = Integer.parseInt(displayedYearText);

            if (displayedYear == targetYear) {
                break;
            }

            if (displayedYear < targetYear) {
                driver.findElement(By.xpath("//android.view.View[contains(@resource-id,\"next-year\")]")).click();
            } else {
                driver.findElement(By.xpath("//android.view.View[contains(@resource-id,\"prev-year\")]")).click();
            }
        }

        // 4. Select Month
        String targetMonthShort =
                targetDate.getMonth().name().substring(0, 1).toUpperCase() +
                        targetDate.getMonth().name().substring(1, 3).toLowerCase();

        String monthButtonXpath = "//android.widget.TextView[@text='" + targetMonthShort + "']";

        driver.findElement(By.xpath(monthButtonXpath)).click();

        // 5. Select Day (universal selector)
        String dayXpath =
                "//android.view.View[@content-desc='" + targetDay + "'] | " +
                        "//android.widget.TextView[@text='" + targetDay + "']/parent::android.view.View";

        driver.findElement(By.xpath(dayXpath)).click();
        System.out.println("Day selected: " + targetDay);
    }
}
