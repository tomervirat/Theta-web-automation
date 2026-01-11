package com.crypto.report;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.crypto.constants.Constants;

public class ExtentReportManager {

        private static final Logger LOGGER = LoggerFactory.getLogger(ExtentReportManager.class);

        public static ExtentReports setupExtentReport() {

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                String actualDate = format.format(date);

                ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(
                                Constants.EXTENT_REPORT_PATH + "ExtentReport_" + actualDate + ".html");

                ExtentReports extent = new ExtentReports();

                LOGGER.info("Extent Report Object is created.");
                extent.attachReporter(extentSparkReporter);
                LOGGER.info("Extent Spark Reporter is attached to Extent Report.");

                extent.setSystemInfo(Constants.CURRENT_DIR, System.getProperty("user.name"));
                LOGGER.info("System info added to Extent Report.");

                extent.setSystemInfo(Constants.CONFIG_KEY_HOST, Constants.HOST);
                LOGGER.info("[{0}] Host name is set to ExtentReport.", Constants.HOST);

                extent.setSystemInfo(Constants.CONFIG_KEY_ENVIRONMENT, Constants.ENVIRONMENT);
                LOGGER.info("Environment name [{0}] is set to ExtentReport.", Constants.ENVIRONMENT);

                extent.setSystemInfo(Constants.CONFIG_KEY_PLATFORM, Constants.PLATFORM.toString());
                LOGGER.info("Platform name [{0}] is set to ExtentReport.", Constants.PLATFORM);

                extent.setSystemInfo(Constants.CONFIG_KEY_BROWSER, Constants.BROWSER);
                LOGGER.info("Browser name [{0}] is set to ExtentReport.", Constants.BROWSER);

                extentSparkReporter.config().setDocumentTitle("Crypto Assignment Web Automation Test Report");
                extentSparkReporter.config().setTheme(Theme.DARK);
                extentSparkReporter.config()
                                .setReportName("Web Automation " + Constants.ENVIRONMENT.toUpperCase() + " "
                                                + Constants.BROWSER.toUpperCase());

                extentSparkReporter.config().setCss(
                                ".header { background-color:rgb(49, 49, 49) !important; }" +
                                                ".nav-tabs .nav-item .nav-link.active { background-color: #FF5722 !important; color: #FFFFFF !important; }"
                                                +
                                                ".tab-content { background-color: #2C2C2C !important; color: #FFFFFF !important; }");

                return extent;
        }
}
