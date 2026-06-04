package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.UploadDownloadPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("Elements")
@Feature("Upload & Download")
public class UploadDownloadTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(UploadDownloadTest.class);

    @Story("Upload a file and verify its name appears in the UI")
    @Description("Upload file and verify filename appears")
    @Test(description = "Upload file and verify filename appears", groups = "smoke")
    public void testUploadFile() {
        log.info("Starting test: testUploadFile");
        UploadDownloadPage page = new UploadDownloadPage(getDriver());
        page.navigateTo();

        String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\test-upload.txt";
        page.uploadFile(filePath);

        Assert.assertTrue(page.getUploadedFileName().contains("test-upload.txt"),
                "Uploaded filename should appear in the UI");
        log.info("Test completed: testUploadFile");
    }
}
