package com.crypto.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.crypto.Driver.DriverInstance;
import com.crypto.constants.ConstantsInitializer;
import com.crypto.utils.ContextManager;

public class BaseTest extends BaseSetup {

    public static volatile ThreadLocal<BaseTest> instance = new ThreadLocal<>();
    public static volatile Set<String> sessionIds = new HashSet<>();
    private static ITestContext testContext;

    public static Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;

    public static void setInstance(BaseTest baseTest) {
        instance.set(baseTest);
    }

    private static BaseTest getThreadInstance() {
        return instance.get();
    }

    public static BaseTest getInstance() {
        WebDriver driver = getBaseDriver();
        if (driver == null) {
            LOGGER.warn("Driver is null, cannot get instance");
            return null;
        }

        if (driver instanceof RemoteWebDriver) {
            String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
            if (getThreadInstance() == null || !sessionIds.contains(sessionId)) {
                synchronized (BaseTest.class) {
                    if (getThreadInstance() == null) {
                        setInstance(new BaseTest());
                    }
                }
                sessionIds.add(sessionId);
            }
        } else {
            if (getThreadInstance() == null) {
                synchronized (BaseTest.class) {
                    if (getThreadInstance() == null) {
                        setInstance(new BaseTest());
                    }
                }
            }
        }

        return getThreadInstance();
    }

    public static WebDriver getBaseDriver() {
        return ContextManager.getDriver();
    }

    public static void setDriver(WebDriver driver) {
        ContextManager.setDriver(driver);
    }

    public static void setTestContext(ITestContext context) {
        testContext = context;
    }

    public static ITestContext getTestContext() {
        return testContext;
    }

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() {
        try {
            ConstantsInitializer.initialize();
            getEnvSetup();
            loadExtentFile();

            // Verify extent is initialized
            if (extent == null) {
                LOGGER.error("Failed to initialize Extent report!");
                throw new RuntimeException("Extent report initialization failed");
            }
            LOGGER.info("Extent report initialized successfully");
        } catch (Exception e) {
            LOGGER.error("Error in setUpSuite: ", e);
            throw e; // Re-throw to fail fast
        }
    }

    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        initializeDriver();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpMethod() {
        WebDriver driver = getBaseDriver();
        if (driver == null) {
            initializeDriver();
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        driverQuit();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        try {
            if (testContext != null) {
                int totalTests = testContext.getPassedTests().size() +
                        testContext.getFailedTests().size() +
                        testContext.getSkippedTests().size();
                LOGGER.info("Flushing Extent Report. Total tests executed: {}", totalTests);
                flushExtentReport(testContext);
            } else {
                LOGGER.warn("TestContext is null, flushing extent report without context");
                if (extent != null) {
                    LOGGER.info("Flushing Extent Report directly (no context)");
                    extent.flush();
                } else {
                    LOGGER.error("Extent report is null, cannot flush!");
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error flushing Extent Report: ", e);
            // Try to flush anyway
            if (extent != null) {
                try {
                    extent.flush();
                } catch (Exception ex) {
                    LOGGER.error("Failed to flush extent report even after error: ", ex);
                }
            }
        }
        driverQuit();
    }

    private void initializeDriver() {
        WebDriver driver = getBaseDriver();
        if (driver != null) {
            LOGGER.info("Driver already exists for thread: {}", Thread.currentThread().getId());
            return;
        }

        try {
            LOGGER.info("Initializing WebDriver for thread: {}", Thread.currentThread().getId());
            driver = DriverInstance.createDriver();

            if (driver != null) {
                setDriver(driver);
                LOGGER.info("WebDriver initialized successfully for thread: {}", Thread.currentThread().getId());
            } else {
                LOGGER.error("Failed to create WebDriver instance for thread: {}", Thread.currentThread().getId());
                throw new RuntimeException("Failed to create WebDriver instance");
            }
        } catch (Exception e) {
            LOGGER.error("Error initializing WebDriver for thread {}: ", Thread.currentThread().getId(), e);
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    public Map<Object, Object> getEnvSetup() {
        Map<Object, Object> envSetup = new HashMap<>();
        envSetup.put("env", System.getProperty("env", "qa"));
        envSetup.put("browser", System.getProperty("browser", "chrome"));
        return envSetup;
    }

    private void driverQuit() {
        WebDriver drv = getBaseDriver();
        if (drv == null) {
            LOGGER.debug("No WebDriver to quit.");
            return;
        }

        String sessionId = null;
        try {
            // attempt to capture session id (if available) so we can remove it from
            // sessionIds
            try {
                if (drv instanceof RemoteWebDriver) {
                    Object sid = ((RemoteWebDriver) drv).getSessionId();
                    sessionId = sid != null ? sid.toString() : null;
                }
            } catch (Exception e) {
                LOGGER.debug("Unable to read session id before quit", e);
            }

            drv.quit();
            LOGGER.info("WebDriver instance quit successfully.");

        } catch (Exception e) {
            LOGGER.error("Error occurred while quitting WebDriver: ", e);
        } finally {
            try {
                // clear thread-local driver reference
                setDriver(null);
            } catch (Exception ex) {
                LOGGER.debug("Error clearing driver from context manager", ex);
            }

            // remove stored session id if present
            if (sessionId != null) {
                sessionIds.remove(sessionId);
            }
        }
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
