package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PracticeFormPage extends BasePage {

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

    @FindBy(id = "react-select-3-input" )
    private WebElement stateDropdown;

    @FindBy(id = "react-select-4-input" )
    private WebElement cityDropdown;

    public PracticeFormPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("https://demoqa.com/automation-practice-form");
        wait.until(ExpectedConditions.visibilityOf(firstNameInput));
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
        safeClick(maleGenderRadio);
    }

    public void fillMobile(String mobile) {
        mobileInput.sendKeys(mobile);
    }

    public void selectSportsHobby() {
        safeClick(sportsHobbyCheckbox);
    }

    public void fillCurrentAddress(String address) {
        currentAddressInput.sendKeys(address);
    }



    public void submit() {
        jsClick(submitButton);
    }

    public boolean isModalDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(modalTitle));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getModalTitle() {
        return modalTitle.getText();
    }

    public void closeModal() {
        safeClick(closeModalButton);
    }
    public void selectState(String state) {
        stateDropdown.sendKeys(state);
        stateDropdown.sendKeys(Keys.ENTER);
    }

    public void selectCity(String city) {
        cityDropdown.sendKeys(city);
        cityDropdown.sendKeys(Keys.ENTER);
    }

    }


