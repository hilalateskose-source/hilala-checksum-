package ntnu.it1901.hilala.checksum.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
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
 * Tests for the primary view.
 */
class PrimaryControllerTest  extends UiTestBase {
    
  /**
   * Test suite for the overview.
   *
   * @throws InterruptedException thread related exception
   */
  @SuppressWarnings("unchecked") 
  @Test
  void check_overview() throws InterruptedException {
    WaitForAsyncUtils.waitForFxEvents();
    FxAssert.verifyThat("#primaryButton", LabeledMatchers.hasText("Details ..."));
    
    //load some files
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

    // select the first
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
  
    WaitForAsyncUtils.waitForFxEvents();
    clickOn("#copyButton");
    WaitForAsyncUtils.waitForFxEvents();

    //check clipboard
    CountDownLatch latchClipboard = new CountDownLatch(1);
    Platform.runLater(() -> {
      try {
        // Perform actions on the JavaFX Application Thread
        String cb = Clipboard.getSystemClipboard().getString();

        assertNotNull(cb, "ClipBoard contents is null");
        if (cb != null) {
          assertTrue(cb.contains("MD5"));
          assertTrue(cb.contains("SHA256"));
          assertTrue(cb.contains(Context.getInstance().getSelectedEntry().getSha256Hash()));
          assertTrue(cb.contains(Context.getInstance().getSelectedEntry().getMd5Hash()));  
        }  
      } catch (Exception e) {
        // ignore
      } finally {
        latchClipboard.countDown();  // Signal that the task is complete    
      }
    });
    

    // Wait for the latch to be counted down
    latchClipboard.await(); 
    
    clickOn("#refreshButton", MouseButton.PRIMARY);
    WaitForAsyncUtils.waitForFxEvents();

    clickOn("#primaryButton", MouseButton.PRIMARY);
    WaitForAsyncUtils.waitForFxEvents();
    
    FxAssert.verifyThat("#secondaryButton", LabeledMatchers.hasText("Back to overview"));
    clickOn("#secondaryButton");
  }
}
