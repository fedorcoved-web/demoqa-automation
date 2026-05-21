package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class BrowserWindowsPage extends BasePage {

    @FindBy(id = "tabButton")
    private WebElement newTabButton;

    @FindBy(id = "windowButton")
    private WebElement newWindowButton;

    @FindBy(id = "sampleHeading")
    private WebElement sampleHeading;

    public BrowserWindowsPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("https://demoqa.com/browser-windows");
        wait.until(ExpectedConditions.elementToBeClickable(newTabButton));
    }

    public void clickNewTab() {
        safeClick(newTabButton);
    }

    public void switchToNewTab() {
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        String original = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(original)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    public String getCurrentUrl() {
        // Block until the new tab's document is at least interactive so
        // ChromeDriver's HTTP response doesn't time out mid-page-load.
        wait.until(d -> {
            String state = (String) js.executeScript("return document.readyState");
            return "interactive".equals(state) || "complete".equals(state);
        });
        return driver.getCurrentUrl();
    }

    public String getSampleHeadingText() {
        wait.until(ExpectedConditions.visibilityOf(sampleHeading));
        return sampleHeading.getText();
    }
}
