package com.crypto.Driver;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crypto.constants.Constants;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverInstance {

    private Logger LOGGER = LoggerFactory.getLogger(DriverInstance.class);
    private static int threadCount = 0;

    public static WebDriver createDriver() {
        WebDriver driver = null;

        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (Constants.DRIVER_TYPE.equals(Constants.CONFIG_KEY_WEB_DRIVER_MANAGER)) {
            driver = getWebDriverManager(Constants.BROWSER);
        } else if (Constants.DRIVER_TYPE.equals(Constants.CONFIG_KEY_REMOTE_WEB_DRIVER)) {

        } else if (Constants.DRIVER_TYPE.equals(Constants.CONFIG_KEY_DRIVER_LOCAL)) {

        }

        capabilities.setBrowserName(Constants.BROWSER);
        capabilities.setPlatform(Constants.PLATFORM);
        capabilities.setCapability("headless", Constants.HEADLESS);
        capabilities.setCapability("windowMaximize", Constants.WINDOW_MAXIMIZE);
        capabilities.setCapability("implicitWait", Constants.IMPLICIT_WAIT);
        capabilities.setCapability("pageLoadTimeout", Constants.PAGE_LOAD_TIMEOUT);
        capabilities.setCapability("scriptTimeout", Constants.SCRIPT_TIMEOUT);
        capabilities.setCapability("incognito", Constants.INCOGNITO);

        return driver;
    }

    private static WebDriver getWebDriverManager(String browserType) {
        switch (browserType) {
            case Constants.CONFIG_KEY_CHROME:
                return createChromeWebDriverManager();
            case Constants.CONFIG_KEY_FIREFOX:
                return createFirefoxWebDriverManager();
            case Constants.CONFIG_KEY_SAFARI:
                return createSafariWebDriverManager();
        }
        return createChromeWebDriverManager();
    }

    private static WebDriver createChromeWebDriverManager() {
        WebDriver driver;
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        if (Constants.WINDOW_MAXIMIZE)
            chromeOptions.addArguments("--start-maximized");
        if (Constants.INCOGNITO)
            chromeOptions.addArguments("--incognito");
        if (Constants.HEADLESS)
            chromeOptions.addArguments("--headless=new");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.IMPLICIT_WAIT));
        return driver;
    }

    private static WebDriver createFirefoxWebDriverManager() {
        WebDriver driver;
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        if (Constants.WINDOW_MAXIMIZE)
            firefoxOptions.addArguments("--start-maximized");
        if (Constants.INCOGNITO)
            firefoxOptions.addArguments("-private");
        if (Constants.HEADLESS)
            firefoxOptions.addArguments("-headless");
        driver = new FirefoxDriver(firefoxOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.IMPLICIT_WAIT));
        return driver;
    }

    private static WebDriver createSafariWebDriverManager() {
        WebDriver driver;
        WebDriverManager.chromedriver().setup();
        SafariOptions safariOptions = new SafariOptions();
        if (Constants.WINDOW_MAXIMIZE)
            safariOptions.setCapability("start-maximized", true);
        driver = new SafariDriver(safariOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.IMPLICIT_WAIT));
        return driver;
    }

}
