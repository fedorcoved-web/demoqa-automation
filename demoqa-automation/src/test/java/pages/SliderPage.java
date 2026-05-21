package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SliderPage extends BasePage {

    @FindBy(css = "input[type='range']")
    private WebElement slider;

    @FindBy(id = "sliderValue")
    private WebElement sliderValue;

    public SliderPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("https://demoqa.com/slider");
        wait.until(ExpectedConditions.visibilityOf(slider));
    }

    public void moveSliderTo(int targetValue) {
        dismissAds();
        scrollIntoView(slider);
        // Standard dispatchEvent is ignored by React's synthetic event system.
        // nativeInputValueSetter bypasses React's controlled-input guard so the
        // component's state actually updates and the #sliderValue display reflects it.
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
