package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class BrokenImagesPage extends BasePage {

    @FindBy(css = "img")
    private List<WebElement> images;

    public BrokenImagesPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("https://demoqa.com/broken");
    }

    public boolean hasValidImage() {
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
