package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WebTablesPage {

    private final WebDriver driver;

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

    public WebTablesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("https://demoqa.com/webtables");
    }

    public void clickAddButton() {
        addButton.click();
        // wait for the registration modal to be visible before interacting with its fields
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(firstNameInput));
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

    public void submitForm() {
        // JS click bypasses any ad overlay covering the modal's submit button
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        // wait for the modal to close before asserting table state
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.invisibilityOf(firstNameInput));
    }

    public void searchFor(String term) {
        searchBox.sendKeys(term);
    }

    public boolean isNamePresentInTable(String firstName) {
        return tableRows.stream().anyMatch(row -> row.getText().contains(firstName));
    }

    public boolean waitForNameInTable(String firstName) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> tableRows.stream().anyMatch(row -> row.getText().contains(firstName)));
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
