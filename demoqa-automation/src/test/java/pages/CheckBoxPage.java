package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CheckBoxPage extends BasePage {

    // DemoQA migrated from react-checkbox-tree (rct-* classes) to rc-tree (rc-tree-* classes).
    private static final By TREE_READY = By.cssSelector(".rc-tree");

    // The Home checkbox in rc-tree renders as a span with role="checkbox" and aria-label.
    private static final By HOME_CHECKBOX = By.cssSelector("span.rc-tree-checkbox[aria-label='Select Home']");

    @FindBy(css = ".text-success")
    private List<WebElement> selectedItems;

    public CheckBoxPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("https://demoqa.com/checkbox");
        wait.until(ExpectedConditions.presenceOfElementLocated(TREE_READY));
    }

    public void selectHomeCheckbox() {
        dismissAds();
        // JS click bypasses DemoQA ad overlays that block regular pointer events
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(HOME_CHECKBOX));
        scrollIntoView(checkbox);
        js.executeScript("arguments[0].click();", checkbox);
    }

    public boolean isResultDisplayed() {
        try {
            wait.until(d -> !d.findElements(By.cssSelector(".text-success")).isEmpty());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getFirstSelectedItem() {
        List<WebElement> items = driver.findElements(By.cssSelector(".text-success"));
        return items.isEmpty() ? "" : items.get(0).getText();
    }
}
