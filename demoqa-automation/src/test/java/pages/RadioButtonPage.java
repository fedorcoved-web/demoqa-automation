package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RadioButtonPage extends BasePage {

    @FindBy(css = "label[for='yesRadio']")
    private WebElement yesRadio;

    @FindBy(css = "label[for='impressiveRadio']")
    private WebElement impressiveRadio;

    @FindBy(css = ".text-success")
    private WebElement successText;

    public RadioButtonPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("https://demoqa.com/radio-button");
        wait.until(ExpectedConditions.elementToBeClickable(yesRadio));
    }

    public void selectYes() {
        safeClick(yesRadio);
    }

    public void selectImpressive() {
        safeClick(impressiveRadio);
    }

    public String getSuccessText() {
        wait.until(ExpectedConditions.visibilityOf(successText));
        return successText.getText();
    }
}
