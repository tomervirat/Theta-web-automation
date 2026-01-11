package com.crypto.utils.wrappers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.crypto.constants.Constants;
import com.crypto.utils.ContextManager;

/**
 * Utility class for capturing and managing screenshots
 */
public class ScreenshotUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOTS_DIR = Constants.CURRENT_DIR + "/screenshots/";

    /**
     * Takes a screenshot and saves it to the screenshots directory
     * 
     * @param driver   WebDriver instance
     * @param fileName Name of the screenshot file (without extension)
     * @return Path to the saved screenshot file, or null if failed
     */
    public static String takeScreenshot(WebDriver driver, String fileName) {
        if (driver == null) {
            LOGGER.error("Cannot take screenshot: WebDriver is null");
            return null;
        }

        try {
            // Ensure screenshots directory exists
            createScreenshotsDirectory();

            // Generate unique filename with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String screenshotFileName = sanitizeFileName(fileName) + "_" + timestamp + ".png";
            String screenshotPath = SCREENSHOTS_DIR + screenshotFileName;

            // Take screenshot
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destPath = Paths.get(screenshotPath);
            Files.copy(srcFile.toPath(), destPath);

            LOGGER.info("Screenshot saved: {}", screenshotPath);
            return screenshotPath;

        } catch (IOException e) {
            LOGGER.error("Failed to save screenshot: {}", e.getMessage(), e);
            return null;
        } catch (Exception e) {
            LOGGER.error("Error taking screenshot: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Takes a screenshot and returns it as a Base64 encoded string
     * 
     * @param driver WebDriver instance
     * @return Base64 encoded screenshot string, or null if failed
     */
    public static String takeScreenshotAsBase64(WebDriver driver) {
        if (driver == null) {
            LOGGER.error("Cannot take screenshot: WebDriver is null");
            return null;
        }

        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            LOGGER.error("Error taking Base64 screenshot: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Takes a screenshot using the current thread's WebDriver from ContextManager
     * 
     * @param fileName Name of the screenshot file (without extension)
     * @return Path to the saved screenshot file, or null if failed
     */
    public static String takeScreenshot(String fileName) {
        WebDriver driver = ContextManager.getDriver();
        return takeScreenshot(driver, fileName);
    }

    /**
     * Takes a screenshot and attaches it to the Extent Report
     * 
     * @param driver  WebDriver instance
     * @param message Message to display with the screenshot
     */
    public static void captureAndAttachScreenshot(WebDriver driver, String message) {
        ExtentTest test = ContextManager.getExtentTest();
        if (test == null) {
            LOGGER.warn("Cannot attach screenshot: ExtentTest is null");
            return;
        }

        try {
            String base64Screenshot = takeScreenshotAsBase64(driver);
            if (base64Screenshot != null) {
                test.info(message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                LOGGER.info("Screenshot attached to report: {}", message);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to attach screenshot to report: {}", e.getMessage(), e);
        }
    }

    /**
     * Takes a failure screenshot and attaches it to the Extent Report with FAIL
     * status
     * 
     * @param driver    WebDriver instance
     * @param testName  Name of the failed test
     * @param throwable The exception/error that caused the failure
     */
    public static void captureFailureScreenshot(WebDriver driver, String testName, Throwable throwable) {
        ExtentTest test = ContextManager.getExtentTest();
        if (test == null) {
            LOGGER.warn("Cannot attach failure screenshot: ExtentTest is null");
            return;
        }

        try {
            // Save screenshot to file
            String screenshotPath = takeScreenshot(driver, "FAILURE_" + testName);

            // Get Base64 for embedding in report
            String base64Screenshot = takeScreenshotAsBase64(driver);

            if (base64Screenshot != null) {
                // Attach screenshot with failure message
                test.fail("Failure Screenshot",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                LOGGER.info("Failure screenshot attached to report for test: {}", testName);

                if (screenshotPath != null) {
                    test.info("Screenshot saved at: " + screenshotPath);
                }
            } else {
                LOGGER.warn("Could not capture failure screenshot for test: {}", testName);
            }

        } catch (Exception e) {
            LOGGER.error("Failed to capture failure screenshot for test {}: {}", testName, e.getMessage(), e);
        }
    }

    /**
     * Takes a failure screenshot using the current thread's WebDriver
     * 
     * @param testName  Name of the failed test
     * @param throwable The exception/error that caused the failure
     */
    public static void captureFailureScreenshot(String testName, Throwable throwable) {
        WebDriver driver = ContextManager.getDriver();
        captureFailureScreenshot(driver, testName, throwable);
    }

    /**
     * Creates the screenshots directory if it doesn't exist
     */
    private static void createScreenshotsDirectory() {
        try {
            Path screenshotsPath = Paths.get(SCREENSHOTS_DIR);
            if (!Files.exists(screenshotsPath)) {
                Files.createDirectories(screenshotsPath);
                LOGGER.info("Created screenshots directory: {}", SCREENSHOTS_DIR);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to create screenshots directory: {}", e.getMessage(), e);
        }
    }

    /**
     * Sanitizes a filename by removing/replacing invalid characters
     * 
     * @param fileName Original filename
     * @return Sanitized filename
     */
    private static String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "screenshot";
        }
        // Replace invalid characters with underscores
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    /**
     * Gets the screenshots directory path
     * 
     * @return Screenshots directory path
     */
    public static String getScreenshotsDirectory() {
        return SCREENSHOTS_DIR;
    }

    /**
     * Cleans up old screenshots (older than specified days)
     * 
     * @param daysOld Number of days after which screenshots should be deleted
     */
    public static void cleanupOldScreenshots(int daysOld) {
        try {
            Path screenshotsPath = Paths.get(SCREENSHOTS_DIR);
            if (!Files.exists(screenshotsPath)) {
                return;
            }

            long cutoffTime = System.currentTimeMillis() - (daysOld * 24L * 60L * 60L * 1000L);

            Files.list(screenshotsPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".png"))
                    .forEach(path -> {
                        try {
                            if (Files.getLastModifiedTime(path).toMillis() < cutoffTime) {
                                Files.delete(path);
                                LOGGER.debug("Deleted old screenshot: {}", path);
                            }
                        } catch (IOException e) {
                            LOGGER.warn("Failed to delete old screenshot {}: {}", path, e.getMessage());
                        }
                    });

            LOGGER.info("Cleaned up screenshots older than {} days", daysOld);

        } catch (IOException e) {
            LOGGER.error("Error during screenshot cleanup: {}", e.getMessage(), e);
        }
    }
}
