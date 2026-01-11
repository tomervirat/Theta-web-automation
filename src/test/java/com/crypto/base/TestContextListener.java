package com.crypto.base;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.crypto.utils.ContextManager;
import com.crypto.utils.wrappers.ScreenshotUtils;

public class TestContextListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        BaseTest.setTestContext(context);
        System.out.println("TestContextListener.onStart() called for suite: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            // Ensure extent report is initialized
            if (BaseSetup.extent == null) {
                System.err.println("ERROR: Extent report is null in onTestStart. Test: " +
                        result.getMethod().getMethodName() + " will not be logged.");
                return;
            }

            // Create ExtentTest node for each test method
            String testName = result.getMethod().getMethodName();
            String className = result.getTestClass().getName();
            String fullTestName = className + "." + testName;

            System.out.println("Creating ExtentTest for: " + fullTestName);
            ExtentTest test = BaseSetup.extent.createTest(fullTestName);

            // Assign groups as categories
            String[] groups = result.getMethod().getGroups();
            if (groups != null && groups.length > 0) {
                test.assignCategory(groups);
                System.out.println("Assigned groups to test: " + String.join(", ", groups));
            }

            ContextManager.setExtentTest(test);
            System.out.println("ExtentTest created and stored successfully for: " + fullTestName);
        } catch (Exception e) {
            System.err.println(
                    "ERROR creating ExtentTest node for " + result.getMethod().getMethodName() + ": " + e.getMessage());
            e.printStackTrace();
            // Don't throw exception - let test continue
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ContextManager.getExtentTest();
        if (test != null) {
            test.log(Status.PASS, "Test Passed: " + result.getMethod().getMethodName());
            System.out.println("Test PASSED logged to Extent: " + result.getMethod().getMethodName());
        } else {
            System.err
                    .println("WARNING: ExtentTest is null in onTestSuccess for: " + result.getMethod().getMethodName());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ContextManager.getExtentTest();
        String testName = result.getMethod().getMethodName();

        if (test != null) {
            // Log the failure
            if (result.getThrowable() != null) {
                test.log(Status.FAIL, result.getThrowable());
            } else {
                test.log(Status.FAIL, "Test Failed: " + testName);
            }

            // Capture and attach failure screenshot
            try {
                ScreenshotUtils.captureFailureScreenshot(testName, result.getThrowable());
                System.out.println("Failure screenshot captured for: " + testName);
            } catch (Exception e) {
                System.err.println("Failed to capture failure screenshot for " + testName + ": " + e.getMessage());
            }

            System.out.println("Test FAILED logged to Extent: " + testName);
        } else {
            System.err.println("WARNING: ExtentTest is null in onTestFailure for: " + testName);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ContextManager.getExtentTest();
        if (test != null) {
            String reason = result.getThrowable() != null ? result.getThrowable().getMessage() : "Test was skipped";
            test.log(Status.SKIP, "Test Skipped: " + result.getMethod().getMethodName() + " - " + reason);
            System.out.println("Test SKIPPED logged to Extent: " + result.getMethod().getMethodName());
        } else {
            System.err
                    .println("WARNING: ExtentTest is null in onTestSkipped for: " + result.getMethod().getMethodName());
            // Try to create test node even if it was skipped
            try {
                if (BaseSetup.extent != null) {
                    String testName = result.getMethod().getMethodName();
                    String className = result.getTestClass().getName();
                    String fullTestName = className + "." + testName;
                    ExtentTest skippedTest = BaseSetup.extent.createTest(fullTestName);
                    skippedTest.log(Status.SKIP, "Test Skipped: " + testName);
                    System.out.println("Created ExtentTest for skipped test: " + fullTestName);
                }
            } catch (Exception e) {
                System.err.println("Failed to create ExtentTest for skipped test: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Handle flaky tests if needed
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("TestContextListener.onFinish() called. Total tests: " +
                (context.getPassedTests().size() + context.getFailedTests().size() + context.getSkippedTests().size()));
    }
}