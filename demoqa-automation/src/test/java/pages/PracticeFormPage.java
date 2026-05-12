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

public class PracticeFormPage {

    private final WebDriver driver;

    @FindBy(id = "firstName")
    private WebElement firstNameInput;

    @FindBy(id = "lastName")
    private WebElement lastNameInput;

    @FindBy(id = "userEmail")
    private WebElement emailInput;

    @FindBy(css = "label[for='gender-radio-1']")
    private WebElement maleGenderRadio;

    @FindBy(id = "userNumber")
    private WebElement mobileInput;

    @FindBy(css = "label[for='hobbies-checkbox-1']")
    private WebElement sportsHobbyCheckbox;

    @FindBy(id = "currentAddress")
    private WebElement currentAddressInput;

    @FindBy(id = "submit")
    private WebElement submitButton;

    @FindBy(css = ".modal-title")
    private WebElement modalTitle;

    @FindBy(id = "closeLargeModal")
    private WebElement closeModalButton;

    public PracticeFormPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("https://demoqa.com/automation-practice-form");
    }

    public void fillFirstName(String name) {
        firstNameInput.sendKeys(name);
    }

    public void fillLastName(String name) {
        lastNameInput.sendKeys(name);
    }

    public void fillEmail(String email) {
        emailInput.sendKeys(email);
    }

    public void selectMaleGender() {
        maleGenderRadio.click();
    }

    public void fillMobile(String mobile) {
        mobileInput.sendKeys(mobile);
    }

    public void selectSportsHobby() {
        sportsHobbyCheckbox.click();
    }

    public void fillCurrentAddress(String address) {
        currentAddressInput.sendKeys(address);
    }

    public void submit() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", submitButton);
        js.executeScript("arguments[0].click();", submitButton);
    }

    public boolean isModalDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(modalTitle));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getModalTitle() {
        return modalTitle.getText();
    }

    public void closeModal() {
        closeModalButton.click();
    }
}
