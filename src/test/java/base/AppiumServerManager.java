package base;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;

public class AppiumServerManager {

    private static AppiumDriverLocalService service;

    public static void startServer() {
        if (service != null && service.isRunning()) {
            return;
        }

        // ⚠️ Adjust these paths to your machine
        String nodePath     = "C:\\Program Files\\nodejs\\node.exe";
        String appiumMainJs = "C:\\Users\\Khushbu Jowaheer\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js";

        String logDir = System.getProperty("user.dir") + File.separator + "appiumlogs";
        File logDirFile = new File(logDir);
        if (!logDirFile.exists()) logDirFile.mkdirs();

        File logFile = new File(logDirFile, "appium_log.txt");

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withAppiumJS(new File(appiumMainJs))
                .usingDriverExecutable(new File(nodePath))
                .withIPAddress("127.0.0.1")
                .usingPort(4723)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(() -> "--log-level", "debug")
                .withLogFile(logFile);

        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

        if (!service.isRunning()) {
            throw new RuntimeException("❌ Appium server failed to start");
        } else {
            System.out.println("✅ Appium server started on: " + service.getUrl());
        }
    }

    public static void stopServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("🛑 Appium server stopped");
        }
    }
}
