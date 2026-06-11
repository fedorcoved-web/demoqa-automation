package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DragAndDropPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Epic("Interactions")
@Feature("Drag And Drop")
public class DragAndDropTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(DragAndDropTest.class);

    @Story("Drag element onto drop target and verify dropped state")
    @Description("Drag the draggable element onto the drop target and verify the droppable area text changes to Dropped!")
    @Test(description = "Drag the draggable element onto the drop target and verify the droppable area text changes to Dropped!",
            groups = {"regression"})
    public void testDragAndDrop() {
        log.info("Starting test: testDragAndDrop");
        DragAndDropPage page = new DragAndDropPage(getDriver());
        page.navigateTo();
        page.dragAndDrop();

        Assert.assertEquals(page.getDroppableText(), "Dropped!",
                "Droppable area text should change to 'Dropped!' after a successful drag-and-drop");
        log.info("Test completed: testDragAndDrop");
    }
}
