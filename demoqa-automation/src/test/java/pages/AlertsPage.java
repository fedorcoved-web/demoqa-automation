package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AlertsPage extends BasePage {

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
        super(driver);
    }

    public void navigateTo() {
        navigateTo("https://demoqa.com/alerts");
        wait.until(ExpectedConditions.elementToBeClickable(simpleAlertButton));
    }

    public void clickSimpleAlert() {
        safeClick(simpleAlertButton);
    }

    public void clickConfirmAlert() {
        safeClick(confirmAlertButton);
    }

    public void clickPromptAlert() {
        safeClick(promptAlertButton);
    }

    public String getAlertText() {
        return waitForAlert().getText();
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
        wait.until(ExpectedConditions.visibilityOf(confirmResult));
        return confirmResult.getText();
    }

    public String getPromptResult() {
        wait.until(ExpectedConditions.visibilityOf(promptResult));
        return promptResult.getText();
    }

    private Alert waitForAlert() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }
}
