package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class DragAndDropPage extends BasePage {

    @FindBy(id = "draggable")
    private WebElement draggable;

    @FindBy(id = "droppable")
    private WebElement droppable;

    public DragAndDropPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("https://demoqa.com/droppable");
        wait.until(ExpectedConditions.elementToBeClickable(draggable));
    }

    public void dragAndDrop() {
        dismissAds();
        scrollIntoView(draggable);
        wait.until(ExpectedConditions.elementToBeClickable(draggable));
        // Actions.dragAndDrop() is unreliable in Chrome 100+.
        // Explicit clickAndHold → moveToElement → release is more stable.
        new Actions(driver)
                .clickAndHold(draggable)
                .pause(Duration.ofMillis(400))
                .moveToElement(droppable)
                .pause(Duration.ofMillis(400))
                .release()
                .perform();
    }

    public String getDroppableText() {
        return droppable.getText();
    }
}
