package base;

import com.epam.healenium.SelfHealingDriver;
import org.openqa.selenium.WebDriver;
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
        WebDriver driver = BrowserFactory.createDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // Diagnostic escape hatch to isolate Healenium/Docker infra noise from real
        // locator failures: run `mvn test -Dhealenium.enabled=false` for a plain driver.
        // Wrapped once per test here (not per Page object) so every page shares one proxy.
        boolean healingEnabled = Boolean.parseBoolean(System.getProperty("healenium.enabled", "true"));
        if (healingEnabled) {
            driver = SelfHealingDriver.create(driver);
        }

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
