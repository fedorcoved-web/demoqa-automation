package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.RadioButtonPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RadioButtonTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(RadioButtonTest.class);

    @Test(description = "Select the Impressive radio button and verify the success message displays Impressive")
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
