package com.crypto.constants;

import org.openqa.selenium.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.ConfigReader;

public class ConstantsInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstantsInitializer.class);
    private static boolean initialized = false;

    /**
     * Initializes all Constants static fields from config.ini file.
     * This should be called once at startup, typically in @BeforeSuite.
     */
    public static synchronized void initialize() {
        if (initialized) {
            LOGGER.warn("Constants have already been initialized. Skipping re-initialization.");
            return;
        }

        try {
            LOGGER.info("Initializing Constants from config.ini...");

            // Server configuration
            if (Constants.DRIVER_TYPE == null) {
                Constants.DRIVER_TYPE = ConfigReader.getPropertyValues(
                        "server", "driver_type");
            }

            // Environment configuration
            Constants.ENVIRONMENT = ConfigReader.getPropertyValues(
                    Constants.CONFIG_KEY_ENVIRONMENT, "env");
            if (Constants.ENVIRONMENT == null) {
                Constants.ENVIRONMENT = System.getProperty("env", "dev");
            }

            String platformStr = ConfigReader.getPropertyValues(
                    Constants.CONFIG_KEY_ENVIRONMENT, Constants.CONFIG_KEY_PLATFORM);
            if (platformStr == null) {
                platformStr = System.getProperty("platform", "mac");
            }
            Constants.PLATFORM = parsePlatform(platformStr);

            setBaseUrl();

            Constants.BROWSER = ConfigReader.getPropertyValues(
                    Constants.CONFIG_KEY_ENVIRONMENT, Constants.CONFIG_KEY_BROWSER);
            if (Constants.BROWSER == null) {
                Constants.BROWSER = System.getProperty("browser", Constants.CONFIG_KEY_CHROME);
            }

            // Jenkins/Host configuration
            Constants.HOST = ConfigReader.getPropertyValues(
                    Constants.CONFIG_KEY_JENKINS, Constants.CONFIG_KEY_HOST);
            if (Constants.HOST == null) {
                Constants.HOST = System.getProperty("host", "local");
            }

            // Browser configuration
            String headlessStr = ConfigReader.getPropertyValues(
                    Constants.CONFIG_KEY_BROWSER, Constants.CONFIG_KEY_HEADLESS);
            Constants.HEADLESS = parseBoolean(headlessStr, false);

            String windowMaximizeStr = ConfigReader.getPropertyValues(
                    Constants.CONFIG_KEY_BROWSER, Constants.CONFIG_KEY_WINDOW_MAXIMIZE);
            Constants.WINDOW_MAXIMIZE = parseBoolean(windowMaximizeStr, true);

            String implicitWaitStr = ConfigReader.getPropertyValues(
                    Constants.CONFIG_KEY_BROWSER, Constants.CONFIG_KEY_IMPLICIT_WAIT);
            Constants.IMPLICIT_WAIT = parseInt(implicitWaitStr, 10);

            String explicitWaitStr = ConfigReader.getPropertyValues(
                    Constants.CONFIG_KEY_BROWSER, Constants.CONFIG_KEY_EXPLICIT_WAIT);
            Constants.EXPLICIT_WAIT = parseInt(explicitWaitStr, 5);

            String pageLoadTimeoutStr = ConfigReader.getPropertyValues(
                    Constants.CONFIG_KEY_BROWSER, Constants.CONFIG_KEY_PAGE_LOAD_TIMEOUT);
            Constants.PAGE_LOAD_TIMEOUT = parseInt(pageLoadTimeoutStr, 30);

            String scriptTimeoutStr = ConfigReader.getPropertyValues(
                    Constants.CONFIG_KEY_BROWSER, Constants.CONFIG_KEY_SCRIPT_TIMEOUT);
            Constants.SCRIPT_TIMEOUT = parseInt(scriptTimeoutStr, 15);

            String incognitoStr = ConfigReader.getPropertyValues(
                    Constants.CONFIG_KEY_BROWSER, Constants.CONFIG_KEY_INCOGNITO);
            Constants.INCOGNITO = parseBoolean(incognitoStr, false);

            initialized = true;
            LOGGER.info("Constants initialized successfully:");
            LOGGER.info("  DRIVER_TYPE: {}", Constants.DRIVER_TYPE);
            LOGGER.info("  ENVIRONMENT: {}", Constants.ENVIRONMENT);
            LOGGER.info("  PLATFORM: {}", Constants.PLATFORM);
            LOGGER.info("  BROWSER: {}", Constants.BROWSER);
            LOGGER.info("  HOST: {}", Constants.HOST);
            LOGGER.info("  HEADLESS: {}", Constants.HEADLESS);
            LOGGER.info("  WINDOW_MAXIMIZE: {}", Constants.WINDOW_MAXIMIZE);
            LOGGER.info("  IMPLICIT_WAIT: {}", Constants.IMPLICIT_WAIT);
            LOGGER.info("  PAGE_LOAD_TIMEOUT: {}", Constants.PAGE_LOAD_TIMEOUT);
            LOGGER.info("  SCRIPT_TIMEOUT: {}", Constants.SCRIPT_TIMEOUT);
            LOGGER.info("  INCOGNITO: {}", Constants.INCOGNITO);
            LOGGER.info("  BASE_URL: {}", Constants.BASE_URL);

        } catch (Exception e) {
            LOGGER.error("Failed to initialize Constants: ", e);
            throw new RuntimeException("Failed to initialize Constants", e);
        }
    }

    private static void setBaseUrl() {
        if (Constants.ENVIRONMENT == null) {
            LOGGER.warn("Environment name is null, defaulting to BASE_URL_DEV");
            Constants.BASE_URL = Constants.BASE_URL_DEV;
        }

        switch (Constants.ENVIRONMENT) {
            case "dev":
            case "development":
                LOGGER.info("Setting BASE_URL to BASE_URL_DEV: {}", Constants.BASE_URL_DEV);
                Constants.BASE_URL = Constants.BASE_URL_DEV;
                break;
            case "qa":
            case "test":
            case "testing":
                LOGGER.info("Setting BASE_URL to BASE_URL_QA: {}", Constants.BASE_URL_QA);
                Constants.BASE_URL = Constants.BASE_URL_QA;
                break;
            case "prod":
            case "production":
                LOGGER.info("Setting BASE_URL to BASE_URL_PROD: {}", Constants.BASE_URL_PROD);
                Constants.BASE_URL = Constants.BASE_URL_PROD;
                break;
            default:
                LOGGER.warn("Unknown environment '{}', defaulting to BASE_URL_DEV: {}", Constants.ENVIRONMENT,
                        Constants.BASE_URL_DEV);
                Constants.BASE_URL = Constants.BASE_URL_DEV;
                break;
        }
    }

    private static Platform parsePlatform(String platformStr) {
        if (platformStr == null) {
            return Platform.MAC;
        }
        String lower = platformStr.toLowerCase();
        if (lower.contains("mac") || lower.contains("darwin")) {
            return Platform.MAC;
        } else if (lower.contains("win")) {
            return Platform.WINDOWS;
        } else if (lower.contains("linux")) {
            return Platform.LINUX;
        }
        return Platform.MAC; // default
    }

    private static boolean parseBoolean(String value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return Constants.CONFIG_KEY_TRUE.equalsIgnoreCase(value.trim());
    }

    private static int parseInt(String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            LOGGER.warn("Failed to parse integer value '{}', using default: {}", value, defaultValue);
            return defaultValue;
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}