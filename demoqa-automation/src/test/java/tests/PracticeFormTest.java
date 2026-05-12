package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.PracticeFormPage;

public class PracticeFormTest extends BaseTest {

    @Test(description = "Fill the complete student registration form and verify the submission confirmation modal appears")
    public void testFillStudentRegistrationForm() {
        PracticeFormPage page = new PracticeFormPage(getDriver());
        page.navigateTo();
        page.fillFirstName("John");
        page.fillLastName("Doe");
        page.fillEmail("john.doe@example.com");
        page.selectMaleGender();
        page.fillMobile("1234567890");
        page.selectSportsHobby();
        page.fillCurrentAddress("123 Main Street, Springfield");
        page.submit();

        Assert.assertTrue(page.isModalDisplayed(),
                "Submission confirmation modal should be displayed after form submit");
        Assert.assertEquals(page.getModalTitle(), "Thanks for submitting the form",
                "Modal title should confirm successful submission");
    }
}
