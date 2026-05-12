package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

public class DragAndDropPage {

    private final WebDriver driver;

    @FindBy(id = "draggable")
    private WebElement draggable;

    @FindBy(id = "droppable")
    private WebElement droppable;

    public DragAndDropPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("https://demoqa.com/droppable");
    }

    public void dragAndDrop() {
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
