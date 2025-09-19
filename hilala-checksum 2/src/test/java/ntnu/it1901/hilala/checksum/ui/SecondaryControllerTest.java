package ntnu.it1901.hilala.checksum.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import ntnu.it1901.hilala.checksum.core.Context;
import ntnu.it1901.hilala.checksum.core.FileEntry;

import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.service.query.NodeQuery;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Tests for the secondary view.
 */
class SecondaryControllerTest extends UiTestBase {

  /**
   * Test suite for the details view.
   *
   * @throws InterruptedException threads related exception
   */
  @SuppressWarnings("unchecked") 
  @Test
  void check_details() throws InterruptedException {
    //load some files
    WaitForAsyncUtils.waitForFxEvents();
    FxAssert.verifyThat("#primaryButton", LabeledMatchers.hasText("Details ..."));
    clickOn("#chooseButton");
    moveBy(150, 150).clickOn(MouseButton.PRIMARY);
    press(KeyCode.CONTROL, KeyCode.A);
    release(new KeyCode[] {});

    WaitForAsyncUtils.waitForFxEvents();
    press(KeyCode.ENTER);
    WaitForAsyncUtils.waitForFxEvents();
    release(new KeyCode[] {});
    release(new MouseButton[] {});

    WaitForAsyncUtils.waitForFxEvents();
    NodeQuery nq = lookup("#filesTable");
    assertFalse(nq.queryAll().isEmpty());
    Node n = nq.queryAll().iterator().next();
    TableView<FileEntry> filesTable = (TableView<FileEntry>) n;

    // Select first file in the table
    CountDownLatch latchTable = new CountDownLatch(1);
    Platform.runLater(() -> {
      // Perform actions on the JavaFX Application Thread
      if (filesTable != null) {
        filesTable.getSelectionModel().select(0);
        filesTable.refresh();
      }
      latchTable.countDown();  // Signal that the task is complete
    });

    // Wait for the latch to be counted down
    latchTable.await(); // This will hold the current thread until latch count is zero.

    //go to the details view
    clickOn("#primaryButton", MouseButton.PRIMARY);
    WaitForAsyncUtils.waitForFxEvents();
    
     
    FxAssert.verifyThat("#secondaryButton", LabeledMatchers.hasText("Back to overview"));

    FileEntry fileEntry = Context.getInstance().getSelectedEntry();

    final Label lblFilename = (Label) lookup("#lblFilename").query();
    assertEquals(fileEntry.getFileName(), lblFilename.getText());

    final Label lblAbsolutePath = (Label) lookup("#lblAbsolutePath").query();
    assertEquals(fileEntry.getAbsolutePath(), lblAbsolutePath.getText());

    final Label lblMd5 = (Label) lookup("#lblMd5").query();
    assertEquals(fileEntry.getMd5Hash(), lblMd5.getText());

    final Label lblSha256 = (Label) lookup("#lblSha256").query();
    assertEquals(fileEntry.getSha256Hash(), lblSha256.getText());
    
    final TextField txtExpectedMd5 = (TextField) lookup("#txtExpectedMd5").query();
    txtExpectedMd5.setText(fileEntry.getMd5Hash());
    
    final TextField txtExpectedSha256 = (TextField) lookup("#txtExpectedSha256").query();
    txtExpectedSha256.setText(fileEntry.getSha256Hash());
    
    WaitForAsyncUtils.waitForFxEvents();

    final ImageView imgMd5Status = (ImageView) lookup("#imgMd5Status").query();
    final ImageView imgSha256Status = (ImageView) lookup("#imgSha256Status").query();

    try {
      Field field = ChecksumSecondaryController.class.getDeclaredField("okImage");
      field.setAccessible(true);
      Image imgOk = (Image) field.get(null);
      assertEquals(imgOk, imgMd5Status.getImage());
      assertEquals(imgOk, imgSha256Status.getImage());
  
    } catch (Exception e) {
      fail(e);
    }
    
    txtExpectedMd5.setText("blah");
    txtExpectedSha256.setText("blah");
    
    WaitForAsyncUtils.waitForFxEvents();

    try {
      Field field = ChecksumSecondaryController.class.getDeclaredField("failImage");
      field.setAccessible(true);
      Image imgFail = (Image) field.get(null);
      assertEquals(imgFail, imgMd5Status.getImage());
      assertEquals(imgFail, imgSha256Status.getImage());
  
    } catch (Exception e) {
      fail(e);
    }

    clickOn("#secondaryButton");
  }
}
