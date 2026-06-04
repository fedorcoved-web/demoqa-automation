package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.PracticeFormPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("Forms")
@Feature("Practice Form")
public class PracticeFormTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(PracticeFormTest.class);

    @Story("Submit student registration form and confirm modal")
    @Description("Fill the complete student registration form and verify the submission confirmation modal appears")
    @Test(description = "Fill the complete student registration form and verify the submission confirmation modal appears")
    public void testFillStudentRegistrationForm() {
        log.info("Starting test: testFillStudentRegistrationForm");
        PracticeFormPage page = new PracticeFormPage(getDriver());
        page.navigateTo();
        page.fillFirstName("John");
        page.fillLastName("Doe");
        page.fillEmail("john.doe@example.com");
        page.selectMaleGender();
        page.fillMobile("1234567890");
        page.selectSportsHobby();
        page.fillCurrentAddress("123 Main Street, Springfield");
        page.selectState("NCR");
        page.selectCity("Delhi");
        page.submit();

        Assert.assertTrue(page.isModalDisplayed(),
                "Submission confirmation modal should be displayed after form submit");
        Assert.assertEquals(page.getModalTitle(), "Thanks for submitting the form",
                "Modal title should confirm successful submission");
        log.info("Test completed: testFillStudentRegistrationForm");
    }
}
