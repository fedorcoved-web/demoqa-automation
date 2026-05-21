package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DatePickerPage extends BasePage {

    @FindBy(id = "datePickerMonthYearInput")
    private WebElement datePickerInput;

    public DatePickerPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("https://demoqa.com/date-picker");
        wait.until(ExpectedConditions.elementToBeClickable(datePickerInput));
    }

    public void selectDate(String date) {
        safeClick(datePickerInput);
        datePickerInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), date);
        datePickerInput.sendKeys(Keys.ENTER);
    }

    public String getSelectedDate() {
        return datePickerInput.getAttribute("value");
    }
}
