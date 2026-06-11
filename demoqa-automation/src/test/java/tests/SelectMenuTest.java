package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SelectMenuPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("Widgets")
@Feature("Select Menu")
public class SelectMenuTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(SelectMenuTest.class);

    @Story("Select option from dropdown and verify active selection")
    @Description("Select an option from the old-style HTML select dropdown and verify the chosen value is active")
    @Test(description = "Select an option from the old-style HTML select dropdown and verify the chosen value is active",
            groups = {"regression"})
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
