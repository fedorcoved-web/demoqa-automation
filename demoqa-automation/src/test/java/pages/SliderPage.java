package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SliderPage {

    private final WebDriver driver;

    @FindBy(css = "input[type='range']")
    private WebElement slider;

    @FindBy(id = "sliderValue")
    private WebElement sliderValue;

    public SliderPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("https://demoqa.com/slider");
    }

    public void moveSliderTo(int targetValue) {
        // Standard dispatchEvent is ignored by React's synthetic event system.
        // nativeInputValueSetter bypasses React's controlled-input guard so the
        // component's state actually updates and the #sliderValue display reflects it.
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
            "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "setter.call(arguments[0], arguments[1]);" +
            "arguments[0].dispatchEvent(new Event('input', {bubbles: true}));",
            slider, String.valueOf(targetValue));
    }

    public String getSliderValue() {
        return sliderValue.getAttribute("value");
    }
}
