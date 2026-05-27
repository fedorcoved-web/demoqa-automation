package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.TextBoxPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextBoxTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(TextBoxTest.class);

    @DataProvider(name = "textBoxData")
    public Object[][] textBoxData() {
        return new Object[][] {
            {"John Doe", "john.doe@example.com", "123 Main Street", "456 Oak Avenue"},
            {"Anna Smith", "anna.smith@example.com", "789 Pine Road", "321 Elm Street"},
            {"Ed", "ed@uman.com", "Uman", "Uman"}
        };
    }

    @Test(description = "Fill TextBox form with name, email and addresses, then verify output section displays all values correctly",
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
