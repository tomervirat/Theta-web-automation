package com.crypto.constants;

import org.openqa.selenium.Platform;

public class Constants {

    /*
     * File System Paths
     */
    public static final String CURRENT_DIR = System.getProperty("user.dir");
    public static final String CONFIG_FILE_PATH = CURRENT_DIR + "/src/main/java/config/config.ini";
    public static final String EXTENT_REPORT_PATH = CURRENT_DIR + "/state/reports/executionReport";
    public static final String LOG_DIR = CURRENT_DIR + "/state/logs";

    /*
     * Base URLs
     */
    public static final String BASE_URL_DEV = "http://localhost:3000";
    public static final String BASE_URL_QA = "http://127.0.0.1:3000";
    public static final String BASE_URL_PROD = "http://127.0.0.1:3000";

    /*
     * Config File Keys
     */
    public static final String CONFIG_KEY_COMMON = "local";
    public static final String CONFIG_KEY_REMOTE = "remote";

    public static final String CONFIG_KEY_CHROME = "chrome";
    public static final String CONFIG_KEY_FIREFOX = "firefox";
    public static final String CONFIG_KEY_SAFARI = "safari";

    public static final String CONFIG_KEY_WEB_DRIVER_MANAGER = "web-driver-manager";
    public static final String CONFIG_KEY_REMOTE_WEB_DRIVER = "remote-web-driver";
    public static final String CONFIG_KEY_DRIVER_LOCAL = "driver-local";

    public static final String CONFIG_KEY_URL = "url";
    public static final String CONFIG_KEY_JENKINS = "jenkins";
    public static final String CONFIG_KEY_HOST = "host";

    public static final String CONFIG_KEY_TRUE = "true";
    public static final String CONFIG_KEY_FALSE = "false";

    public static final String CONFIG_KEY_ENVIRONMENT = "environment";
    public static final String CONFIG_KEY_DEV = "dev";
    public static final String CONFIG_KEY_QA = "qa";
    public static final String CONFIG_KEY_PROD = "prod";

    public static final String CONFIG_KEY_SELENIUM_LOCAL = "selenium-local";
    public static final String CONFIG_KEY_SELENIUM_REMOTE = "selenium-remote";

    public static final String CONFIG_KEY_PLATFORM = "platform";
    public static final String CONFIG_KEY_PLATFORM_MAC = "platform-mac";
    public static final String CONFIG_KEY_PLATFORM_WINDOWS = "platform-windows";

    public static final String CONFIG_KEY_DEVICE = "device";
    public static final String CONFIG_KEY_BROWSER = "browser";

    public static final String CONFIG_KEY_HEADLESS = "headless";
    public static final String CONFIG_KEY_WINDOW_MAXIMIZE = "windowMaximize";
    public static final String CONFIG_KEY_IMPLICIT_WAIT = "implicitWait";
    public static final String CONFIG_KEY_EXPLICIT_WAIT = "explicitWait";
    public static final String CONFIG_KEY_PAGE_LOAD_TIMEOUT = "pageLoadTimeout";
    public static final String CONFIG_KEY_SCRIPT_TIMEOUT = "scriptTimeout";
    public static final String CONFIG_KEY_INCOGNITO = "incognito";

    /*
     * Runtime Configurations
     */
    public static String DRIVER_TYPE;
    public static String HOST;
    public static String ENVIRONMENT;
    public static Platform PLATFORM;
    public static String BROWSER;
    public static String BASE_URL;

    public static boolean HEADLESS;
    public static boolean WINDOW_MAXIMIZE;
    public static int IMPLICIT_WAIT;
    public static int EXPLICIT_WAIT;
    public static int PAGE_LOAD_TIMEOUT;
    public static int SCRIPT_TIMEOUT;
    public static boolean INCOGNITO;

    /*
     * Test Groups
     */
    public static final String GROUP_SANITY = "sanity";
    public static final String GROUP_REGRESSION = "regression";

    /*
     * Scroll Directions
     */
    public static final String DOWN = "down";
    public static final String UP = "up";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";

}
