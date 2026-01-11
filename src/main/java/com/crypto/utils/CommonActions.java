package com.crypto.utils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crypto.constants.Constants;
import com.crypto.utils.wrappers.ScreenshotUtils;

public class CommonActions {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonActions.class);
    private WebDriver driver;
    private WebDriverWait wait;
    private FluentWait<WebDriver> fluentWait;
    private JavascriptExecutor jsExecutor;
    private Actions actions;

    public CommonActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.EXPLICIT_WAIT));
        this.fluentWait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(Constants.EXPLICIT_WAIT))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
        this.actions = new Actions(driver);
    }

    public void openUrl(String url) {
        try {
            LOGGER.info("Opening URL: {}", url);

            if (driver == null) {
                throw new IllegalStateException("WebDriver is null. Cannot open URL.");
            }

            if (url == null || url.trim().isEmpty()) {
                throw new IllegalArgumentException("URL cannot be null or empty");
            }

            driver.get(url);

            // Wait for page to load
            waitForPageLoad(Constants.PAGE_LOAD_TIMEOUT);

            LOGGER.info("URL opened successfully: {}", url);
        } catch (TimeoutException e) {
            // Catch TimeoutException first (more specific)
            String errorMsg = String.format("Timeout while opening URL: %s. Page did not load within %d seconds.",
                    url, Constants.PAGE_LOAD_TIMEOUT);
            LOGGER.error(errorMsg, e);
            captureErrorScreenshot("openUrl_timeout_" + sanitizeUrl(url));
            throw new RuntimeException(errorMsg, e);
        } catch (WebDriverException e) {
            // Handle connection errors specifically (less specific, but still specific)
            String errorMessage = e.getMessage();
            String userFriendlyMessage = getConnectionErrorMessage(url, errorMessage);

            LOGGER.error("Failed to open URL: {}. Error: {}", url, errorMessage, e);
            captureErrorScreenshot("openUrl_connection_error_" + sanitizeUrl(url));

            throw new RuntimeException(userFriendlyMessage, e);
        } catch (Exception e) {
            // Catch all other exceptions (most general)
            String errorMsg = String.format("Unexpected error while opening URL: %s", url);
            LOGGER.error(errorMsg, e);
            captureErrorScreenshot("openUrl_unexpected_" + sanitizeUrl(url));
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * Sanitizes URL for use in filenames
     */
    private String sanitizeUrl(String url) {
        if (url == null) {
            return "unknown";
        }
        return url.replaceAll("[^a-zA-Z0-9]", "_")
                .replaceAll("_{2,}", "_")
                .substring(0, Math.min(50, url.length()));
    }

    /**
     * Captures a screenshot when an error occurs
     */
    private void captureErrorScreenshot(String actionName) {
        try {
            if (driver != null) {
                ScreenshotUtils.takeScreenshot(driver, "ERROR_" + actionName);
                LOGGER.info("Error screenshot captured for: {}", actionName);

                ScreenshotUtils.captureAndAttachScreenshot(
                        driver,
                        "Error screenshot captured during: " + actionName);
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to capture error screenshot: {}", e.getMessage());
        }
    }

    /**
     * Waits for page to be ready with custom timeout
     * 
     * @param timeoutSeconds Timeout in seconds
     * @return true if page loaded, false if timeout
     */
    @SuppressWarnings("null")
    public boolean waitForPageLoad(int timeoutSeconds) {
        try {
            wait.until(webDriver -> {
                String state = ((org.openqa.selenium.JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").toString();
                return state.equals("complete");
            });
            return true;
        } catch (TimeoutException e) {
            LOGGER.warn("Page did not load within {} seconds", timeoutSeconds);
            return false;
        } catch (Exception e) {
            LOGGER.warn("Error waiting for page load: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Provides user-friendly error messages for connection errors
     */
    private String getConnectionErrorMessage(String url, String errorMessage) {
        if (errorMessage == null) {
            return String.format("Connection error while opening URL: %s", url);
        }

        String lowerError = errorMessage.toLowerCase();

        if (lowerError.contains("err_connection_refused")) {
            return String.format(
                    "Connection Refused: Cannot connect to %s. " +
                            "Possible reasons:\n" +
                            "  1. The server is not running\n" +
                            "  2. The URL is incorrect\n" +
                            "  3. Firewall is blocking the connection\n" +
                            "  4. The port is not accessible\n" +
                            "Please verify the server is running and the URL is correct.",
                    url);
        } else if (lowerError.contains("err_name_not_resolved") || lowerError.contains("name not resolved")) {
            return String.format(
                    "DNS Error: Cannot resolve hostname for URL: %s. " +
                            "The domain name does not exist or cannot be resolved.",
                    url);
        } else if (lowerError.contains("err_timed_out") || lowerError.contains("timeout")) {
            return String.format(
                    "Connection Timeout: The server at %s did not respond in time. " +
                            "The server may be slow or unreachable.",
                    url);
        } else if (lowerError.contains("err_connection_reset")) {
            return String.format(
                    "Connection Reset: The connection to %s was reset by the server.",
                    url);
        } else if (lowerError.contains("err_network_changed")) {
            return String.format(
                    "Network Error: Network configuration changed while connecting to %s.",
                    url);
        } else if (lowerError.contains("err_internet_disconnected")) {
            return String.format(
                    "No Internet Connection: Cannot connect to %s. Please check your internet connection.",
                    url);
        } else if (lowerError.contains("err_ssl") || lowerError.contains("certificate")) {
            return String.format(
                    "SSL/Certificate Error: There is a problem with the SSL certificate for %s. " +
                            "The connection may be insecure or the certificate is invalid.",
                    url);
        } else {
            return String.format(
                    "Connection Error: Failed to open URL: %s. " +
                            "Error details: %s",
                    url, errorMessage);
        }
    }

    public void closeTab() {
        LOGGER.info("Closing tab");
        driver.close();
        LOGGER.info("Tab closed successfully");
    }

    public void closeBrowser() {
        LOGGER.info("Closing browser");
        driver.quit();
        LOGGER.info("Browser closed successfully");
    }

    public String getCurrentUrl() {
        LOGGER.info("Getting current URL");
        String currentUrl = driver.getCurrentUrl();
        LOGGER.info("Current URL: {}", currentUrl);
        return currentUrl;
    }

    public String getPageTitle() {
        LOGGER.info("Getting page title");
        String pageTitle = driver.getTitle();
        LOGGER.info("Page title: {}", pageTitle);
        return pageTitle;
    }

    public String getPageSource() {
        LOGGER.info("Getting page source");
        String pageSource = driver.getPageSource();
        LOGGER.info("Page source: {}", pageSource);
        return pageSource;
    }

    public void refreshPage() {
        LOGGER.info("Refreshing page");
        driver.navigate().refresh();
        LOGGER.info("Page refreshed successfully");
    }

    public void navigateBack() {
        LOGGER.info("Navigating back");
        driver.navigate().back();
        LOGGER.info("Navigated back successfully");
    }

    public void navigateForward() {
        LOGGER.info("Navigating forward");
        driver.navigate().forward();
        LOGGER.info("Navigated forward successfully");
    }

    public void maximizeWindow() {
        LOGGER.info("Maximizing window");
        driver.manage().window().maximize();
        LOGGER.info("Window maximized successfully");
    }

    public void minimizeWindow() {
        LOGGER.info("Minimizing window");
        driver.manage().window().minimize();
        LOGGER.info("Window minimized successfully");
    }

    public void setWindowSize(int width, int height) {
        LOGGER.info("Setting window size to {}x{}", width, height);
        driver.manage().window().setSize(new Dimension(width, height));
        LOGGER.info("Window size set successfully");
    }

    public void setWindowPosition(int x, int y) {
        LOGGER.info("Setting window position to {}x{}", x, y);
        driver.manage().window().setPosition(new Point(x, y));
        LOGGER.info("Window position set successfully");
    }

    public Map<String, Object> getWindowPosition() {
        LOGGER.info("Getting window position");
        Map<String, Object> windowPosition = new HashMap<>();
        windowPosition.put("x", driver.manage().window().getPosition().getX());
        windowPosition.put("y", driver.manage().window().getPosition().getY());
        LOGGER.info("Window position: {}", windowPosition);
        return windowPosition;
    }

    public Map<String, Object> getWindowSize() {
        LOGGER.info("Getting window size");
        Map<String, Object> windowSize = new HashMap<>();
        windowSize.put("width", driver.manage().window().getSize().getWidth());
        windowSize.put("height", driver.manage().window().getSize().getHeight());
        LOGGER.info("Window size: {}", windowSize);
        return windowSize;
    }

    public WebElement findElement(String xpath, String elementName) {
        LOGGER.info("Finding element by xpath: {}", xpath);
        WebElement element = driver.findElement(By.xpath(xpath));
        LOGGER.info("Element {} found: {}", elementName, element);
        return element;
    }

    public WebElement findElement(By locator, String elementName) {
        LOGGER.info("Finding element");
        WebElement element = driver.findElement(locator);
        LOGGER.info("Element {} found: {}", elementName, element);
        return element;
    }

    public List<WebElement> findElements(String xpath, String elementName) {
        LOGGER.info("Finding elements by xpath: {}", xpath);
        List<WebElement> elements = driver.findElements(By.xpath(xpath));
        LOGGER.info("Elements {} found: {}", elementName, elements);
        return elements;
    }

    public List<WebElement> findElements(By locator, String elementName) {
        LOGGER.info("Finding elements");
        List<WebElement> elements = driver.findElements(locator);
        LOGGER.info("Elements {} found: {}", elementName, elements);
        return elements;
    }

    @SuppressWarnings("null")
    public boolean isElementDisplayed(WebElement element, String elementName) {
        LOGGER.info("Checking if element is visible with wait");
        boolean isVisible;
        try {
            isVisible = wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (TimeoutException e) {
            LOGGER.warn("Element is not visible: {}", e.getMessage());
            return false;
        }
        LOGGER.info("Element {} is visible: {}", elementName, isVisible);
        return isVisible;
    }

    public boolean isElementDisplayed(By locator, String elementName) {
        LOGGER.info("Checking if element is visible with wait");
        boolean isVisible;
        try {
            isVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not visible: {}", elementName, e.getMessage());
            return false;
        }
        LOGGER.info("Element {} is visible: {}", elementName, isVisible);
        return isVisible;
    }

    public boolean isElementDisplayedWithFluentWait(WebElement element, String elementName) {
        LOGGER.info("Checking if element is visible with fluent wait");
        boolean isVisible;
        try {
            isVisible = fluentWait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not visible: {}", elementName, e.getMessage());
            return false;
        }
        LOGGER.info("Element {} is visible: {}", elementName, isVisible);
        return isVisible;
    }

    public boolean isElementDisplayedWithFluentWait(By locator, String elementName) {
        LOGGER.info("Checking if element is visible with fluent wait");
        boolean isVisible;
        try {
            isVisible = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not visible: {}", elementName, e.getMessage());
            return false;
        }
        LOGGER.info("Element {} is visible: {}", elementName, isVisible);
        return isVisible;
    }

    public boolean areElementsDisplayed(WebElement element, String elementName) {
        LOGGER.info("Checking if elements are visible");
        boolean areVisible;
        try {
            areVisible = wait.until(ExpectedConditions.visibilityOfAllElements(element)).size() > 0;
        } catch (TimeoutException e) {
            LOGGER.warn("Elements are not visible: {}", e.getMessage());
            return false;
        }
        LOGGER.info("Elements are visible: {}", areVisible);
        return areVisible;
    }

    public boolean isElementClickable(WebElement element, String elementName) {
        LOGGER.info("Checking if element is clickable");
        boolean isClickable;
        try {
            isClickable = wait.until(ExpectedConditions.elementToBeClickable(element)).isDisplayed();
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not clickable: {}", elementName, e.getMessage());
            return false;
        }
        LOGGER.info("Element {} is clickable: {}", elementName, isClickable);
        return isClickable;
    }

    public boolean isElementClickable(By locator, String elementName) {
        LOGGER.info("Checking if element is clickable");
        boolean isClickable;
        try {
            isClickable = wait.until(ExpectedConditions.elementToBeClickable(locator)).isDisplayed();
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not clickable: {}", elementName, e.getMessage());
            return false;
        }
        LOGGER.info("Element {} is clickable: {}", elementName, isClickable);
        return isClickable;
    }

    public boolean isElementClickableWithWait(WebElement element, String elementName) {
        LOGGER.info("Checking if element is clickable with wait");
        boolean isClickable;
        try {
            isClickable = wait.until(ExpectedConditions.elementToBeClickable(element)).isDisplayed();
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not clickable: {}", elementName, e.getMessage());
            return false;
        }
        LOGGER.info("Element {} is clickable: {}", elementName, isClickable);
        return isClickable;
    }

    public boolean isElementClickableWithWait(By locator, String elementName) {
        LOGGER.info("Checking if element is clickable with wait");
        boolean isClickable;
        try {
            isClickable = wait.until(ExpectedConditions.elementToBeClickable(locator)).isDisplayed();
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not clickable: {}", elementName, e.getMessage());
            return false;
        }
        LOGGER.info("Element {} is clickable: {}", elementName, isClickable);
        return isClickable;
    }

    public boolean isElementClickableWithFluentWait(By locator, String elementName) {
        LOGGER.info("Checking if element is clickable with wait");
        boolean isClickable;
        try {
            isClickable = wait.until(ExpectedConditions.elementToBeClickable(locator)).isDisplayed();
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not clickable: {}", elementName, e.getMessage());
            return false;
        }
        LOGGER.info("Element {} is clickable: {}", elementName, isClickable);
        return isClickable;
    }

    public boolean isElementClickableWithFluentWait(WebElement element, String elementName) {
        LOGGER.info("Checking if element is clickable with fluent wait");
        boolean isClickable;
        try {
            isClickable = fluentWait.until(ExpectedConditions.elementToBeClickable(element)).isDisplayed();
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not clickable: {}", elementName, e.getMessage());
            return false;
        }
        LOGGER.info("Element {} is clickable: {}", elementName, isClickable);
        return isClickable;
    }

    public void clickElement(WebElement element, String elementName) {
        LOGGER.info("Clicking element");
        element.click();
        LOGGER.info("Element {} clicked successfully", elementName);
    }

    public void clickElement(By locator, String elementName) {
        LOGGER.info("Clicking element");
        driver.findElement(locator).click();
        LOGGER.info("Element {} clicked successfully", elementName);
    }

    public void clickElementWithWait(WebElement element, String elementName) {
        LOGGER.info("Clicking element with wait");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not clickable: {}", elementName, e.getMessage());
            return;
        }
        LOGGER.info("Element {} clicked successfully", elementName);
    }

    public void clickElementWithFluentWait(WebElement element, String elementName) {
        LOGGER.info("Clicking element with fluent wait");
        try {
            fluentWait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not clickable: {}", elementName, e.getMessage());
            return;
        }
        LOGGER.info("Element {} clicked successfully", elementName);
    }

    public void clickElementWithWait(By locator, String elementName) {
        LOGGER.info("Clicking element with wait");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not clickable: {}", elementName, e.getMessage());
            return;
        }
        LOGGER.info("Element {} clicked successfully", elementName);
    }

    public void clickElementWithFluentWait(By locator, String elementName) {
        LOGGER.info("Clicking element with fluent wait");
        try {
            fluentWait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not clickable: {}", elementName, e.getMessage());
            return;
        }
        LOGGER.info("Element {} clicked successfully", elementName);
    }

    public void clickElementWithJavaScriptExecutor(WebElement element, String elementName) {
        jsExecutor = (JavascriptExecutor) driver;
        LOGGER.info("Clicking element with JavaScript executor");
        jsExecutor.executeScript("arguments[0].click();", element);
        LOGGER.info("Element {} clicked successfully with JavaScript executor", elementName);
    }

    public void clickElementWithJavaScriptExecutor(By locator, String elementName) {
        jsExecutor = (JavascriptExecutor) driver;
        LOGGER.info("Clicking element with JavaScript executor");
        jsExecutor.executeScript("arguments[0].click();", locator);
        LOGGER.info("Element {} clicked successfully with JavaScript executor", elementName);
    }

    public void clickElementsWithActionClass(WebElement element, String elementName) {
        actions.click(element).perform();
        LOGGER.info("Element {} clicked successfully with Action class", elementName);
    }

    public void clickElementsWithActionClass(By locator, String elementName) {
        actions.click(driver.findElement(locator)).perform();
        LOGGER.info("Element {} clicked successfully with Action class", elementName);
    }

    public void clearText(WebElement element, String elementName) {
        LOGGER.info("Clearing text from element");
        element.clear();
        LOGGER.info("Text cleared successfully from element {}", elementName);
    }

    public void clearText(By locator, String elementName) {
        LOGGER.info("Clearing text from element");
        driver.findElement(locator).clear();
        LOGGER.info("Text cleared successfully from element {}", elementName);
    }

    public void sendKeys(WebElement element, String keys, String elementName) {
        LOGGER.info("Sending keys to element");
        element.sendKeys(keys);
        LOGGER.info("Keys sent successfully to element {}", elementName);
    }

    public void sendKeys(By locator, String keys, String elementName) {
        LOGGER.info("Sending keys to element");
        driver.findElement(locator).sendKeys(keys);
        LOGGER.info("Keys sent successfully to element {}", elementName);
    }

    public void sendKeysWithWait(WebElement element, String keys, String elementName) {
        LOGGER.info("Sending keys to element with wait");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).sendKeys(keys);
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not clickable: {}", elementName, e.getMessage());
            return;
        }
        LOGGER.info("Keys sent successfully to element {}", elementName);
    }

    public void sendKeysWithFluentWait(WebElement element, String keys, String elementName) {
        LOGGER.info("Sending keys to element with fluent wait");
        try {
            fluentWait.until(ExpectedConditions.elementToBeClickable(element)).sendKeys(keys);
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not clickable: {}", elementName, e.getMessage());
            return;
        }
        LOGGER.info("Keys sent successfully to element {}", elementName);
    }

    public void sendKeysAsChar(WebElement element, String string, String elementName) {
        LOGGER.info("Sending keys as char to element");
        for (char c : string.toCharArray()) {
            element.sendKeys(String.valueOf(c));
        }
        LOGGER.info("Keys sent successfully to element {}", elementName);
    }

    public void sendKeysAsChar(By locator, String string, String elementName) {
        LOGGER.info("Sending keys as char to element");
        for (char c : string.toCharArray()) {
            driver.findElement(locator).sendKeys(String.valueOf(c));
        }
        LOGGER.info("Keys sent successfully to element {}", elementName);
    }

    public void sendKeysAsCharWithSleep(WebElement element, String string, String elementName) {
        LOGGER.info("Sending keys as char to element with sleep");
        for (char c : string.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.warn("Interrupted while sending keys as char: {}", e.getMessage());
            }
        }
        LOGGER.info("Keys sent successfully to element {}", elementName);
    }

    public void sendKeysWithJavaScriptExecutor(WebElement element, String string, String elementName) {
        jsExecutor = (JavascriptExecutor) driver;
        LOGGER.info("Sending keys to element with JavaScript executor");
        jsExecutor.executeScript("arguments[0].value = arguments[1];", element, string);
        LOGGER.info("Keys sent successfully to element {}", elementName);
    }

    public void sendKeysWithJavaScriptExecutor(By locator, String string, String elementName) {
        jsExecutor = (JavascriptExecutor) driver;
        LOGGER.info("Sending keys to element with JavaScript executor");
        jsExecutor.executeScript("arguments[0].value = arguments[1];", locator, string);
        LOGGER.info("Keys sent successfully to element {}", elementName);
    }

    public String getText(WebElement element, String elementName) {
        LOGGER.info("Getting text from element");
        String text = element.getText();
        LOGGER.info("Text from element {} is: {}", elementName, text);
        return text;
    }

    public String getName(WebElement element, String elementName) {
        LOGGER.info("Getting name from element");
        String name = element.getAttribute("name");
        LOGGER.info("Name from element {} is: {}", elementName, name);
        return name;
    }

    public String getAttribute(WebElement element, String attributeName, String elementName) {
        LOGGER.info("Getting attribute from element");
        String attribute = element.getAttribute(attributeName);
        LOGGER.info("Attribute {} from element {} is: {}", attributeName, elementName, attribute);
        return attribute;
    }

    public void moveCursorToElement(WebElement element, String elementName) {
        actions.moveToElement(element).perform();
        LOGGER.info("Cursor moved to element {}", elementName);
    }

    public void moveCursorToElement(By locator, String elementName) {
        actions.moveToElement(driver.findElement(locator)).perform();
    }

    public void moveCursorToElementUsingCoordinates(WebElement element, int x, int y, String elementName) {
        actions.moveToElement(element, x, y).perform();
        LOGGER.info("Cursor moved to element {} using coordinates", elementName);
    }

    public void moveCursorToElementUsingCoordinates(By locator, int x, int y, String elementName) {
        actions.moveToElement(driver.findElement(locator), x, y).perform();
        LOGGER.info("Cursor moved to element {} using coordinates", elementName);
    }

    public boolean isElementInvisible(WebElement element, String elementName) {
        LOGGER.info("Checking if element is invisible");
        try {
            boolean isInvisible = wait.until(ExpectedConditions.invisibilityOf(element));
            LOGGER.info("Element {} is invisible: {}", elementName, isInvisible);
            return isInvisible;
        } catch (TimeoutException e) {
            LOGGER.warn("Element {} is not invisible: {}", elementName, e.getMessage());
            captureErrorScreenshot("isElementInvisible");
            return false;
        }
    }

    public void scrollDown(int pixels) {
        LOGGER.info("Scrolling down by {} pixels", pixels);
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, " + pixels + ");");
            LOGGER.info("Scrolled down successfully by {} pixels", pixels);
        } catch (WebDriverException e) {
            String errorMsg = "Error scrolling down";
            LOGGER.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public boolean isContainerScrollable(WebElement containerElement) {
        LOGGER.info("Checking if container is scrollable");
        try {
            if (containerElement == null) {
                throw new IllegalArgumentException("Container element cannot be null.");
            }

            Boolean isScrollable = (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].scrollHeight > arguments[0].clientHeight || " +
                            "arguments[0].scrollWidth > arguments[0].clientWidth;",
                    containerElement);

            LOGGER.info("Container is scrollable: {}", isScrollable);
            return isScrollable != null && isScrollable;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid argument for isContainerScrollable: {}", e.getMessage());
            throw e;
        } catch (WebDriverException e) {
            String errorMsg = "Error checking if container is scrollable";
            LOGGER.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public void scrollDownInContainer(WebElement containerElement, int pixels) {
        LOGGER.info("Scrolling down in container by {} pixels", pixels);
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = arguments[0].scrollHeight",
                    containerElement);
            LOGGER.info("Scrolled down in container successfully by {} pixels", pixels);
        } catch (WebDriverException e) {
            String errorMsg = "Error scrolling down in container";
            LOGGER.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public void switchToFrame(WebElement frameElement, String frameName) {
        LOGGER.info("Switching to frame");
        driver.switchTo().frame(frameElement);
        LOGGER.info("Switched to frame {} successfully", frameName);
    }

    public void switchToDefaultContent() {
        LOGGER.info("Switching to default content");
        driver.switchTo().defaultContent();
        LOGGER.info("Switched to default content successfully");
    }

    public void switchToParentFrame(String frameName) {
        LOGGER.info("Switching to parent frame");
        driver.switchTo().parentFrame();
        LOGGER.info("Switched to parent frame of {} successfully", frameName);
    }

    public WebElement findRelativeElement(WebElement parentElement, String relativeXpath, String elementName) {
        LOGGER.info("Finding relative element");
        WebElement element = parentElement.findElement(By.xpath(relativeXpath));
        LOGGER.info("Relative element {} found: {}", elementName, element);
        return element;
    }
}