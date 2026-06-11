package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        // Defensive: quit any driver left over if tearDown was skipped (e.g. by a retry edge-case)
        WebDriver existing = driverThread.get();
        if (existing != null) {
            try { existing.quit(); } catch (Exception ignored) {}
            driverThread.remove();
        }
        log.info("setUp: starting");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driverThread.set(driver);
        log.info("setUp: complete");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("tearDown: starting");
        WebDriver driver = getDriver();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
        }
        log.info("tearDown: complete");
    }
}
