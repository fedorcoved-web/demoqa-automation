package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AlertsPage {

    private final WebDriver driver;

    @FindBy(id = "alertButton")
    private WebElement simpleAlertButton;

    @FindBy(id = "confirmButton")
    private WebElement confirmAlertButton;

    // Note: intentional typo matches the actual DemoQA element id
    @FindBy(id = "promtButton")
    private WebElement promptAlertButton;

    @FindBy(id = "confirmResult")
    private WebElement confirmResult;

    @FindBy(id = "promptResult")
    private WebElement promptResult;

    public AlertsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("https://demoqa.com/alerts");
    }

    public void clickSimpleAlert() {
        simpleAlertButton.click();
    }

    public void clickConfirmAlert() {
        confirmAlertButton.click();
    }

    public void clickPromptAlert() {
        promptAlertButton.click();
    }

    public String getAlertText() {
        Alert alert = waitForAlert();
        return alert.getText();
    }

    public void acceptAlert() {
        waitForAlert().accept();
    }

    public void dismissAlert() {
        waitForAlert().dismiss();
    }

    public void typeInAlertAndAccept(String text) {
        Alert alert = waitForAlert();
        alert.sendKeys(text);
        alert.accept();
    }

    public String getConfirmResult() {
        return confirmResult.getText();
    }

    public String getPromptResult() {
        return promptResult.getText();
    }

    private Alert waitForAlert() {
        return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.alertIsPresent());
    }
}
