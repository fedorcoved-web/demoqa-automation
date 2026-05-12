package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DatePickerPage {

    private final WebDriver driver;

    @FindBy(id = "datePickerMonthYearInput")
    private WebElement datePickerInput;

    public DatePickerPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("https://demoqa.com/date-picker");
    }

    public void selectDate(String date) {
        datePickerInput.click();
        datePickerInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), date);
        datePickerInput.sendKeys(Keys.ENTER);
    }

    public String getSelectedDate() {
        return datePickerInput.getAttribute("value");
    }
}
