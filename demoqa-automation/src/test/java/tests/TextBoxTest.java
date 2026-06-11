package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.JsonDataProvider;
import pages.TextBoxPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("Elements")
@Feature("Text Box")
public class TextBoxTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(TextBoxTest.class);

    @DataProvider(name = "textBoxData")
    public Object[][] textBoxData() {
        return JsonDataProvider.readTextBoxData();
    }

    @Story("Fill and verify text box form output")
    @Description("Fill TextBox form with name, email and addresses, then verify output section displays all values correctly")
    @Test(description = "Fill TextBox form with name, email and addresses, then verify output section displays all values correctly",
            groups = {"sanity", "smoke", "regression"},
            dataProvider = "textBoxData")
    public void testFillTextBoxForm(String name, String email, String currentAddress, String permanentAddress) {
        log.info("Starting test: testFillTextBoxForm");
        TextBoxPage page = new TextBoxPage(getDriver());
        page.navigateTo();
        page.fillForm(name, email, currentAddress, permanentAddress);
        page.submit();

        Assert.assertTrue(page.getOutputName().contains(name),
                "Output name should contain the submitted full name");
        Assert.assertTrue(page.getOutputEmail().contains(email),
                "Output email should contain the submitted email");
        Assert.assertTrue(page.getOutputCurrentAddress().contains(currentAddress),
                "Output current address should match submitted value");
        Assert.assertTrue(page.getOutputPermanentAddress().contains(permanentAddress),
                "Output permanent address should match submitted value");
        log.info("Test completed: testFillTextBoxForm");
    }
}
