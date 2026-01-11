package com.crypto.dataProviders;

import org.testng.annotations.DataProvider;

public class HomePageDataProvider {

    @DataProvider(name = "tokenData")
    public static Object[][] tokenData() {
        return new Object[][] {
                { "TFUEL" } };
    }

    @DataProvider(name = "symbolData")
    public static Object[][] symbolData() {
        return new Object[][] {
                { "AAL" },
                { "AAPL" } };
    }

    @DataProvider(name = "exchangeData")
    public static Object[][] exchangeData() {
        return new Object[][] {
                { "NasdaqNM" },
                { "NYSE" } };
    }

}
