package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.WebTablesPage;

public class WebTablesTest extends BaseTest {

    @Test(description = "Add a new row via the Add button and verify the new entry appears in the web table")
    public void testAddNewRow() {
        WebTablesPage page = new WebTablesPage(getDriver());
        page.navigateTo();
        page.clickAddButton();
        page.fillRegistrationForm("Alice", "Smith", "alice@test.com", "28", "75000", "Engineering");
        page.submitForm();

        Assert.assertTrue(page.waitForNameInTable("Alice"),
                "Newly added row with name 'Alice' should be visible in the table");
    }

    @Test(description = "Delete an existing row via the Delete icon and verify the entry disappears from the table")
    public void testDeleteRow() {
        WebTablesPage page = new WebTablesPage(getDriver());
        page.navigateTo();
        page.searchFor("Cierra");
        page.deleteFirstRow();

        Assert.assertFalse(page.isNamePresentInTable("Cierra"),
                "Deleted entry 'Cierra' should no longer be visible in the table");
    }
}
