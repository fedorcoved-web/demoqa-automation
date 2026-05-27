package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SliderPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SliderTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(SliderTest.class);

    @Test(description = "Move the range slider to value 75 and verify the displayed value updates accordingly")
    public void testMoveSlider() {
        log.info("Starting test: testMoveSlider");
        SliderPage page = new SliderPage(getDriver());
        page.navigateTo();
        page.moveSliderTo(75);

        Assert.assertEquals(page.getSliderValue(), "75",
                "Slider display value should be 75 after moving it to that position");
        log.info("Test completed: testMoveSlider");
    }
}
