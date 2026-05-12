package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class SelectMenuPage {

    private final WebDriver driver;

    @FindBy(id = "oldSelectMenu")
    private WebElement oldSelectMenu;

    public SelectMenuPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("https://demoqa.com/select-menu");
    }

    public void selectFromOldMenu(String visibleText) {
        new Select(oldSelectMenu).selectByVisibleText(visibleText);
    }

    public String getSelectedOption() {
        return new Select(oldSelectMenu).getFirstSelectedOption().getText();
    }
}
