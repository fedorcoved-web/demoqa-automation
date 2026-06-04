package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SliderPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("Widgets")
@Feature("Slider")
public class SliderTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(SliderTest.class);

    @Story("Move slider to target value and verify display")
    @Description("Move the range slider to value 75 and verify the displayed value updates accordingly")
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
