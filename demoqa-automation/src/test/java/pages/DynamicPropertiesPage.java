package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DynamicPropertiesPage extends BasePage {

    @FindBy(id = "enableAfter")
    private WebElement enableAfterButton;

    @FindBy(id = "visibleAfter")
    private WebElement visibleAfterButton;

    public DynamicPropertiesPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("/dynamic-properties");
        wait.until(ExpectedConditions.visibilityOf(enableAfterButton));
    }

    public void clickVisibleAfterButton() {
        wait.until(ExpectedConditions.visibilityOf(visibleAfterButton));
        safeClick(visibleAfterButton);
    }

    public boolean isButtonVisible() {
        return visibleAfterButton.isDisplayed();
    }
}
