package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.WebTablesPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("Elements")
@Feature("Web Tables")
public class WebTablesTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(WebTablesTest.class);

    @Story("Add new row and verify it appears in the table")
    @Description("Add a new row via the Add button and verify the new entry appears in the web table")
    @Test(description = "Add a new row via the Add button and verify the new entry appears in the web table",
            groups = {"regression"})
    public void testAddNewRow() {
        log.info("Starting test: testAddNewRow");
        WebTablesPage page = new WebTablesPage(getDriver());
        page.navigateTo();
        page.clickAddButton();
        page.fillRegistrationForm("Alice", "Smith", "alice@test.com", "28", "75000", "Engineering");
        page.submitForm();

        Assert.assertTrue(page.waitForNameInTable("Alice"),
                "Newly added row with name 'Alice' should be visible in the table");
        log.info("Test completed: testAddNewRow");
    }

    @Story("Delete existing row and verify it is removed from the table")
    @Description("Delete an existing row via the Delete icon and verify the entry disappears from the table")
    @Test(description = "Delete an existing row via the Delete icon and verify the entry disappears from the table",
            groups = {"regression"})
    public void testDeleteRow() {
        log.info("Starting test: testDeleteRow");
        WebTablesPage page = new WebTablesPage(getDriver());
        page.navigateTo();
        page.searchFor("Cierra");
        Assert.assertTrue(page.waitForNameInTable("Cierra"),
                "Precondition: 'Cierra' must be present in the table before delete");

        page.deleteFirstRow();

        Assert.assertFalse(page.isNamePresentInTable("Cierra"),
                "Deleted entry 'Cierra' should no longer be visible in the table");
        log.info("Test completed: testDeleteRow");
    }

    @Story("Edit row data and verify updated value appears in table")
    @Description("Edit salary of existing row and verify change")
    @Test(description = "Edit salary of existing row and verify change",
            groups = {"regression"})
    public void testEditRow() {
        log.info("Starting test: testEditRow");
        WebTablesPage page = new WebTablesPage(getDriver());
        page.navigateTo();
        page.searchFor("Cierra");
        page.clickFirstEditButton();    // відкрити форму редагування
        page.editSalary("11000");       // змінити зарплату
        page.submitForm();              // підтвердити

        // Search still filters on "Cierra", so the edited row stays at index 0;
        // exact match on the Salary column (index 4) instead of contains-anywhere.
        Assert.assertTrue(page.waitForExactCellText(0, 4, "11000"),
                "Salary column of the edited row should equal exactly '11000'");
        log.info("Test completed: testEditRow");
    }

}
