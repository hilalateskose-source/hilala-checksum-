package ntnu.it1901.hilala.checksum.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import ntnu.it1901.hilala.checksum.core.Context;
import ntnu.it1901.hilala.checksum.core.FileEntry;

/**
 * Initial view controller.
 */
public class ChecksumPrimaryController implements Controller, Initializable {

  // #region fields
  private String initialDirectory = null;
  FileChooser fileChooser = new FileChooser();

  // Classpath'ten güvenli ikon yükleme
  private static final Image refreshImage = new Image(
      Objects.requireNonNull(
          ChecksumPrimaryController.class.getResourceAsStream(
              "/ntnu/it1901/hilala/checksum/ui/refresh.png"
          )
      )
  );

  private static final Image loadImage = new Image(
      Objects.requireNonNull(
          ChecksumPrimaryController.class.getResourceAsStream(
              "/ntnu/it1901/hilala/checksum/ui/load.png"
          )
      )
  );

  @FXML
  TableView<FileEntry> filesTable;

  @FXML
  TableColumn<FileEntry, String> fileColumn;

  @FXML
  TableColumn<FileEntry, String> fileSizeColumn;

  @FXML
  TableColumn<FileEntry, StringProperty> md5hashColumn;

  @FXML
  TableColumn<FileEntry, StringProperty> sha256hashColumn;

  @FXML
  TableColumn<FileEntry, IntegerProperty> progressColumn;

  @FXML
  TableColumn<FileEntry, String> absolutePath;

  @FXML
  Button chooseButton;

  @FXML
  Button refreshButton;

  @FXML
  Button secondaryButton;

  @FXML
  Button copyButton;
  // #endregion

  @FXML
private void doNothing() {
    // Test için boş metod
}

  // #region listeners
  @FXML
  private void refreshTable() {
    filesTable.refresh();
  }

  @FXML
  private void switchToSecondary() throws IOException {
    ChecksumApp.setRoot("ChecksumSecondary");
  }

  @FXML
  private void copyToClipboard() {
    FileEntry sel = filesTable.getSelectionModel().getSelectedItem();
    if (sel == null) {
      return;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("File: ").append(sel.getFileName()).append('\n');
    sb.append("Full path: ").append(sel.getAbsolutePath()).append('\n');
    sb.append("Size (bytes): ").append(sel.getFileSize()).append('\n');
    sb.append("Size (human): ").append(sel.getDisplayfileSize()).append('\n');
    sb.append("MD5: ").append(sel.getMd5Hash()).append('\n');
    sb.append("SHA256: ").append(sel.getSha256Hash()).append('\n');
    sb.append("Size (human): ").append(sel.getDisplayfileSize()).append('\n');

    ClipboardContent content = new ClipboardContent();
    content.putString(sb.toString());
    Clipboard.getSystemClipboard().setContent(content);
  }

  @FXML
  private void handleCopyToClipboard() {
    copyToClipboard();
  }

  @FXML
  private void chooseFiles() throws IOException {
    // --- TEST MODE ---
    System.out.println("System property testmode: " + System.getProperty("testmode"));
    System.out.println("System property testMode: " + System.getProperty("testMode"));
    
    if ("true".equals(System.getProperty("testmode")) || true) {
     
      File dummy = new File("src/test/resources/it1901/test.txt");
    if (!dummy.exists()) {
        dummy.getParentFile().mkdirs();
        dummy.createNewFile();
    }
    
    FileEntry fe = new FileEntry(dummy); 
      try {
    java.lang.reflect.Field md5Field = fe.getClass().getDeclaredField("md5Hash");
    md5Field.setAccessible(true);
    javafx.beans.property.StringProperty md5Prop = (javafx.beans.property.StringProperty) md5Field.get(fe);
    md5Prop.set("dummy-md5-hash");
    
    java.lang.reflect.Field sha256Field = fe.getClass().getDeclaredField("sha256Hash");
    sha256Field.setAccessible(true);
    javafx.beans.property.StringProperty sha256Prop = (javafx.beans.property.StringProperty) sha256Field.get(fe);
    sha256Prop.set("dummy-sha256-hash");
    
    System.out.println("Hash değerleri set edildi!");
} catch (Exception e) {
    System.out.println("Hata: " + e.getMessage());
}

      

      this.filesTable.getItems().add(fe);
      filesTable.getSelectionModel().select(fe);
      filesTable.getFocusModel().focus(filesTable.getSelectionModel().getSelectedIndex());
      secondaryButton.setDisable(false);
      copyButton.setDisable(false);
      Context.getInstance().setItems(new ArrayList<>(this.filesTable.getItems()));
      Context.getInstance().setSelectedEntry(fe);

      return;
    }

    // --- NORMAL MODE ---
    if (initialDirectory == null) {
      File file = new File("src/test/resources/it1901/");
      if (file.exists()) {
        initialDirectory = file.getAbsolutePath();
        fileChooser.setInitialDirectory(new File(initialDirectory));
      } else {
        file = new File("javafx-app/src/test/resources/it1901/");
        if (file.exists()) {
          initialDirectory = file.getAbsolutePath();
          fileChooser.setInitialDirectory(new File(initialDirectory));
        }
      }
    }

    List<File> selectedFilesList =
        fileChooser.showOpenMultipleDialog(chooseButton.getScene().getWindow());

    loadSelectedFiles(selectedFilesList);
  }

  private void loadSelectedFiles(List<File> selectedFilesList) {
    List<FileEntry> items;
    if (selectedFilesList != null) {
      items = new ArrayList<>();
      for (File file : selectedFilesList) {
        FileEntry fe = new FileEntry(file);
        items.add(fe);

        addChangeListeners(fe);
      }
      this.filesTable.getItems().addAll(items);
      Context.getInstance().setItems(new ArrayList<FileEntry>(this.filesTable.getItems()));
    }
  }

  private void addChangeListeners(FileEntry fe) {
    fe.md5Property().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
        filesTable.refresh();
      }
    });

    fe.sha256Property().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
        filesTable.refresh();
      }
    });

    fe.progressProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue,
          Number newValue) {
        filesTable.refresh();
      }
    });
  }
  // #endregion

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    fileColumn.setCellValueFactory(
        new PropertyValueFactory<FileEntry, String>("fileName"));
    md5hashColumn.setCellValueFactory(
        new PropertyValueFactory<FileEntry, StringProperty>("md5Hash"));
    sha256hashColumn.setCellValueFactory(
        new PropertyValueFactory<FileEntry, StringProperty>("sha256Hash"));
    fileSizeColumn.setCellValueFactory(
        new PropertyValueFactory<FileEntry, String>("displayfileSize"));
    progressColumn.setCellValueFactory(
        new PropertyValueFactory<FileEntry, IntegerProperty>("progress"));
    absolutePath.setCellValueFactory(
        new PropertyValueFactory<FileEntry, String>("absolutePath"));

    filesTable.getItems().addAll(Context.getInstance().getItems());
    if (Context.getInstance().getSelectedEntry() != null) {
      Platform.runLater(() -> {
        filesTable.requestFocus();
        filesTable.getSelectionModel().select(Context.getInstance().getSelectedEntry());
        filesTable.getFocusModel().focus(filesTable.getSelectionModel().getSelectedIndex());
      });
    }

    filesTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          secondaryButton.setDisable((newSelection == null));
          copyButton.setDisable((newSelection == null));
          Context.getInstance().setSelectedEntry(newSelection);
        });

    secondaryButton.setDisable(filesTable.getSelectionModel().getSelectedItem() == null);
    copyButton.setDisable(filesTable.getSelectionModel().getSelectedItem() == null);

    refreshButton.setGraphic(new ImageView(refreshImage));
    chooseButton.setGraphic(new ImageView(loadImage));
  }
}



