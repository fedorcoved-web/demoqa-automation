package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CheckBoxPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CheckBoxTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(CheckBoxTest.class);

    @Test(description = "Expand the checkbox tree, select the Home node, and verify result section shows home selected")
    public void testSelectHomeCheckbox() {
        log.info("Starting test: testSelectHomeCheckbox");
        CheckBoxPage page = new CheckBoxPage(getDriver());
        page.navigateTo();
        page.selectHomeCheckbox(); // Home is the root node, visible without expanding

        Assert.assertTrue(page.isResultDisplayed(),
                "Result section should be visible after selecting a checkbox");
        Assert.assertEquals(page.getFirstSelectedItem().toLowerCase(), "home",
                "First selected item in result should be 'home'");
        log.info("Test completed: testSelectHomeCheckbox");
    }
}
