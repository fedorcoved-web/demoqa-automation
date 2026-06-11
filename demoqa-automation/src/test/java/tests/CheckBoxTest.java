package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CheckBoxPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("Elements")
@Feature("Check Box")
public class CheckBoxTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(CheckBoxTest.class);

    @Story("Select home checkbox and verify result")
    @Description("Expand the checkbox tree, select the Home node, and verify result section shows home selected")
    @Test(description = "Expand the checkbox tree, select the Home node, and verify result section shows home selected",
            groups = {"sanity", "smoke", "regression"})
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
