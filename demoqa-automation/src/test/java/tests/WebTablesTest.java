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

}
