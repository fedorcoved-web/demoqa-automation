package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CheckBoxPage {

    private final WebDriver driver;

    // DemoQA migrated from react-checkbox-tree (rct-* classes) to rc-tree (rc-tree-* classes).
    private static final By TREE_READY =
            By.cssSelector(".rc-tree");

    // The Home checkbox in rc-tree renders as a span with role="checkbox" and aria-label.
    private static final By HOME_CHECKBOX =
            By.cssSelector("span.rc-tree-checkbox[aria-label='Select Home']");

    @FindBy(css = ".text-success")
    private List<WebElement> selectedItems;

    public CheckBoxPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("https://demoqa.com/checkbox");
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.presenceOfElementLocated(TREE_READY));
    }

    public void selectHomeCheckbox() {
        // JS click bypasses DemoQA ad overlays that block regular pointer events
        WebElement checkbox = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(HOME_CHECKBOX));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
    }

    public boolean isResultDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> !d.findElements(By.cssSelector(".text-success")).isEmpty());
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
