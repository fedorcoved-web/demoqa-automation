package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.TextBoxPage;

public class TextBoxTest extends BaseTest {

    @Test(description = "Fill TextBox form with name, email and addresses, then verify output section displays all values correctly")
    public void testFillTextBoxForm() {
        TextBoxPage page = new TextBoxPage(getDriver());
        page.navigateTo();
        page.fillForm("John Doe", "john.doe@example.com", "123 Main Street", "456 Oak Avenue");
        page.submit();

        Assert.assertTrue(page.getOutputName().contains("John Doe"),
                "Output name should contain the submitted full name");
        Assert.assertTrue(page.getOutputEmail().contains("john.doe@example.com"),
                "Output email should contain the submitted email");
        Assert.assertTrue(page.getOutputCurrentAddress().contains("123 Main Street"),
                "Output current address should match submitted value");
        Assert.assertTrue(page.getOutputPermanentAddress().contains("456 Oak Avenue"),
                "Output permanent address should match submitted value");
    }
}
