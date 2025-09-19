package ntnu.it1901.hilala.checksum.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.service.query.NodeQuery;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Test suite for application ui.
 */
class AppTest extends UiTestBase {

  @Test
  @DisplayName("the application should load the primary view")
  void should_load_primary_view() {
    WaitForAsyncUtils.waitForFxEvents();

    NodeQuery nq = lookup("#chooseButton");
    assertFalse(nq.queryAll().isEmpty());

    nq = lookup("#refreshButton");
    assertFalse(nq.queryAll().isEmpty());

    nq = lookup("#primaryButton");
    assertFalse(nq.queryAll().isEmpty());

    FxAssert.verifyThat("#primaryButton", LabeledMatchers.hasText("Details ..."));
    
    sleep(1000);
  }

  @Test
  @DisplayName("scene should not be null")
  void scene_should_not_be_null() {
    WaitForAsyncUtils.waitForFxEvents();
    assertNotNull(ChecksumApp.getScene());
  }

  @Test
  @DisplayName("controller should not be null")
  void controller_should_not_be_null() {
    WaitForAsyncUtils.waitForFxEvents();
    assertNotNull(ChecksumApp.getController());
  }
}
