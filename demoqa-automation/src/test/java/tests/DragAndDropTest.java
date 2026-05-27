package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DragAndDropPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DragAndDropTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(DragAndDropTest.class);

    @Test(description = "Drag the draggable element onto the drop target and verify the droppable area text changes to Dropped!")
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
