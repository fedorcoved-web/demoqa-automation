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

    @FindBy(id = "firstName")
    private WebElement firstNameInput;

    @FindBy(id = "lastName")
    private WebElement lastNameInput;

    @FindBy(id = "userEmail")
    private WebElement emailInput;

    @FindBy(id = "age")
    private WebElement ageInput;

    @FindBy(id = "salary")
    private WebElement salaryInput;

    @FindBy(id = "department")
    private WebElement departmentInput;

    @FindBy(id = "submit")
    private WebElement submitButton;

    @FindBy(id = "searchBox")
    private WebElement searchBox;

    // DemoQA migrated from react-table (.rt-tbody .rt-tr-group) to a standard HTML table.
    @FindBy(css = "table tbody tr")
    private List<WebElement> tableRows;

    @FindBy(css = "span[title='Delete']")
    private List<WebElement> deleteButtons;

    @FindBy(css = "span[id='edit-record-1'] svg path")
    private List<WebElement> editButtons;

    public WebTablesPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("https://demoqa.com/webtables");
        wait.until(ExpectedConditions.elementToBeClickable(addButton));
    }

    public void clickAddButton() {
        safeClick(addButton);
        wait.until(ExpectedConditions.visibilityOf(firstNameInput));
    }

    public void clickEditButtons() {
        editButtons.get(0).click();
    }

    public void fillRegistrationForm(String firstName, String lastName, String email,
                                     String age, String salary, String department) {
        firstNameInput.sendKeys(firstName);
        lastNameInput.sendKeys(lastName);
        emailInput.sendKeys(email);
        ageInput.sendKeys(age);
        salaryInput.sendKeys(salary);
        departmentInput.sendKeys(department);
    }

    public void editSalary(String salary) {
        salaryInput.sendKeys(salary);
    }

    public void submitForm() {
        // JS click bypasses any ad overlay covering the modal's submit button
        jsClick(submitButton);
        wait.until(ExpectedConditions.invisibilityOf(firstNameInput));
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
        if (!deleteButtons.isEmpty()) {
            deleteButtons.get(0).click();
        }
    }

    public int getNonEmptyRowCount() {
        return (int) tableRows.stream().filter(r -> !r.getText().trim().isEmpty()).count();
    }
}
