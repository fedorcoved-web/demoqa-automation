package pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class WebTablesPage extends BasePage {

    @FindBy(id = "addNewRecordButton")
    private WebElement addButton;

    @FindBy(id = "searchBox")
    private WebElement searchBox;

    // DemoQA migrated from react-table (.rt-tbody .rt-tr-group) to a standard HTML table.
    @FindBy(css = "table tbody tr")
    private List<WebElement> tableRows;

    @FindBy(css = "span[title='Delete']")
    private List<WebElement> deleteButtons;

    public WebTablesPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("/webtables");
        wait.until(ExpectedConditions.elementToBeClickable(addButton));
    }

    public void searchFor(String term) {
        searchBox.sendKeys(term);
    }

    public boolean isNamePresentInTable(String firstName) {
        return tableRows.stream().anyMatch(row -> row.getText().contains(firstName));
    }

    public boolean waitForNameInTable(String firstName) {
        try {
            wait.until(d -> tableRows.stream().anyMatch(row -> row.getText().contains(firstName)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void deleteFirstRow() {
        if (deleteButtons.isEmpty()) {
            throw new IllegalStateException("No delete buttons found — search returned no rows");
        }
        safeClick(deleteButtons.get(0));
    }

    public int getNonEmptyRowCount() {
        return (int) tableRows.stream().filter(r -> !r.getText().trim().isEmpty()).count();
    }
}
