package com.crypto.test;

import org.testng.annotations.Test;

import com.crypto.base.BaseTest;
import com.crypto.constants.Constants;
import com.crypto.dataProviders.HomePageDataProvider;
import com.crypto.screens.HomePage;
import com.crypto.screens.SymbolSearchPopupScreen;
import com.crypto.utils.SoftAssertActions;

public class HomeScreenTest extends BaseTest {

        @Test(priority = 1, description = "Verify the home screen UI", groups = {
                        Constants.GROUP_SANITY,
                        Constants.GROUP_REGRESSION }, enabled = true, dataProviderClass = HomePageDataProvider.class, dataProvider = "tokenData")
        public void testHomeScreen(String tokenName) {
                SoftAssertActions softAssertActions = new SoftAssertActions();
                HomePage.getInstance().openHomePage();
                softAssertActions.assertTrue(HomePage.getInstance().isSelectTokenContractAddressButtonDisplayed(),
                                "Select Token/Contract Address Button is displayed");
                softAssertActions.assertTrue(HomePage.getInstance().isSelectTokenContractAddressButtonClickable(),
                                "Select Token/Contract Address Button is clickable");
                HomePage.getInstance().clickOnSelectTokenContractAddressButton();
                softAssertActions.assertTrue(HomePage.getInstance().isSearchTokenContractAddressInputFieldDisplayed(),
                                "Search Token/Contract Address Input Field is displayed");
                softAssertActions.assertTrue(HomePage.getInstance().isSearchTokenContractAddressInputFieldClickable(),
                                "Search Token/Contract Address Input Field is clickable");

                sleep(3);
                HomePage.getInstance().enterTextToSearchTokenContractAddressInputField(tokenName);
                softAssertActions.assertEquals(HomePage.getInstance().getTextFromTokenContractAddressInputField(),
                                tokenName,
                                "Token name is entered correctly");
                sleep(3);

                HomePage.getInstance().collapseTokenListTable(softAssertActions);
                softAssertActions.assertAll();
        }

        @Test(priority = 2, description = "Verify the scroll in  Token List Table", groups = {
                        Constants.GROUP_SANITY,
                        Constants.GROUP_REGRESSION }, enabled = true)
        public void testSearchedRowData() {
                SoftAssertActions softAssertActions = new SoftAssertActions();
                HomePage.getInstance().openHomePage();
                softAssertActions.assertTrue(HomePage.getInstance().isSelectTokenContractAddressButtonDisplayed(),
                                "Select Token/Contract Address Button is displayed");
                HomePage.getInstance().clickOnSelectTokenContractAddressButton();
                sleep(3);
                HomePage.getInstance().checkTokenListTableScrollable(softAssertActions);
                sleep(10);
                softAssertActions.assertAll();
        }

        @Test(priority = 3, description = "Verify the opening of a symbol on chart", groups = {
                        Constants.GROUP_SANITY,
                        Constants.GROUP_REGRESSION }, enabled = true, dataProviderClass = HomePageDataProvider.class, dataProvider = "symbolData")
        public void testSwitchContext(String symbol) {

                SoftAssertActions softAssertActions = new SoftAssertActions();
                HomePage.getInstance().openHomePage();
                softAssertActions.assertTrue(HomePage.getInstance().isSelectTokenContractAddressButtonDisplayed(),
                                "Select Token/Contract Address Button is displayed");

                HomePage.getInstance().switchToChartsIframe();

                SymbolSearchPopupScreen.getInstance().selectSymbolOnChart(softAssertActions, symbol);
                HomePage.getInstance().switchToDefaultContent();

                softAssertActions.assertAll();
        }

        @Test(priority = 4, description = "Verify changing the exchange for the symbols.", groups = {
                        Constants.GROUP_SANITY,
                        Constants.GROUP_REGRESSION }, enabled = true, dataProviderClass = HomePageDataProvider.class, dataProvider = "exchangeData")
        public void testChangeExchange(String exchange) {
                SoftAssertActions softAssertActions = new SoftAssertActions();
                HomePage.getInstance().openHomePage();
                HomePage.getInstance().switchToChartsIframe();

                SymbolSearchPopupScreen.getInstance().selectExchange(softAssertActions, exchange);
        }

        @Test(priority = 5, description = "Verify the selection of a token", groups = {
                        Constants.GROUP_SANITY,
                        Constants.GROUP_REGRESSION }, enabled = true, dataProviderClass = HomePageDataProvider.class, dataProvider = "tokenData")
        public void verifyTokenSelection(String token) {
                SoftAssertActions softAssertActions = new SoftAssertActions();
                HomePage.getInstance().openHomePage();
                HomePage.getInstance().verifyTokenSearchAndSelection(softAssertActions, token);
                softAssertActions.assertAll();
        }
}