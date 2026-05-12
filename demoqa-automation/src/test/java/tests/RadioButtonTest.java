package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.RadioButtonPage;

public class RadioButtonTest extends BaseTest {

    @Test(description = "Select the Impressive radio button and verify the success message displays Impressive")
    public void testSelectImpressiveRadio() {
        RadioButtonPage page = new RadioButtonPage(getDriver());
        page.navigateTo();
        page.selectImpressive();

        Assert.assertEquals(page.getSuccessText(), "Impressive",
                "Success text should display 'Impressive' after selecting that option");
    }
}
