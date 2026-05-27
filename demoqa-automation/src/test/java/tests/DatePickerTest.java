package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DatePickerPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatePickerTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(DatePickerTest.class);

    @Test(description = "Select a specific date using the date picker widget and verify the input field reflects the chosen date")
    public void testSelectDate() {
        log.info("Starting test: testSelectDate");
        DatePickerPage page = new DatePickerPage(getDriver());
        page.navigateTo();
        page.selectDate("01/15/2025");

        Assert.assertEquals(page.getSelectedDate(), "01/15/2025",
                "Date picker input value should match the selected date");
        log.info("Test completed: testSelectDate");
    }
}
