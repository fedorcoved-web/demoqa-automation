package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class UploadDownloadPage extends BasePage {

    @FindBy(id = "uploadFile")
    private WebElement uploadFileInput;

    @FindBy(id = "uploadedFilePath")
    private WebElement uploadedFilePath;

    public UploadDownloadPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        navigateTo("https://demoqa.com/upload-download");
        wait.until(ExpectedConditions.visibilityOf(uploadFileInput));
    }

    public void uploadFile(String filePath) {
        uploadFileInput.sendKeys(filePath); // sendKeys, not click — avoids native file dialog
    }

    public String getUploadedFileName() {
        wait.until(ExpectedConditions.visibilityOf(uploadedFilePath));
        return uploadedFilePath.getText();
    }
}
