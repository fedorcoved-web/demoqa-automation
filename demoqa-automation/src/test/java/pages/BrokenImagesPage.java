package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class BrokenImagesPage {

    private final WebDriver driver;

    @FindBy(css = "img")
    private List<WebElement> images;

    public BrokenImagesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("https://demoqa.com/broken");
    }

    public boolean hasValidImage() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (WebElement img : images) {
            Long naturalWidth = (Long) js.executeScript("return arguments[0].naturalWidth;", img);
            if (naturalWidth != null && naturalWidth > 0) {
                return true;
            }
        }
        return false;
    }

    public int getTotalImageCount() {
        return images.size();
    }
}
