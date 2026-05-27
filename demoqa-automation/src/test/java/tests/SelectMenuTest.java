package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SelectMenuPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SelectMenuTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(SelectMenuTest.class);

    @Test(description = "Select an option from the old-style HTML select dropdown and verify the chosen value is active")
    public void testSelectFromDropdown() {
        log.info("Starting test: testSelectFromDropdown");
        SelectMenuPage page = new SelectMenuPage(getDriver());
        page.navigateTo();
        page.selectFromOldMenu("Blue");

        Assert.assertEquals(page.getSelectedOption(), "Blue",
                "Selected dropdown option should be 'Blue'");
        log.info("Test completed: testSelectFromDropdown");
    }
}
