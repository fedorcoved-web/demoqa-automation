package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BrokenImagesPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("Book Store Application")
@Feature("Broken Images")
public class BrokenImagesTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(BrokenImagesTest.class);

    @Story("Verify at least one valid image loads on the page")
    @Description("Navigate to the broken images page and verify at least one valid image loads with a non-zero natural width")
    @Test(description = "Navigate to the broken images page and verify at least one valid image loads with a non-zero natural width")
    public void testValidImageLoads() {
        log.info("Starting test: testValidImageLoads");
        BrokenImagesPage page = new BrokenImagesPage(getDriver());
        page.navigateTo();

        Assert.assertTrue(page.getTotalImageCount() > 0,
                "Page should contain at least one img element");
        Assert.assertTrue(page.hasValidImage(),
                "At least one image should have naturalWidth > 0, indicating it loaded successfully");
        log.info("Test completed: testValidImageLoads");
    }
}
