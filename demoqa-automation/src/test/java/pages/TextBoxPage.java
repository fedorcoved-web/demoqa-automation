package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TextBoxPage {

    private final WebDriver driver;

    @FindBy(id = "userName")
    private WebElement fullNameInput;

    @FindBy(id = "userEmail")
    private WebElement emailInput;

    @FindBy(id = "currentAddress")
    private WebElement currentAddressInput;

    @FindBy(id = "permanentAddress")
    private WebElement permanentAddressInput;

    @FindBy(id = "submit")
    private WebElement submitButton;

    @FindBy(id = "name")
    private WebElement outputName;

    @FindBy(id = "email")
    private WebElement outputEmail;

    @FindBy(css = "#output #currentAddress")
    private WebElement outputCurrentAddress;

    @FindBy(css = "#output #permanentAddress")
    private WebElement outputPermanentAddress;

    public TextBoxPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("https://demoqa.com/text-box");
    }

    public void fillForm(String name, String email, String currentAddress, String permanentAddress) {
        fullNameInput.sendKeys(name);
        emailInput.sendKeys(email);
        currentAddressInput.sendKeys(currentAddress);
        permanentAddressInput.sendKeys(permanentAddress);
    }

    public void submit() {
        submitButton.click();
    }

    public String getOutputName() {
        return outputName.getText();
    }

    public String getOutputEmail() {
        return outputEmail.getText();
    }

    public String getOutputCurrentAddress() {
        return outputCurrentAddress.getText();
    }

    public String getOutputPermanentAddress() {
        return outputPermanentAddress.getText();
    }
}
