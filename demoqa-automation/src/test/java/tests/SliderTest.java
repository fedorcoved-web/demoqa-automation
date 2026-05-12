package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SliderPage;

public class SliderTest extends BaseTest {

    @Test(description = "Move the range slider to value 75 and verify the displayed value updates accordingly")
    public void testMoveSlider() {
        SliderPage page = new SliderPage(getDriver());
        page.navigateTo();
        page.moveSliderTo(75);

        Assert.assertEquals(page.getSliderValue(), "75",
                "Slider display value should be 75 after moving it to that position");
    }
}
