package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DynamicPropertiesPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("Widgets")
@Feature("Dynamic Properties")
public class DynamicPropertiesTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(DynamicPropertiesTest.class);

    @Story("Wait for element to become visible and click it")
    @Description("Navigate to Dynamic Properties page, wait for the visibleAfter button to appear, click it, and verify it is displayed")
    @Test(description = "Navigate to Dynamic Properties page, wait for the visibleAfter button to appear, click it, and verify it is displayed",
            groups = {"smoke", "regression"})
    public void testVisibleAfterButton() {
        log.info("Starting test: testVisibleAfterButton");
        DynamicPropertiesPage page = new DynamicPropertiesPage(getDriver());
        page.navigateTo();
        page.clickVisibleAfterButton();

        Assert.assertTrue(page.isButtonVisible(),
                "The visibleAfter button should be displayed after waiting for it to appear");
        log.info("Test completed: testVisibleAfterButton");
    }
}
