package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.time.Duration;

public class BaseTest {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driverThread.set(driver);
    }

    @AfterClass
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
        }
    }
}
