package ntnu.it1901.hilala.checksum.core;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ntnu.it1901.hilala.checksum.core.utils.FileSizeUtil;
import ntnu.it1901.hilala.checksum.core.utils.ChecksumCalculator;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class representing a file data item for each row in the main view.
 */
public class FileEntry {
  //#region Fields
  Logger logger = LoggerFactory.getLogger(FileEntry.class);

  private File file;
  private String fileName;
  private long fileSize;
  private String displayfileSize;
  private Thread hashThread;

  private final IntegerProperty progress = new SimpleIntegerProperty(0);
  private final StringProperty sha256Hash = new SimpleStringProperty("-");
  private final StringProperty md5Hash = new SimpleStringProperty("-");

  private ChecksumCalculator checksumCalculator;
  
  /**
   * Observable property exposing the hashing progress.
   *
   * @return integer value with the approximate progress percentage
   */
  public final IntegerProperty progressProperty() {
    return progress;
  }

  /**
   * Observable property exposing the SHA256 hash. 
   *
   * @return string with the hash values
   */
  public final StringProperty sha256Property() {
    return sha256Hash;
  }

  /**
   * Observable property exposing the MD5 hash.
   *
   * @return string with the hash values
   */
  public final StringProperty md5Property() {
    return md5Hash;
  }
  //#endregion

  //#region getters and setters
  /**
   * Get the filename.
   *
   * @return string with file name
   */
  public String getFileName() {
    return this.fileName;
  }

  /**
   * Get the file absolute path.
   *
   * @return string with path
   */
  public String getAbsolutePath() {
    return this.file.getAbsolutePath();
  }

  /**
   * Get the file size in bytes.
   *
   * @return long value with the size
   */
  public Long getFileSize() {
    return this.fileSize;
  }

  /**
   * Get human friendly file size using standardized units.
   *
   * @return string with file size in human readable format
   */
  public String getDisplayfileSize() {
    return this.displayfileSize;
  }

  /**
   * Get the current hashing progress as a percentage (approximation).
   *
   * @return integer with the 0 to 100 value
   */
  public Integer getProgress() {
    return this.progress.get();
  }

  /**
   * Get the MD5 hash.
   *
   * @return string with hash, "-" if not available
   */
  public String getMd5Hash() {
    return this.md5Hash.get();
  }

  /**
   * Get the SHA 256 hash.
   *
   * @return string with hash, "-" if not available
   */
  public String getSha256Hash() {
    return this.sha256Hash.get();
  }

  //#endregion

  /**
   * Default constructor. 
   * Use FileEntry.setFile() to initialize.
   */
  public FileEntry() {
    this.fileName = "";
    this.fileSize = 0;
  }

  /**
   * Constructor with initialization.
   * Same as calling FileEntry() followed by FileEntry.setFile().
   *
   * @param file file to process
   */
  public FileEntry(File file) {
    setFile(file);
  }

  /**
   * Set the file for this data item entry.
   *
   * @param value file to add
   */
  public void setFile(File value) {
    this.file = value;
    try {
      this.fileSize = Files.size(Paths.get(this.file.getAbsolutePath()));
      this.fileName = this.file.getName();
      this.displayfileSize = FileSizeUtil.humanReadableByteCountStd(this.fileSize);
      this.checksumCalculator = new ChecksumCalculator(this.file);
      this.md5Hash.set(this.checksumCalculator.getMd5());
      this.sha256Hash.set(this.checksumCalculator.getSha256());
      addChecksumCalcListeners();

      hashThread = new Thread(checksumCalculator);
      hashThread.start();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Validate expected MD5 hash vs calculated hash.
   *
   * @param expectedHash expected hash string
   * @return true if matching, false otherwise
   */
  public boolean validateMd5(String expectedHash) {
    return md5Hash.get().equals(expectedHash);
  }

  /**
   * Validate expected SHA 256 hash vs calculated hash.
   *
   * @param expectedHash expected hash string
   * @return true if matching, false otherwise
   */
  public boolean validateSha256(String expectedHash) {
    return sha256Hash.get().equals(expectedHash);
  }

  private void addChecksumCalcListeners() {
    this.checksumCalculator.progressProperty().addListener(new ChangeListener<Number>() {

      @Override
      public void changed(ObservableValue<? extends Number> observable, 
                          Number oldValue, Number newValue) {
        progress.setValue(newValue);
        logger.debug("change event progress " + fileName + " " + newValue);
        if (newValue.intValue() == 100) {
          updateHashes();
          if (!hashThread.isAlive()) {
            hashThread = null;
          }
        }
      }
    });

    this.checksumCalculator.md5Property().addListener(new ChangeListener<String>() {

      @Override
      public void changed(ObservableValue<? extends String> observable, 
                          String oldValue, String newValue) {
        md5Hash.setValue(newValue);
        logger.debug("change event md5 " + fileName);
      }
      
    });

    this.checksumCalculator.sha256Property().addListener(new ChangeListener<String>() {

      @Override
      public void changed(ObservableValue<? extends String> observable, 
                          String oldValue, String newValue) {
        sha256Hash.setValue(newValue);
        logger.debug("change event sha256 " + fileName);
      }
      
    });
  }

  private void updateHashes() {
    this.md5Hash.set(this.checksumCalculator.getMd5());
    this.sha256Hash.set(this.checksumCalculator.getSha256());
    logger.debug("updateHashes" + fileName);
  }
}
