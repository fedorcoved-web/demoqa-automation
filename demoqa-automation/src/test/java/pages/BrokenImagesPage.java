package pages;

import org.openqa.selenium.TimeoutException;
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
        navigateTo("/broken");
    }

    // EAGER page load strategy returns control before images finish downloading,
    // so poll instead of checking naturalWidth once immediately after navigation.
    public boolean hasValidImage() {
        try {
            wait.until(d -> images.stream().anyMatch(this::isLoaded));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean isLoaded(WebElement img) {
        Long naturalWidth = (Long) js.executeScript("return arguments[0].naturalWidth;", img);
        return naturalWidth != null && naturalWidth > 0;
    }

    public int getTotalImageCount() {
        return images.size();
    }
}
