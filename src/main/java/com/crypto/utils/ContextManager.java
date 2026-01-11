package com.crypto.utils;

import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;

public class ContextManager {
    private static final ThreadLocal<ExtentTest> extentPoolParent = new ThreadLocal<>();

    public static ExtentTest getExtentTest() {
        return extentPoolParent.get();
    }

    public static void setExtentTest(ExtentTest extentTest) {
        extentPoolParent.set(extentTest);
    }

    public static void removeExtentTest() {
        extentPoolParent.remove();
    }

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver webDriver) {
        driver.set(webDriver);
    }
}
