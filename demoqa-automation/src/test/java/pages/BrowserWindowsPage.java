package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class BrowserWindowsPage {

    private final WebDriver driver;

    @FindBy(id = "tabButton")
    private WebElement newTabButton;

    @FindBy(id = "windowButton")
    private WebElement newWindowButton;

    @FindBy(id = "sampleHeading")
    private WebElement sampleHeading;

    public BrowserWindowsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("https://demoqa.com/browser-windows");
    }

    public void clickNewTab() {
        newTabButton.click();
    }

    public void switchToNewTab() {
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(handles.get(handles.size() - 1));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getSampleHeadingText() {
        return sampleHeading.getText();
    }
}
