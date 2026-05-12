package utils;

import base.BaseTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.concurrent.ConcurrentHashMap;

public class TestListener implements ITestListener {

    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ConcurrentHashMap<Long, ExtentTest> testMap = new ConcurrentHashMap<>();

    private ExtentTest getTest() {
        return testMap.get(Thread.currentThread().getId());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();
        ExtentTest test = extent.createTest(result.getMethod().getMethodName(), description);
        testMap.put(Thread.currentThread().getId(), test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
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
        getTest().skip(result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        testMap.clear();
    }
}
