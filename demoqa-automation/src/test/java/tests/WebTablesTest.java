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

    @Test(description = "Edit salary of existing row and verify change")
    public void testEditRow() {
        WebTablesPage page = new WebTablesPage(getDriver());
        page.navigateTo();
        page.searchFor("Cierra");
        page.clickEditButtons();        // відкрити форму редагування
        page.editSalary("11000");       // змінити зарплату
        page.submitForm();              // підтвердити
        Assert.assertTrue(page.isNamePresentInTable("11000"),
                "Updated salary should be visible in table");
    }

}
