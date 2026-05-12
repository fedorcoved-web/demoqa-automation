package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CheckBoxPage;

public class CheckBoxTest extends BaseTest {

    @Test(description = "Expand the checkbox tree, select the Home node, and verify result section shows home selected")
    public void testSelectHomeCheckbox() {
        CheckBoxPage page = new CheckBoxPage(getDriver());
        page.navigateTo();
        page.selectHomeCheckbox(); // Home is the root node, visible without expanding

        Assert.assertTrue(page.isResultDisplayed(),
                "Result section should be visible after selecting a checkbox");
        Assert.assertEquals(page.getFirstSelectedItem().toLowerCase(), "home",
                "First selected item in result should be 'home'");
    }
}
