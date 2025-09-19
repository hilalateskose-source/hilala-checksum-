package ntnu.it1901.hilala.checksum.core.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class to calculate MD5 and SHA256 hashes for a given file.
 */
public class ChecksumCalculator implements Runnable {
  Logger logger = LoggerFactory.getLogger(ChecksumCalculator.class);

  private File file = null;
  private Long size;
  private int stepsMax;
  private int steps;
  private static final int BUFFER_SIZE = 8192;
  private final IntegerProperty progress = new SimpleIntegerProperty(0);
  private final StringProperty sha256 = new SimpleStringProperty("-");
  private final StringProperty md5 = new SimpleStringProperty("-");

  /**
   * Observable property with the progress hashing the file.
   *
   * @return integer value from 0 to 100
   */
  public final IntegerProperty progressProperty() {
    return progress;
  }

  /**
   * Observable property with the SHA 256 hash of the file.
   *
   * @return string with the SHA 256 hash
   */
  public final StringProperty sha256Property() {
    return sha256;
  }

  /**
   * Observable property with the MD5 hash of the file.
   *
   * @return string with the MD5 hash
   */
  public final StringProperty md5Property() {
    return md5;
  }

  /**
   * Default simple constructor.
   * Use this constructor if you need the instance
   * but want to start processing later 
   * by setting the file property
   *
   * @see setFile
   */
  public ChecksumCalculator() {
  }

  /**
   * Path constructor.
   * Use a path to construct an  instance of this class
   *
   * @param path path to the file to process
   * @throws IOException exception
   */
  public ChecksumCalculator(String path) {
    try {
      this.setFile(new File(path));  
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage(), e);
    }
  }

  /**
   * File constructor.
   * Use a File to construct an  instance of this class
   *
   * @param file file to process
   * @throws IOException exception
   */
  public ChecksumCalculator(File file) {
    try {
      this.setFile(file);  
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage(), e);
    }
  }

  /**
   * Initializer method.
   * Initialize the file for this instance
   *
   * @param file the file to process
   * @throws IOException exception
   */
  public synchronized void setFile(File file) throws IOException {
    this.file = file;
    this.size = Files.size(Paths.get(this.file.getAbsolutePath()));
  }

  public String getMd5() {
    return md5.get();
  }

  public String getSha256() {
    return sha256.get();
  }

  public int getProgress() {
    return progress.get();
  }

  /**
   * Calculate the file hashes.
   * Method will update the progress as it goes
   *
   * @throws NoSuchAlgorithmException exception if hashing algorithm is not found
   * @throws IOException exception
   */
  public synchronized void calculate() throws NoSuchAlgorithmException, IOException {
    stepsMax = (int) (size / BUFFER_SIZE) * 2;
    if (stepsMax == 0) {
      stepsMax = 2;
    }
    
    steps = 0;
    calculateMd5();
    calculateSha256();
    progress.set(100);
  }

  private synchronized void updateProgress() {
    progress.set((steps * 100) / stepsMax);
  }

  private synchronized void incSteps() {
    steps++;
  }

  private synchronized void calculateMd5() throws NoSuchAlgorithmException, IOException {
    //TODO: calculate MD5 hash, set md5 property
  }

  private synchronized void calculateSha256()  throws NoSuchAlgorithmException, IOException {
   //TODO: calculate SHA 256 hash, set sha256 property
  }

  @Override
  public void run() {
    try {
      calculate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
