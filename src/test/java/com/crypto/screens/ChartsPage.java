package com.crypto.screens;

import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crypto.base.BaseTest;
import com.crypto.utils.SoftAssertActions;

public class ChartsPage extends BaseScreen {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChartsPage.class);
    public static volatile ThreadLocal<ChartsPage> instance = new ThreadLocal<>();
    private WebDriver driver;

    public static final Set<String> sessionIds = new HashSet<>();

    public static void setInstance(ChartsPage chartsPage) {
        instance.set(chartsPage);
    }

    private static ChartsPage getThreadInstance() {
        return instance.get();
    }

    public static ChartsPage getInstance() {
        WebDriver driver = BaseTest.getBaseDriver();
        if (driver == null) {
            LOGGER.warn("WebDriver is null. Cannot create ChartsPage instance.");
            throw new IllegalStateException("WebDriver is null. Cannot create ChartsPage instance.");
        }

        // Check if driver is RemoteWebDriver before calling getSessionId()
        if (driver instanceof RemoteWebDriver) {
            try {
                String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
                if (getThreadInstance() == null || !sessionIds.contains(sessionId)) {
                    synchronized (ChartsPage.class) {
                        if (getThreadInstance() == null || !sessionIds.contains(sessionId)) {
                            setInstance(new ChartsPage(driver));
                            sessionIds.add(sessionId);
                        }
                    }
                }
            } catch (Exception e) {
                // Fallback if session ID retrieval fails
                if (getThreadInstance() == null) {
                    synchronized (ChartsPage.class) {
                        if (getThreadInstance() == null) {
                            setInstance(new ChartsPage(driver));
                        }
                    }
                }
            }
        } else {
            // For non-RemoteWebDriver, use simple thread-local check
            if (getThreadInstance() == null) {
                synchronized (ChartsPage.class) {
                    if (getThreadInstance() == null) {
                        setInstance(new ChartsPage(driver));
                    }
                }
            }
        }

        return getThreadInstance();
    }

    public ChartsPage(WebDriver driver) {
        super(driver);
    }

    // Chart Header Toolbar Elements

    @FindBy(id = "header-toolbar-symbol-search")
    private WebElement searchIcon;

    @FindBy(xpath = "header-toolbar-compare")
    private WebElement compareIcon;

    @FindBy(id = "header-toolbar-intervals")
    private WebElement intervalIcon;

    @FindBy(id = "header-toolbar-chart-styles")
    private WebElement candlesIcon;

    @FindBy(xpath = "//button/div[contains(text(), 'Indicators')]")
    private WebElement indicatorIcon;

    @FindBy(id = "header-toolbar-study-templates")
    private WebElement indicatorTemplateIcon;

    @FindBy(tagName = "table")
    private WebElement customTable;

    @FindBy(xpath = "//div[text()='Check API']")
    private WebElement checkApiButton;

    @FindBy(xpath = "//div[@id='header-toolbar-undo-redo']/button[1]")
    private WebElement undoButton;

    @FindBy(xpath = "//div[@id='header-toolbar-undo-redo']/button[2]")
    private WebElement redoButton;

    @FindBy(id = "header-toolbar-save-load")
    private WebElement saveButton;

    @FindBy(xpath = "//button[@id='header-toolbar-save-load']/following-sibling::button")
    private WebElement manageLayoutIcon;

    @FindBy(id = "header-toolbar-quick-search")
    private WebElement quickSearchIcon;

    @FindBy(id = "header-toolbar-properties")
    private WebElement settingsIcon;

    @FindBy(id = "header-toolbar-fullscreen")
    private WebElement fullScreenIcon;

    @FindBy(id = "header-toolbar-screenshot")
    private WebElement takeScreenshotIcon;

    @FindBy(xpath = "//button/div[text()='‹']")
    private WebElement chartCollapseIcon;

    @FindBy(xpath = "//button/div[text()='›']")
    private WebElement chartExpandIcon;

    // Chart Bottom Toolbar Elements

    @FindBy(xpath = "//button/div[text()='5y']")
    private WebElement fiveYearIntervalButton;

    @FindBy(xpath = "//button/div[text()='1y']")
    private WebElement oneYearIntervalButton;

    @FindBy(xpath = "//button[contains(@data-name, 'go-to-date')]")
    private WebElement goToCalendarIcon;

    @FindBy(xpath = "//button[contains(@aria-label, 'Timezone')]/div")
    private WebElement timezoneIcon;

    @FindBy(xpath = "//button[contains(@aria-label, 'Toggle Percentage')]")
    private WebElement togglePercentageIcon;

    @FindBy(xpath = "//div[text()='log']/parent::button")
    private WebElement logButton;

    @FindBy(xpath = "//div[text()='auto']/parent::button")
    private WebElement autoButton;

    @FindBy(xpath = "//div[text()='A']/parent::div/parent::div")
    private WebElement axisGearIcon;

    // Indicator Popup SectionElements
    @FindBy(xpath = "//div/div[text()='Indicators']")
    private WebElement indicatorsTextLabel;

    @FindBy(xpath = "//button[@data-name='close']")
    private WebElement indicatorPopupCloseButton;

    @FindBy(xpath = "//input[@placeholder='Search']")
    private WebElement indicatorPopupSearchInputField;

    @FindBy(xpath = "//div[@class='container-hrZZtP0J scroll-I087YV6b']")
    private WebElement indicatorsListSection;

    @FindBy(xpath = "//div[@class='container-hrZZtP0J scroll-I087YV6b']/div/div/span")
    private WebElement indicatorsItemList;

    public boolean isSearchIconDisplayed() {
        return commonActions.isElementDisplayed(searchIcon, "searchIcon");
    }

    public boolean isSearchIconClickable() {
        return commonActions.isElementClickable(searchIcon, "searchIcon");
    }

    public void clickOnSearchIcon() {
        commonActions.clickElement(searchIcon, "searchIcon");
    }

    public String getSearchIconValue() {
        return commonActions.getAttribute(searchIcon, "value", "searchIcon");
    }

    public boolean isCompareIconDisplayed() {
        return commonActions.isElementDisplayed(compareIcon, "compareIcon");
    }

    public boolean isCompareIconClickable() {
        return commonActions.isElementClickable(compareIcon, "compareIcon");
    }

    public void clickOnCompareIcon() {
        commonActions.clickElement(compareIcon, "compareIcon");
    }

    public boolean isIntervalIconDisplayed() {
        return commonActions.isElementDisplayed(intervalIcon, "intervalIcon");
    }

    public boolean isIntervalIconClickable() {
        return commonActions.isElementClickable(intervalIcon, "intervalIcon");
    }

    public void clickOnIntervalIcon() {
        commonActions.clickElement(intervalIcon, "intervalIcon");
    }

    public boolean isCandlesIconDisplayed() {
        return commonActions.isElementDisplayed(candlesIcon, "candlesIcon");
    }

    public boolean isCandlesIconClickable() {
        return commonActions.isElementClickable(candlesIcon, "candlesIcon");
    }

    public void clickOnCandlesIcon() {
        commonActions.clickElement(candlesIcon, "candlesIcon");
    }

    public boolean isIndicatorIconDisplayed() {
        return commonActions.isElementDisplayed(indicatorIcon, "indicatorIcon");
    }

    public boolean isIndicatorIconClickable() {
        return commonActions.isElementClickable(indicatorIcon, "indicatorIcon");
    }

    public void clickOnIndicatorIcon() {
        commonActions.clickElement(indicatorIcon, "indicatorIcon");
    }

    public boolean isIndicatorTemplateIconDisplayed() {
        return commonActions.isElementDisplayed(indicatorTemplateIcon, "indicatorTemplateIcon");
    }

    public boolean isIndicatorTemplateIconClickable() {
        return commonActions.isElementClickable(indicatorTemplateIcon, "indicatorTemplateIcon");
    }

    public void clickOnIndicatorTemplateIcon() {
        commonActions.clickElement(indicatorTemplateIcon, "indicatorTemplateIcon");
    }

    public boolean isCustomTableDisplayed() {
        return commonActions.isElementDisplayed(customTable, "customTable");
    }

    public boolean isCustomTableClickable() {
        return commonActions.isElementClickable(customTable, "customTable");
    }

    public void clickOnCustomTable() {
        commonActions.clickElement(customTable, "customTable");
    }

    public boolean isCheckApiButtonDisplayed() {
        return commonActions.isElementDisplayed(checkApiButton, "checkApiButton");
    }

    public boolean isCheckApiButtonClickable() {
        return commonActions.isElementClickable(checkApiButton, "checkApiButton");
    }

    public void clickOnCheckApiButton() {
        commonActions.clickElement(checkApiButton, "checkApiButton");
    }

    public boolean isUndoButtonDisplayed() {
        return commonActions.isElementDisplayed(undoButton, "undoButton");
    }

    public boolean isUndoButtonClickable() {
        return commonActions.isElementClickable(undoButton, "undoButton");
    }

    public void clickOnUndoButton() {
        commonActions.clickElement(undoButton, "undoButton");
    }

    public boolean isRedoButtonDisplayed() {
        return commonActions.isElementDisplayed(redoButton, "redoButton");
    }

    public boolean isRedoButtonClickable() {
        return commonActions.isElementClickable(redoButton, "redoButton");
    }

    public void clickOnRedoButton() {
        commonActions.clickElement(redoButton, "redoButton");
    }

    public boolean isSaveButtonDisplayed() {
        return commonActions.isElementDisplayed(saveButton, "saveButton");
    }

    public boolean isSaveButtonClickable() {
        return commonActions.isElementClickable(saveButton, "saveButton");
    }

    public void clickOnSaveButton() {
        commonActions.clickElement(saveButton, "saveButton");
    }

    public boolean isManageLayoutIconDisplayed() {
        return commonActions.isElementDisplayed(manageLayoutIcon, "manageLayoutIcon");
    }

    public boolean isManageLayoutIconClickable() {
        return commonActions.isElementClickable(manageLayoutIcon, "manageLayoutIcon");
    }

    public void clickOnManageLayoutIcon() {
        commonActions.clickElement(manageLayoutIcon, "manageLayoutIcon");
    }

    public boolean isQuickSearchIconDisplayed() {
        return commonActions.isElementDisplayed(quickSearchIcon, "quickSearchIcon");
    }

    public boolean isQuickSearchIconClickable() {
        return commonActions.isElementClickable(quickSearchIcon, "quickSearchIcon");
    }

    public void clickOnQuickSearchIcon() {
        commonActions.clickElement(quickSearchIcon, "quickSearchIcon");
    }

    public boolean isSettingsIconDisplayed() {
        return commonActions.isElementDisplayed(settingsIcon, "settingsIcon");
    }

    public boolean isSettingsIconClickable() {
        return commonActions.isElementClickable(settingsIcon, "settingsIcon");
    }

    public void clickOnSettingsIcon() {
        commonActions.clickElement(settingsIcon, "settingsIcon");
    }

    public boolean isFullScreenIconDisplayed() {
        return commonActions.isElementDisplayed(fullScreenIcon, "fullScreenIcon");
    }

    public boolean isFullScreenIconClickable() {
        return commonActions.isElementClickable(fullScreenIcon, "fullScreenIcon");
    }

    public void clickOnFullScreenIcon() {
        commonActions.clickElement(fullScreenIcon, "fullScreenIcon");
    }

    public boolean isTakeScreenshotIconDisplayed() {
        return commonActions.isElementDisplayed(takeScreenshotIcon, "takeScreenshotIcon");
    }

    public boolean isTakeScreenshotIconClickable() {
        return commonActions.isElementClickable(takeScreenshotIcon, "takeScreenshotIcon");
    }

    public void clickOnTakeScreenshotIcon() {
        commonActions.clickElement(takeScreenshotIcon, "takeScreenshotIcon");
    }

    public boolean isChartCollapseIconDisplayed() {
        return commonActions.isElementDisplayed(chartCollapseIcon, "chartCollapseIcon");
    }

    public boolean isChartCollapseIconClickable() {
        return commonActions.isElementClickable(chartCollapseIcon, "chartCollapseIcon");
    }

    public void clickOnChartCollapseIcon() {
        commonActions.clickElement(chartCollapseIcon, "chartCollapseIcon");
    }

    public boolean isChartExpandIconDisplayed() {
        return commonActions.isElementDisplayed(chartExpandIcon, "chartExpandIcon");
    }

    public boolean isChartExpandIconClickable() {
        return commonActions.isElementClickable(chartExpandIcon, "chartExpandIcon");
    }

    public void clickOnChartExpandIcon() {
        commonActions.clickElement(chartExpandIcon, "chartExpandIcon");
    }

    public boolean isFiveYearIntervalButtonDisplayed() {
        return commonActions.isElementDisplayed(fiveYearIntervalButton, "fiveYearIntervalButton");
    }

    public boolean isFiveYearIntervalButtonClickable() {
        return commonActions.isElementClickable(fiveYearIntervalButton, "fiveYearIntervalButton");
    }

    public void clickOnFiveYearIntervalButton() {
        commonActions.clickElement(fiveYearIntervalButton, "fiveYearIntervalButton");
    }

    public boolean isOneYearIntervalButtonDisplayed() {
        return commonActions.isElementDisplayed(oneYearIntervalButton, "oneYearIntervalButton");
    }

    public boolean isOneYearIntervalButtonClickable() {
        return commonActions.isElementClickable(oneYearIntervalButton, "oneYearIntervalButton");
    }

    public void clickOnOneYearIntervalButton() {
        commonActions.clickElement(oneYearIntervalButton, "oneYearIntervalButton");
    }

    public boolean isGoToCalendarIconDisplayed() {
        return commonActions.isElementDisplayed(goToCalendarIcon, "goToCalendarIcon");
    }

    public boolean isGoToCalendarIconClickable() {
        return commonActions.isElementClickable(goToCalendarIcon, "goToCalendarIcon");
    }

    public boolean isTimezoneIconDisplayed() {
        return commonActions.isElementDisplayed(timezoneIcon, "timezoneIcon");
    }

    public boolean isTimezoneIconClickable() {
        return commonActions.isElementClickable(timezoneIcon, "timezoneIcon");
    }

    public void clickOnTimezoneIcon() {
        commonActions.clickElement(timezoneIcon, "timezoneIcon");
    }

    public boolean isTogglePercentageIconDisplayed() {
        return commonActions.isElementDisplayed(togglePercentageIcon, "togglePercentageIcon");
    }

    public boolean isTogglePercentageIconClickable() {
        return commonActions.isElementClickable(togglePercentageIcon, "togglePercentageIcon");
    }

    public void clickOnTogglePercentageIcon() {
        commonActions.clickElement(togglePercentageIcon, "togglePercentageIcon");
    }

    public boolean isLogButtonDisplayed() {
        return commonActions.isElementDisplayed(logButton, "logButton");
    }

    public boolean isLogButtonClickable() {
        return commonActions.isElementClickable(logButton, "logButton");
    }

    public void clickOnLogButton() {
        commonActions.clickElement(logButton, "logButton");
    }

    public boolean isAutoButtonDisplayed() {
        return commonActions.isElementDisplayed(autoButton, "autoButton");
    }

    public boolean isAutoButtonClickable() {
        return commonActions.isElementClickable(autoButton, "autoButton");
    }

    public void clickOnAutoButton() {
        commonActions.clickElement(autoButton, "autoButton");
    }

    public boolean isAxisGearIconDisplayed() {
        return commonActions.isElementDisplayed(axisGearIcon, "axisGearIcon");
    }

    public boolean isAxisGearIconClickable() {
        return commonActions.isElementClickable(axisGearIcon, "axisGearIcon");
    }

    public void clickOnAxisGearIcon() {
        commonActions.clickElement(axisGearIcon, "axisGearIcon");
    }

    public void addIndicator(SoftAssertActions softAssertActions, String indicator) {
        LOGGER.info("Adding an indicator: {}", indicator);
        softAssertActions.assertTrue(isIndicatorIconDisplayed(), "Indicator Icon is displayed");
        softAssertActions.assertTrue(isIndicatorIconClickable(), "Indicator Icon is clickable");
        clickOnIndicatorIcon();
    }
}
