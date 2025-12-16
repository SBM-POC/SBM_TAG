package utils;

import base.BasePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.Map;

public class ExchangeRateUtils extends BasePage {

    // Get BUY rate for currency
    public double getBuyRate(String currency) {
        String xpath = "//android.widget.TextView[@text='" + currency + "']/following-sibling::android.widget.TextView[1]";
        WebElement rateElement = driver.findElement(AppiumBy.xpath(xpath));
        return Double.parseDouble(rateElement.getText());
    }

    // Get SELL rate for currency
    public double getSellRate(String currency) {
        String xpath = "//android.widget.TextView[@text='" + currency + "']/following-sibling::android.widget.TextView[2]";
        WebElement rateElement = driver.findElement(AppiumBy.xpath(xpath));
        return Double.parseDouble(rateElement.getText());
    }

    // MUR → FCY
    public double convertMURtoFCY(double murAmount, double sellRate) {
        return murAmount / sellRate;
    }

    // FCY → MUR
    public double convertFCYtoMUR(double fcyAmount, double buyRate) {
        return fcyAmount * buyRate;
    }

    // FCY → FCY Cross rate
    public double convertFCYtoFCY(double amount, double buyRateSource, double sellRateTarget) {
        double crossRate = buyRateSource / sellRateTarget;
        return amount * crossRate;
    }

    public void validateExchangeRate(Map<String, String> data) {

        ExchangeRateUtils fx = new ExchangeRateUtils();

        String from = data.get("FromCurrency");  // e.g. MUR or AUD
        String to = data.get("ToCurrency");      // e.g. AUD or MUR
        double amount = Double.parseDouble(data.get("Amount"));

        double expectedRate = 0.0;

        // MUR → FCY
        if (from.equals("MUR") && !to.equals("MUR")) {
            expectedRate = fx.getSellRate(to);
        }

        // FCY → MUR
        else if (!from.equals("MUR") && to.equals("MUR")) {
            expectedRate = fx.getBuyRate(from);
        }

        // FCY → FCY (Cross rate)
        else if (!from.equals("MUR") && !to.equals("MUR")) {
            double buySource = fx.getBuyRate(from);
            double sellTarget = fx.getSellRate(to);
            expectedRate = buySource / sellTarget;
        }

        // Get actual rate displayed on confirmation screen
        String actualRateText = getText("//android.widget.TextView[contains(@text,'Exchange Rate')]/following-sibling::android.widget.TextView");
        double actualRate = Double.parseDouble(actualRateText);

        // Validate
        Assert.assertEquals(actualRate, expectedRate, 0.01,
                "Exchange Rate mismatch! Expected: " + expectedRate + " Actual: " + actualRate);
    }
}
