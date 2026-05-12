package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BrowserWindowsPage;

public class BrowserWindowsTest extends BaseTest {

    @Test(description = "Click New Tab button, switch to the opened tab, and verify its URL contains the expected path")
    public void testOpenNewTab() {
        BrowserWindowsPage page = new BrowserWindowsPage(getDriver());
        page.navigateTo();
        page.clickNewTab();
        page.switchToNewTab();

        Assert.assertTrue(page.getCurrentUrl().contains("sample"),
                "New tab URL should contain 'sample'");
        Assert.assertEquals(page.getSampleHeadingText(), "This is a sample page",
                "New tab heading should display the sample page message");
    }
}
