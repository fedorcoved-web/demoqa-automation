package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.UploadDownloadPage;

public class UploadDownloadTest extends BaseTest {

    @Test(description = "Upload file and verify filename appears", groups = "smoke")
    public void testUploadFile() {
        UploadDownloadPage page = new UploadDownloadPage(getDriver());
        page.navigateTo();

        String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\test-upload.txt";
        page.uploadFile(filePath);

        Assert.assertTrue(page.getUploadedFileName().contains("test-upload.txt"),
                "Uploaded filename should appear in the UI");
    }
}
