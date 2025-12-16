package utils;

import base.DriverFactory;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TestUtils {

    // Default explicit wait time (seconds)
    public static final long WAIT = 10;

    // ---------------------------------------------------------
    //  XML PARSER
    // ---------------------------------------------------------
    /**
     * Reads test data from XML file.
     * Format expected:
     * <TestData>
     *     <String name="username">abc</String>
     * </TestData>
     */
    public HashMap<String, String> parseStringXML(InputStream file) throws Exception {

        HashMap<String, String> map = new HashMap<>();

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();

        NodeList list = document.getElementsByTagName("String");

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;
                map.put(e.getAttribute("name"), e.getTextContent().trim());
            }
        }

        return map;
    }

    // ---------------------------------------------------------
    //  TIMESTAMP
    // ---------------------------------------------------------
    public String getDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
    }

    // ---------------------------------------------------------
    //  SCREENSHOT
    // ---------------------------------------------------------
    /**
     * Takes a screenshot and returns the absolute file path.
     */
    public String takeScreenshot(String name) {
        AppiumDriver driver = DriverFactory.getDriver();

        if (driver == null) {
            System.out.println("❌ Cannot take screenshot: driver is null");
            return null;
        }

        try {
            String folder = System.getProperty("user.dir") + File.separator + "Screenshots";
            Files.createDirectories(Paths.get(folder));

            String filename = name + "_" + getDateTime() + ".png";
            String path = folder + File.separator + filename;

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(path));

            System.out.println("📸 Screenshot saved: " + path);
            return path;

        } catch (Exception e) {
            System.out.println("❌ Screenshot failed: " + e.getMessage());
            return null;
        }
    }

    // ---------------------------------------------------------
    //  SIMPLE SLEEP
    // ---------------------------------------------------------
    public static void sleep(long millis) {
        try { Thread.sleep(millis); }
        catch (InterruptedException ignored) {}
    }

    // ---------------------------------------------------------
    //  OPTIONAL UTILITY placeholder (your original)
    // ---------------------------------------------------------
    public static class UtilityMethods {
        public static boolean checkImageScaling(WebElement element, int expectedWidth, int expectedHeight) {
            // Placeholder — implement if needed
            return true;
        }
    }
}
