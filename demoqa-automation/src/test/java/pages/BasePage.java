package pages;

import com.epam.healenium.SelfHealingDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final JavascriptExecutor js;

    protected BasePage(WebDriver driver) {
        // TODO(diagnostics): temporary escape hatch to isolate Healenium/Docker
        // infra noise from real locator failures. Run `mvn test -Dhealenium.enabled=false`
        // to use a plain, unwrapped driver. Remove once the CI docker-compose
        // healenium-backend startup is fixed, or keep as a permanent debug toggle.
        boolean healingEnabled = Boolean.parseBoolean(System.getProperty("healenium.enabled", "true"));
        this.driver = healingEnabled ? SelfHealingDriver.create(driver) : driver;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(15));
        this.js = (JavascriptExecutor) this.driver;
        PageFactory.initElements(this.driver, this);
    }

    protected void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    // Navigates to url and immediately strips ad nodes so they cannot intercept clicks.
    protected void navigateTo(String url) {
        driver.get(url);
        dismissAds();
    }

    // Removes iframes, Google ad nodes, adsby containers, and high-z-index overlays
    // that cause ElementClickInterceptedException on demoqa.com.
    protected void dismissAds() {
        js.executeScript(
            "var ads = document.querySelectorAll(" +
            "  'iframe, [id*=\"google\"], [class*=\"adsby\"], [id*=\"ad-\"], div[style*=\"z-index: 9\"]'" +
            ");" +
            "ads.forEach(function(ad) { ad.parentNode && ad.parentNode.removeChild(ad); });"
        );
    }

    protected void safeClick(WebElement element) {
        dismissAds();
        scrollIntoView(element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        try {
            element.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", element);
        }
    }

    protected void jsClick(WebElement element) {
        scrollIntoView(element);
        js.executeScript("arguments[0].click();", element);
    }
}
