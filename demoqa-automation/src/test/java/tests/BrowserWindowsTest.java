package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BrowserWindowsPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BrowserWindowsTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(BrowserWindowsTest.class);

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
