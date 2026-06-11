package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.RadioButtonPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("Elements")
@Feature("Radio Button")
public class RadioButtonTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(RadioButtonTest.class);

    @Story("Select radio button option and verify confirmation message")
    @Description("Select the Impressive radio button and verify the success message displays Impressive")
    @Test(description = "Select the Impressive radio button and verify the success message displays Impressive",
            groups = {"sanity", "smoke", "regression"})
    public void testSelectImpressiveRadio() {
        log.info("Starting test: testSelectImpressiveRadio");
        RadioButtonPage page = new RadioButtonPage(getDriver());
        page.navigateTo();
        page.selectImpressive();

        Assert.assertEquals(page.getSuccessText(), "Impressive",
                "Success text should display 'Impressive' after selecting that option");
        log.info("Test completed: testSelectImpressiveRadio");
    }
}
