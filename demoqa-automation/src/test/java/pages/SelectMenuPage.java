package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class SelectMenuPage extends BasePage {

    @FindBy(id = "oldSelectMenu")
    private WebElement oldSelectMenu;

    public SelectMenuPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("https://demoqa.com/select-menu");
        wait.until(ExpectedConditions.elementToBeClickable(oldSelectMenu));
    }

    public void selectFromOldMenu(String visibleText) {
        dismissAds();
        scrollIntoView(oldSelectMenu);
        wait.until(ExpectedConditions.elementToBeClickable(oldSelectMenu));
        new Select(oldSelectMenu).selectByVisibleText(visibleText);
    }

    public String getSelectedOption() {
        return new Select(oldSelectMenu).getFirstSelectedOption().getText();
    }
}
