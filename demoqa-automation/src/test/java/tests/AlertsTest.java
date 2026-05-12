package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AlertsPage;

public class AlertsTest extends BaseTest {

    @Test(description = "Click the simple alert button, capture alert text, then accept it")
    public void testSimpleAlert() {
        AlertsPage page = new AlertsPage(getDriver());
        page.navigateTo();
        page.clickSimpleAlert();

        String alertText = page.getAlertText();
        Assert.assertEquals(alertText, "You clicked a button",
                "Simple alert should display the expected message");
        page.acceptAlert();
    }

    @Test(description = "Click the confirm alert button, dismiss it, and verify the result shows Cancel")
    public void testConfirmAlertDismiss() {
        AlertsPage page = new AlertsPage(getDriver());
        page.navigateTo();
        page.clickConfirmAlert();
        page.dismissAlert();

        Assert.assertTrue(page.getConfirmResult().contains("Cancel"),
                "Confirm result should contain 'Cancel' after dismissing the alert");
    }

    @Test(description = "Click the prompt alert button, type text into it, and verify the result displays the typed text")
    public void testPromptAlert() {
        AlertsPage page = new AlertsPage(getDriver());
        page.navigateTo();
        page.clickPromptAlert();
        page.typeInAlertAndAccept("TestUser");

        Assert.assertTrue(page.getPromptResult().contains("TestUser"),
                "Prompt result should contain the text entered in the prompt alert");
    }
}
