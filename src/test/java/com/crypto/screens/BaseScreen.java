package com.crypto.screens;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crypto.base.BaseTest;
import com.crypto.constants.Constants;
import com.crypto.utils.CommonActions;
import com.crypto.utils.StringActions;

public class BaseScreen {

    private WebDriver driver;
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseScreen.class);
    public final CommonActions commonActions;
    public final StringActions stringActions;

    public BaseScreen(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
        this.commonActions = new CommonActions(driver);
        this.stringActions = new StringActions();
    }

    public static BaseScreen getInstance() {
        WebDriver driver = BaseTest.getBaseDriver();
        if (driver == null) {
            LOGGER.warn("WebDriver is null. Cannot create BaseScreen instance.");
            throw new IllegalStateException("WebDriver is null. Cannot create BaseScreen instance.");
        }
        return new BaseScreen(driver);
    }

    public void openHomePage() {
        commonActions.openUrl(Constants.BASE_URL);
    }

    public void switchToDefaultContent() {
        commonActions.switchToDefaultContent();
    }

    public static void sleep(long seconds) {
        try {
            for (int i = 0; i < seconds; i++) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
