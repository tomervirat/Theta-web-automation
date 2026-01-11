package com.crypto.screens;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crypto.base.BaseTest;
import com.crypto.utils.SoftAssertActions;

public class SymbolSearchPopupScreen extends ChartsPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(SymbolSearchPopupScreen.class);
    public static volatile ThreadLocal<SymbolSearchPopupScreen> instance = new ThreadLocal<>();
    private WebDriver driver;

    public static final Set<String> sessionIds = new HashSet<>();

    public static void setInstance(SymbolSearchPopupScreen symbolSearchPopupScreen) {
        instance.set(symbolSearchPopupScreen);
    }

    private static SymbolSearchPopupScreen getThreadInstance() {
        return instance.get();
    }

    public static SymbolSearchPopupScreen getInstance() {
        WebDriver driver = BaseTest.getBaseDriver();
        if (driver == null) {
            LOGGER.warn("WebDriver is null. Cannot create SymbolSearchPopupScreen instance.");
            throw new IllegalStateException("WebDriver is null. Cannot create SymbolSearchPopupScreen instance.");
        }
        if (driver instanceof RemoteWebDriver) {
            try {
                String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
                if (getThreadInstance() == null || !sessionIds.contains(sessionId)) {
                    synchronized (SymbolSearchPopupScreen.class) {
                        if (getThreadInstance() == null || !sessionIds.contains(sessionId)) {
                            setInstance(new SymbolSearchPopupScreen(driver));
                            sessionIds.add(sessionId);
                        }
                    }
                }
            } catch (Exception e) {
                // Fallback if session ID retrieval fails
                if (getThreadInstance() == null) {
                    synchronized (SymbolSearchPopupScreen.class) {
                        if (getThreadInstance() == null) {
                            setInstance(new SymbolSearchPopupScreen(driver));
                        }
                    }
                }
            }
        } else {
            // For non-RemoteWebDriver, use simple thread-local check
            if (getThreadInstance() == null) {
                synchronized (SymbolSearchPopupScreen.class) {
                    if (getThreadInstance() == null) {
                        setInstance(new SymbolSearchPopupScreen(driver));
                    }
                }
            }
        }
        return getThreadInstance();
    }

    public SymbolSearchPopupScreen(WebDriver driver) {
        super(driver);
    }

    /*
     * Search Symbol Popup Section Elements
     */
    @FindBy(xpath = "//div[@data-name='symbol-search-items-dialog']")
    private WebElement symbolSearchPopupSection;

    @FindBy(xpath = "//div[text()='Symbol Search']")
    private WebElement symbolSearchPopupTitle;

    @FindBy(xpath = "//button[@data-name='close']")
    private WebElement symbolSearchPopupCloseButton;

    @FindBy(xpath = "//input[@placeholder='Search']")
    private WebElement symbolSearchPopupSearchInputField;

    @FindBy(xpath = "//span[text()='All types']")
    private WebElement allTypesChip;

    @FindBy(xpath = "//span[text()='Stock']")
    private WebElement stockChip;

    @FindBy(xpath = "//span[text()='Index']")
    private WebElement indexChip;

    @FindBy(xpath = "//div[text()='Symbol & description']")
    private WebElement symbolAndDescriptionTextLabel;

    @FindBy(xpath = "//div[@data-name='symbol-search-dialog-content-item']")
    private List<WebElement> listedSymbolsList;

    @FindBy(xpath = "//div[text()='No symbols match your criteria']")
    private WebElement noSymbolsMatchYourCriteriaTextLabel;

    @FindBy(xpath = "//div[@data-name='sources-button']")
    private WebElement exchangeSourcesButton;

    @FindBy(xpath = "//div[text()='Sources']")
    private WebElement sourcesTextLabel;

    @FindBy(xpath = "//button[contains(@class, 'backButton')]")
    private WebElement exchangePopupBackButton;

    @FindBy(xpath = "//div[contains(@class, 'textBlock-IxKZEhmO')]")
    private List<WebElement> exchangeItemsList;

    public boolean isSymbolSearchPopupSectionDisplayed() {
        sleep(2);
        return commonActions.isElementDisplayed(symbolSearchPopupSection, "symbolSearchPopupSection");
    }

    public boolean isSymbolPopupSectionInvisible() {
        return commonActions.isElementInvisible(symbolSearchPopupSection, "symbolSearchPopupSection");
    }

    public boolean isSymbolSearchPopupTitleDisplayed() {
        return commonActions.isElementDisplayed(symbolSearchPopupTitle, "symbolSearchPopupTitle");
    }

    public String getSymbolSearchPopupTitleText() {
        return commonActions.getText(symbolSearchPopupTitle, "symbolSearchPopupTitle");
    }

    public boolean isSymbolSearchPopupCloseButtonDisplayed() {
        return commonActions.isElementDisplayed(symbolSearchPopupCloseButton, "symbolSearchPopupCloseButton");
    }

    public boolean isSymbolSearchPopupCloseButtonClickable() {
        return commonActions.isElementClickable(symbolSearchPopupCloseButton, "symbolSearchPopupCloseButton");
    }

    public void clickOnSymbolSearchPopupCloseButton() {
        commonActions.clickElement(symbolSearchPopupCloseButton, "symbolSearchPopupCloseButton");
    }

    public boolean isSymbolSearchPopupSearchInputFieldDisplayed() {
        return commonActions.isElementDisplayed(symbolSearchPopupSearchInputField, "symbolSearchPopupSearchInputField");
    }

    public void clickOnSymbolSearchPopupSearchInputField() {
        commonActions.clickElement(symbolSearchPopupSearchInputField, "symbolSearchPopupSearchInputField");
    }

    public void enterTextToSymbolSearchPopupSearchInputField(String text) {
        commonActions.sendKeys(symbolSearchPopupSearchInputField, text, "symbolSearchPopupSearchInputField");
    }

    public boolean isAllTypesChipDisplayed() {
        return commonActions.isElementDisplayed(allTypesChip, "allTypesChip");
    }

    public boolean isAllTypesChipClickable() {
        return commonActions.isElementClickable(allTypesChip, "allTypesChip");
    }

    public void clickOnAllTypesChip() {
        commonActions.clickElement(allTypesChip, "allTypesChip");
    }

    public boolean isStockChipDisplayed() {
        return commonActions.isElementDisplayed(stockChip, "stockChip");
    }

    public boolean isStockChipClickable() {
        return commonActions.isElementClickable(stockChip, "stockChip");
    }

    public void clickOnStockChip() {
        commonActions.clickElement(stockChip, "stockChip");
    }

    public boolean isIndexChipDisplayed() {
        return commonActions.isElementDisplayed(indexChip, "indexChip");
    }

    public boolean isIndexChipClickable() {
        return commonActions.isElementClickable(indexChip, "indexChip");
    }

    public void clickOnIndexChip() {
        commonActions.clickElement(indexChip, "indexChip");
    }

    public boolean isSymbolAndDescriptionTextLabelDisplayed() {
        return commonActions.isElementDisplayed(symbolAndDescriptionTextLabel, "symbolAndDescriptionTextLabel");
    }

    // Need to improve.
    public boolean isListedSymbolsListDisplayed() {
        return commonActions.areElementsDisplayed(listedSymbolsList.get(0), "listedSymbolsList");
    }

    public boolean isExchangeItemsListDisplayed() {
        return commonActions.areElementsDisplayed(exchangeItemsList.get(0), "exchangeItemsList");
    }

    public boolean isNoSymbolsMatchYourCriteriaTextLabelDisplayed() {
        return commonActions.isElementDisplayed(noSymbolsMatchYourCriteriaTextLabel,
                "noSymbolsMatchYourCriteriaTextLabel");
    }

    public String getNoSymbolsMatchYourCriteriaTextLabelText() {
        return commonActions.getText(noSymbolsMatchYourCriteriaTextLabel, "noSymbolsMatchYourCriteriaTextLabel");
    }

    public void isNoSymbolsMatchYourCriteriaTextLabelInvisible() {
        commonActions.isElementInvisible(noSymbolsMatchYourCriteriaTextLabel, "noSymbolsMatchYourCriteriaTextLabel");
    }

    public boolean isExchangeSourcesButtonDisplayed() {
        return commonActions.isElementDisplayed(exchangeSourcesButton, "exchangeSourcesButton");
    }

    public boolean isExchangeSourcesButtonClickable() {
        return commonActions.isElementClickable(exchangeSourcesButton, "exchangeSourcesButton");
    }

    public void clickOnExchangeSourcesButton() {
        commonActions.clickElement(exchangeSourcesButton, "exchangeSourcesButton");
    }

    public String getExchangeSourceName() {
        return commonActions.getText(exchangeSourcesButton, "exchangeSourcesButton");
    }

    public boolean isExchangePopupBackButtonDisplayed() {
        return commonActions.isElementDisplayed(exchangePopupBackButton, "exchangePopupBackButton");
    }

    public boolean isExchangePopupBackButtonClickable() {
        return commonActions.isElementClickable(exchangePopupBackButton, "exchangePopupBackButton");
    }

    public void clickOnExchangePopupBackButton() {
        commonActions.clickElement(exchangePopupBackButton, "exchangePopupBackButton");
    }

    public boolean isSourcesTextLabelDisplayed() {
        return commonActions.isElementDisplayed(sourcesTextLabel, "sourcesTextLabel");
    }

    public String getSourcesTextLabelText() {
        return commonActions.getText(sourcesTextLabel, "sourcesTextLabel");
    }

    public void clickOnSearchedSymbolItem(String text) {
        if (commonActions.isElementDisplayed(listedSymbolsList.get(0), "listedSearchedSymbolItem : " + text)) {
            commonActions.clickElement(commonActions.findRelativeElement(listedSymbolsList.get(0), "./div/div[2]/div",
                    "listedSearchedSymbolItem : " + text), "listedSearchedSymbolItem : " + text);
        } else {
            LOGGER.warn("Searched symbol item is not displayed: {}", text);
            throw new IllegalStateException("Searched symbol item is not displayed: " + text);
        }
    }

    // checking if the div contains the text of listed items or have to use span.
    public String getSearchedSymbolListedItemText(String text) {
        sleep(3);
        return stringActions.convertMultipleLineIntoSingleLine(
                commonActions.getText(listedSymbolsList.get(0), "listedSymbolsList"));
    }

    public String getSearchedExchangeItemText(String text) {
        sleep(3);
        return stringActions.convertMultipleLineIntoSingleLine(
                commonActions.getText(exchangeItemsList.get(0), "exchangeItemsList : " + text));
    }

    public void clickOnExchangeItem(String text) {
        sleep(1);
        if (commonActions.isElementDisplayed(exchangeItemsList.get(0), "exchangeItemsList : " + text)) {
            commonActions.clickElement(exchangeItemsList.get(0), "exchangeItemsList : " + text);
        } else {
            LOGGER.warn("Exchange popup title list is not displayed: {}", text);
            throw new IllegalStateException("Exchange popup title list is not displayed: " + text);
        }
    }

    public void selectSymbolOnChart(SoftAssertActions softAssertActions, String symbol) {
        LOGGER.info("Selecting a new symbol on chart: {}", symbol);
        clickOnSearchIcon();

        softAssertActions.assertTrue(isSymbolSearchPopupSectionDisplayed(), "Symbol Search Popup Section is displayed");
        softAssertActions.assertTrue(
                isSymbolSearchPopupSearchInputFieldDisplayed(),
                "Symbol Search Popup Search Input Field is displayed");

        enterTextToSymbolSearchPopupSearchInputField(symbol);
        String searchedSymbolItemText = getSearchedSymbolListedItemText(symbol);
        softAssertActions.assertTrue(stringActions.containsText(searchedSymbolItemText, symbol),
                "Searched symbol item text contains the symbol");

        clickOnSearchedSymbolItem(symbol);

        softAssertActions.assertTrue(isSymbolPopupSectionInvisible(), "Symbol Search Popup Section is invisible");

        softAssertActions.assertEquals(getSearchIconValue(), symbol, "Search Icon Value is correct");

        LOGGER.info("New symbol {} opened on chart successfully", symbol);
    }

    public void selectExchange(SoftAssertActions softAssertActions, String exchange) {
        LOGGER.info("Selecting an exchange: {}", exchange);
        clickOnSearchIcon();
        softAssertActions.assertTrue(isExchangeSourcesButtonDisplayed(), "Exchange Sources Button is displayed");
        softAssertActions.assertTrue(isExchangeSourcesButtonClickable(), "Exchange Sources Button is clickable");
        clickOnExchangeSourcesButton();
        sleep(1);
        softAssertActions.assertTrue(isSourcesTextLabelDisplayed(), "Sources Text Label is displayed");

        enterTextToSymbolSearchPopupSearchInputField(exchange);

        String searchedExchangeItemText = getSearchedExchangeItemText(exchange);
        softAssertActions.assertTrue(stringActions.containsText(searchedExchangeItemText, exchange),
                "Searched exchange item text contains the exchange");

        softAssertActions.assertTrue(isExchangeItemsListDisplayed(), "Exchange Items List is displayed");
        clickOnExchangeItem(exchange);

        sleep(2);
        softAssertActions.assertEquals(getExchangeSourceName(), exchange, "Exchange Source Name is correct");
    }
}
