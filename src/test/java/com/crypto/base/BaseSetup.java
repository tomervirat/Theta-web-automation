package com.crypto.base;

import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.crypto.report.ExtentReportManager;
import com.crypto.utils.ContextManager;

public class BaseSetup {

    public static ExtentReports extent;
    public Set<String> processId;

    // @Getter
    // private final CommonActions commonActions;

    public BaseSetup() {
        // commonActions = new CommonActions(BaseTest.getBaseDriver());
    }

    public void loadExtentFile() {
        extent = ExtentReportManager.setupExtentReport();
    }

    public void configExtentTest(String className) {
        ExtentTest parent = extent.createTest(className);
        ContextManager.setExtentTest(parent);
    }

    public static void assignCategoryToTest(String categoryName) {
        ExtentTest loggerReport = ContextManager.getExtentTest();
        loggerReport.assignCategory(categoryName);
    }

    public void flushExtentReport(ITestContext context) {
        int passedCount = context.getPassedTests().size();
        int failedCount = context.getFailedTests().size();
        int skippedCount = context.getSkippedTests().size();
        int totalCount = passedCount + failedCount + skippedCount;

        double passPercentage = 0;

        if (totalCount > 0) {
            passPercentage = ((double) passedCount / totalCount) * 100;
        } else {
            System.out.println("WARNING: No tests were executed. Total count is 0.");
        }

        System.out.println("Pre-flush");
        System.out.println("Total tests: " + totalCount + " (Passed: " + passedCount + ", Failed: " + failedCount
                + ", Skipped: " + skippedCount + ")");

        String summaryFormat = String.format(
                "[INFO] Tests run: %d, Passed: %d, Failures: %d, Errors: 0, Skipped: %d",
                totalCount, passedCount, failedCount, skippedCount);
        System.out.println(summaryFormat);

        if (failedCount > 0) {
            System.err.println(summaryFormat.replace("[INFO]", "[ERROR]"));
        }

        if (extent != null) {
            extent.setSystemInfo("Pass Percentage", String.valueOf(passPercentage));
            extent.setSystemInfo("Total Tests", String.valueOf(totalCount));
            extent.setSystemInfo("Passed Tests", String.valueOf(passedCount));
            extent.setSystemInfo("Failed Tests", String.valueOf(failedCount));
            extent.setSystemInfo("Skipped Tests", String.valueOf(skippedCount));
            extent.flush();
            System.out.println("Post-flush");
            System.out.println("PassPercentage: " + passPercentage);
        } else {
            System.err.println("ERROR: Extent report is null, cannot flush!");
        }
        System.setProperty("passPercentage", String.valueOf(passPercentage));
    }

    public void testResultCapture(ITestResult result) {
        if (result.getStatus() == ITestResult.SUCCESS) {
            ContextManager.getExtentTest().log(Status.PASS, result.getMethod().getMethodName() + " Passed");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            try {
                if (result.getThrowable() != null) {
                    ContextManager.getExtentTest().log(Status.FAIL, result.getThrowable());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (result.getStatus() == ITestResult.SKIP) {
            ContextManager.getExtentTest().log(Status.SKIP, result.getMethod().getMethodName() + " Skipped");
        }
    }
}
