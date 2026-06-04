package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BrowserWindowsPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("Alerts, Frame & Windows")
@Feature("Browser Windows")
public class BrowserWindowsTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(BrowserWindowsTest.class);

    @Story("Open new tab and verify sample page URL and heading")
    @Description("Click New Tab button, switch to the opened tab, and verify its URL contains the expected path")
    @Test(description = "Click New Tab button, switch to the opened tab, and verify its URL contains the expected path")
    public void testOpenNewTab() {
        log.info("Starting test: testOpenNewTab");
        BrowserWindowsPage page = new BrowserWindowsPage(getDriver());
        page.navigateTo();
        page.clickNewTab();
        page.switchToNewTab();

        Assert.assertTrue(page.getCurrentUrl().contains("sample"),
                "New tab URL should contain 'sample'");
        Assert.assertEquals(page.getSampleHeadingText(), "This is a sample page",
                "New tab heading should display the sample page message");
        log.info("Test completed: testOpenNewTab");
    }
}
