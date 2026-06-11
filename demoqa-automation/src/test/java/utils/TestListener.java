package utils;

import base.BaseTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.model.StatusDetails;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);
    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ThreadLocal<ExtentTest> testLocal = new ThreadLocal<>();

    private ExtentTest getTest() {
        return testLocal.get();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();
        ExtentTest test = extent.createTest(result.getMethod().getMethodName(), description);
        testLocal.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        IRetryAnalyzer analyzer = result.getMethod().getRetryAnalyzer(result);
        if (analyzer instanceof RetryAnalyzer ra && ra.isRetryAvailable(result)) {
            log.warn("Test '{}' failed — retry attempt, skipping screenshot",
                    result.getMethod().getMethodName());
            Allure.getLifecycle().updateTestCase(tc -> {
                tc.setStatus(io.qameta.allure.model.Status.SKIPPED);
                StatusDetails details = new StatusDetails();
                details.setMessage("Retry attempt — test will be re-run");
                tc.setStatusDetails(details);
            });
            return;
        }

        log.error("Test failed: {} - {}", result.getMethod().getMethodName(), result.getThrowable().getMessage());
        ExtentTest test = getTest();
        test.fail(result.getThrowable());
        try {
            WebDriver driver = BaseTest.getDriver();
            if (driver != null) {
                String screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                test.fail("Screenshot on failure:",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build());
            }
        } catch (Exception e) {
            test.fail("Could not capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = getTest();
        if (test == null) return;
        Throwable t = result.getThrowable();
        if (t != null) {
            test.skip(t);
        } else {
            test.skip("Test skipped");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        testLocal.remove();
    }
}
