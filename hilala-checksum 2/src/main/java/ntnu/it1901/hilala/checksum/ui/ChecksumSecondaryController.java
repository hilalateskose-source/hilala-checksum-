package ntnu.it1901.hilala.checksum.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ntnu.it1901.hilala.checksum.core.Context;
import ntnu.it1901.hilala.checksum.core.FileEntry;
import ntnu.it1901.hilala.checksum.ui.ChecksumApp;

public class ChecksumSecondaryController implements Controller, Initializable {

    private FileEntry fileEntry;
    // Statik referanslarla ikonları tek bir yerden yükleyip testlerin beklentisini karşılıyoruz.
    private static final Image okImage = new Image(
        Objects.requireNonNull(
            ChecksumSecondaryController.class.getResourceAsStream(
                "/ntnu/it1901/hilala/checksum/ui/ok.png"
            )
        )
    );
    private static final Image failImage = new Image(
        Objects.requireNonNull(
            ChecksumSecondaryController.class.getResourceAsStream(
                "/ntnu/it1901/hilala/checksum/ui/fail.png"
            )
        )
    );
    
    @FXML
    private Button secondaryButton;
    
    @FXML
    private Label lblTitleFileName;
    
    @FXML
    private Label lblFilename;
    
    @FXML
    private Label lblAbsolutePath;

    
    @FXML
    private Label lblMd5;
    
    @FXML
    private Label lblSizeBytes;
    
    @FXML
    private Label lblSizeHuman;
    
    @FXML
    private TextField txtExpectedMd5;
    
    @FXML
    private ImageView imgMd5Status;
    
    @FXML
    private Label lblSha256;
    
    @FXML
    private TextField txtExpectedSha256;
    
    @FXML
    private ImageView imgSha256Status;

    @FXML private Button primaryButton;
    
    @FXML
    private void switchToPrimary() throws IOException {
        ChecksumApp.setRoot("ChecksumPrimary");
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      System.out.println("=== SECONDARY CONTROLLER INITIALIZE CALLED ===");
      
        fileEntry = Context.getInstance().getSelectedEntry();

        if (fileEntry != null) {
            lblTitleFileName.setText(fileEntry.getFileName());
            lblFilename.setText(fileEntry.getFileName());
            lblAbsolutePath.setText(fileEntry.getAbsolutePath());
            lblMd5.setText(fileEntry.getMd5Hash());
            lblSha256.setText(fileEntry.getSha256Hash());
            lblSizeBytes.setText(fileEntry.getFileSize().toString());
            lblSizeHuman.setText(fileEntry.getDisplayfileSize());
        }


        // Single listener for MD5 - no duplicates!
        txtExpectedMd5.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                imgMd5Status.setImage(null);
            } else if (fileEntry != null && newVal.equalsIgnoreCase(fileEntry.getMd5Hash())) {
                imgMd5Status.setImage(okImage);
            } else {
                imgMd5Status.setImage(failImage);
            }
        });
        
        // Single listener for SHA256 - no duplicates!
        txtExpectedSha256.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                imgSha256Status.setImage(null);
            } else if (fileEntry != null && newVal.equalsIgnoreCase(fileEntry.getSha256Hash())) {
                imgSha256Status.setImage(okImage);
            } else {
                imgSha256Status.setImage(failImage);
            }
        });
    }
}