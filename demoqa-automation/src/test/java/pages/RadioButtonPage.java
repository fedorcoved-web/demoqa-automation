package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RadioButtonPage {

    private final WebDriver driver;

    @FindBy(css = "label[for='yesRadio']")
    private WebElement yesRadio;

    @FindBy(css = "label[for='impressiveRadio']")
    private WebElement impressiveRadio;

    @FindBy(css = "label[for='noRadio']")
    private WebElement noRadio;

    @FindBy(css = ".text-success")
    private WebElement successText;

    public RadioButtonPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("https://demoqa.com/radio-button");
    }

    public void selectYes() {
        yesRadio.click();
    }

    public void selectImpressive() {
        impressiveRadio.click();
    }

    public String getSuccessText() {
        return successText.getText();
    }
}
