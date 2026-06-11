package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AlertsPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("Alerts, Frame & Windows")
@Feature("Alerts")
public class AlertsTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(AlertsTest.class);

    @Story("Accept simple browser alert and verify message")
    @Description("Click the simple alert button, capture alert text, then accept it")
    @Test(description = "Click the simple alert button, capture alert text, then accept it",
            groups = {"smoke", "regression"})
    public void testSimpleAlert() {
        log.info("Starting test: testSimpleAlert");
        AlertsPage page = new AlertsPage(getDriver());
        page.navigateTo();
        page.clickSimpleAlert();

        String alertText = page.getAlertText();
        Assert.assertEquals(alertText, "You clicked a button",
                "Simple alert should display the expected message");
        page.acceptAlert();
        log.info("Test completed: testSimpleAlert");
    }

    @Story("Dismiss confirm alert and verify cancel result")
    @Description("Click the confirm alert button, dismiss it, and verify the result shows Cancel")
    @Test(description = "Click the confirm alert button, dismiss it, and verify the result shows Cancel",
            groups = {"smoke", "regression"})
    public void testConfirmAlertDismiss() {
        log.info("Starting test: testConfirmAlertDismiss");
        AlertsPage page = new AlertsPage(getDriver());
        page.navigateTo();
        page.clickConfirmAlert();
        page.dismissAlert();

        Assert.assertTrue(page.getConfirmResult().contains("Cancel"),
                "Confirm result should contain 'Cancel' after dismissing the alert");
        log.info("Test completed: testConfirmAlertDismiss");
    }

    @Story("Type into prompt alert and verify echoed result")
    @Description("Click the prompt alert button, type text into it, and verify the result displays the typed text")
    @Test(description = "Click the prompt alert button, type text into it, and verify the result displays the typed text",
            groups = {"smoke", "regression"})
    public void testPromptAlert() {
        log.info("Starting test: testPromptAlert");
        AlertsPage page = new AlertsPage(getDriver());
        page.navigateTo();
        page.clickPromptAlert();
        page.typeInAlertAndAccept("TestUser");

        Assert.assertTrue(page.getPromptResult().contains("TestUser"),
                "Prompt result should contain the text entered in the prompt alert");
        log.info("Test completed: testPromptAlert");
    }
}
