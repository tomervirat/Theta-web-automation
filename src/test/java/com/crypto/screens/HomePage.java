package com.crypto.screens;

import java.util.ArrayList;
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

public class HomePage extends BaseScreen {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomePage.class);
    public static volatile ThreadLocal<HomePage> instance = new ThreadLocal<>();
    private WebDriver driver;

    public static final Set<String> sessionIds = new HashSet<>();

    public static void setInstance(HomePage homePage) {
        instance.set(homePage);
    }

    private static HomePage getThreadInstance() {
        return instance.get();
    }

    public static HomePage getInstance() {
        WebDriver driver = BaseTest.getBaseDriver();
        if (driver == null) {
            LOGGER.warn("WebDriver is null. Cannot create HomePage instance.");
            throw new IllegalStateException("WebDriver is null. Cannot create HomePage instance.");
        }

        // Check if driver is RemoteWebDriver before calling getSessionId()
        if (driver instanceof RemoteWebDriver) {
            try {
                String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
                if (getThreadInstance() == null || !sessionIds.contains(sessionId)) {
                    synchronized (HomePage.class) {
                        if (getThreadInstance() == null || !sessionIds.contains(sessionId)) {
                            setInstance(new HomePage(driver));
                            sessionIds.add(sessionId);
                        }
                    }
                }
            } catch (Exception e) {
                // Fallback if session ID retrieval fails
                if (getThreadInstance() == null) {
                    synchronized (HomePage.class) {
                        if (getThreadInstance() == null) {
                            setInstance(new HomePage(driver));
                        }
                    }
                }
            }
        } else {
            // For non-RemoteWebDriver, use simple thread-local check
            if (getThreadInstance() == null) {
                synchronized (HomePage.class) {
                    if (getThreadInstance() == null) {
                        setInstance(new HomePage(driver));
                    }
                }
            }
        }

        return getThreadInstance();
    }

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//div[contains(text(), 'Trade on Thetaswap')]")
    private WebElement tradeOnThetaswapButton;

    @FindBy(xpath = "//button[contains(text(), 'Select Token/Contract Address')]")
    private WebElement selectTokenContractAddressButton;

    @FindBy(xpath = "//input[contains(@placeholder, 'Select Token/Contract Address ⌄')]")
    private WebElement searchTokenContractAddressInputField;

    @FindBy(xpath = "//iframe[contains(@name,'tradingview')]")
    private WebElement tradingViewChartIframe;

    @FindBy(xpath = "//div[@class='table-container font-header']/table")
    private WebElement tokenListTable;

    @FindBy(xpath = "//div[@class='table-container font-header']")
    private WebElement tokenListTableContainer;

    @FindBy(xpath = "//div[@class='table-container font-header']/table/thead/tr/th")
    private List<WebElement> tokenListTableHeaders;

    @FindBy(xpath = "//div[@class='table-container font-header']/table/tbody/tr")
    private List<WebElement> tokenListTableRows;

    public boolean isOnHomePage() {
        return isTradeOnThetaswapButtonDisplayed();
    }

    public boolean isTradeOnThetaswapButtonDisplayed() {
        return commonActions.isElementDisplayed(tradeOnThetaswapButton, "tradeOnThetaswapButton");
    }

    public boolean isTradeOnThetaswapButtonClickable() {
        return commonActions.isElementClickable(tradeOnThetaswapButton, "tradeOnThetaswapButton");
    }

    public void clickOnTradeOnThetaswapButton() {
        commonActions.clickElement(tradeOnThetaswapButton, "tradeOnThetaswapButton");
    }

    public boolean isSelectTokenContractAddressButtonDisplayed() {
        return commonActions.isElementDisplayed(selectTokenContractAddressButton, "selectTokenContractAddressButton");
    }

    public boolean isSelectTokenContractAddressButtonClickable() {
        return commonActions.isElementClickable(selectTokenContractAddressButton, "selectTokenContractAddressButton");
    }

    public void clickOnSelectTokenContractAddressButton() {
        commonActions.clickElement(selectTokenContractAddressButton, "selectTokenContractAddressButton");
    }

    public boolean isSearchTokenContractAddressInputFieldDisplayed() {
        return commonActions.isElementDisplayed(searchTokenContractAddressInputField,
                "searchTokenContractAddressInputField");
    }

    public boolean isSearchTokenContractAddressInputFieldClickable() {
        return commonActions.isElementClickable(searchTokenContractAddressInputField,
                "searchTokenContractAddressInputField");
    }

    public void enterTextToSearchTokenContractAddressInputField(String text) {
        commonActions.sendKeys(searchTokenContractAddressInputField, text, "searchTokenContractAddressInputField");
    }

    public String getTextFromTokenContractAddressInputField() {
        return commonActions.getAttribute(searchTokenContractAddressInputField, "value",
                "searchTokenContractAddressInputField");
    }

    public boolean isTokenListTableInvisible() {
        return commonActions.isElementInvisible(tokenListTable, "tokenListTable");
    }

    public void collapseTokenListTable(SoftAssertActions softAssertActions) {
        commonActions.clickElement(searchTokenContractAddressInputField, getTextFromTokenContractAddressInputField());
        softAssertActions.assertTrue(isTokenListTableInvisible(), "Token list table is collapsed");
    }

    public List<String> getTableHeaders() {
        List<String> tableHeaders = new ArrayList<>();
        for (WebElement header : tokenListTableHeaders) {
            tableHeaders.add(commonActions.getText(header, "header"));
            System.out.println("------> " + commonActions.getText(header, "header"));
        }
        return tableHeaders;
    }

    public List<List<String>> getTableData() {
        List<List<String>> tableData = new ArrayList<>();
        int columnCount = tokenListTableHeaders.size();
        for (WebElement row : tokenListTableRows) {
            List<String> rowData = new ArrayList<>();
            for (int i = 0; i < columnCount; i++) {
                String tokenName;
                String tokenProfitLoss;
                if (i == 0) {
                    tokenName = commonActions.getText(commonActions.findRelativeElement(row, "./td/div", "tokenName"),
                            "tokenName");
                    tokenProfitLoss = commonActions.getText(
                            commonActions.findRelativeElement(row, "./td/span", "tokenProfitLoss"), "tokenProfitLoss");
                    rowData.add(tokenName + " " + tokenProfitLoss);
                } else {
                    rowData.add(commonActions.getText(
                            commonActions.findRelativeElement(row, String.format("./td[%d]", i + 1), "row[" + i + "]"),
                            "row[" + i + "]"));
                }
            }
            tableData.add(rowData);
        }
        return tableData;
    }

    public List<String> getSearchedRowData(String name) {
        List<String> rowData = new ArrayList<>();
        int columnCount = tokenListTableHeaders.size();
        for (int i = 0; i < columnCount; i++) {
            String tokenName;
            String tokenProfitLoss;
            if (i == 0) {
                tokenName = commonActions.getText(
                        commonActions.findRelativeElement(tokenListTableRows.get(0), "./td/div", "tokenName"),
                        "tokenName");
                tokenProfitLoss = commonActions.getText(
                        commonActions.findRelativeElement(tokenListTableRows.get(0), "./td/span", "tokenProfitLoss"),
                        "tokenProfitLoss");
                rowData.add(tokenName + " " + tokenProfitLoss);
            } else {
                rowData.add(commonActions.getText(commonActions.findRelativeElement(tokenListTableRows.get(0),
                        String.format("./td[%d]", i + 1), "row[" + i + "]"), "row[" + i + "]"));
            }
        }

        if (rowData.get(0).contains(name)) {
            return rowData;
        } else {
            return null;
        }
    }

    public void printTableData(List<List<String>> tableData) {
        for (List<String> row : tableData) {
            System.out.print("row:: ");
            for (String cell : row) {
                System.out.print(cell + " | ");
            }
            System.out.println();
        }
    }

    public void selectSearchedToken(SoftAssertActions softAssertActions, String tokenName) {
        WebElement tokenToSelement = commonActions.findRelativeElement(tokenListTableRows.get(0), "./td/div",
                tokenName);
        softAssertActions.assertNotNull(tokenToSelement, "Token to select is found");
        commonActions.clickElement(tokenToSelement, "tokenToSelement");
        softAssertActions.assertTrue(commonActions.isElementClickable(tokenToSelement, tokenName),
                "Token is clickable " + tokenName);
        commonActions.clickElement(tokenToSelement, tokenName);
        softAssertActions.assertTrue(isTokenListTableInvisible(),
                "Token is selected and token list table is collapsed");
    }

    public void checkTokenListTableScrollable(SoftAssertActions softAssertActions) {
        softAssertActions.assertTrue(commonActions.isContainerScrollable(tokenListTableContainer),
                "Token list table is scrollable");
        sleep(3);
        commonActions.scrollDownInContainer(tokenListTableContainer, 100);
    }

    public void switchToChartsIframe() {
        commonActions.switchToFrame(tradingViewChartIframe, "tradingViewChartIframe");
    }

    public void verifyTokenSearchAndSelection(SoftAssertActions softAssertActions, String tokenName) {
        clickOnSelectTokenContractAddressButton();
        sleep(3);
        softAssertActions.assertTrue(isSearchTokenContractAddressInputFieldDisplayed(),
                "Search Token/Contract Address Input Field is displayed");
        softAssertActions.assertTrue(isSearchTokenContractAddressInputFieldClickable(),
                "Search Token/Contract Address Input Field is clickable");
        enterTextToSearchTokenContractAddressInputField(tokenName);
        softAssertActions.assertEquals(getTextFromTokenContractAddressInputField(), tokenName,
                "Token name is entered correctly");
        sleep(2);
        softAssertActions.assertNotNull(getSearchedRowData(tokenName), "Token is searched and visible");
        selectSearchedToken(softAssertActions, tokenName);
        collapseTokenListTable(softAssertActions);
    }
}