package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final JavascriptExecutor js;

    // Driver arrives already wrapped (or not) by BaseTest.setUp() based on
    // -Dhealenium.enabled — pages just consume it, they don't decide healing policy.
    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(15));
        this.js = (JavascriptExecutor) this.driver;
        PageFactory.initElements(this.driver, this);
    }

    protected void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    // Navigates to <base URL><path> and immediately strips ad nodes so they cannot intercept clicks.
    // base.url defaults to https://demoqa.com; override with -Dbase.url=... for other environments.
    protected void navigateTo(String path) {
        driver.get(ConfigReader.baseUrl() + path);
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
