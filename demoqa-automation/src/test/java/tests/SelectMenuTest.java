package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SelectMenuPage;

public class SelectMenuTest extends BaseTest {

    @Test(description = "Select an option from the old-style HTML select dropdown and verify the chosen value is active")
    public void testSelectFromDropdown() {
        SelectMenuPage page = new SelectMenuPage(getDriver());
        page.navigateTo();
        page.selectFromOldMenu("Blue");

        Assert.assertEquals(page.getSelectedOption(), "Blue",
                "Selected dropdown option should be 'Blue'");
    }
}
