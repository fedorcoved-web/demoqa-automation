package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DragAndDropPage;

public class DragAndDropTest extends BaseTest {

    @Test(description = "Drag the draggable element onto the drop target and verify the droppable area text changes to Dropped!")
    public void testDragAndDrop() {
        DragAndDropPage page = new DragAndDropPage(getDriver());
        page.navigateTo();
        page.dragAndDrop();

        Assert.assertEquals(page.getDroppableText(), "Dropped!",
                "Droppable area text should change to 'Dropped!' after a successful drag-and-drop");
    }
}
