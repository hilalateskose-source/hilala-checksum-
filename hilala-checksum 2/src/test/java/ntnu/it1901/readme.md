# Tests for the checksum calculator

This folder/package contains tests based on TestFX and JUnit for the Checksum Calculator application.

## What is tested

### [FileSizeUtilTest.java](/javafx-app/src/test/java/ntnu/it1901/core/utils/FileSizeUtilTest.java)

This is the test suite for the class dealing with producing human readable file sizes.

### [ChecksumCalculatorTest.java](/javafx-app/src/test/java/ntnu/it1901/core/utils/ChecksumCalculatorTest.java)

This is the test suite for the class dealing with producing human file hashes with the MD5 and SHA256 algorithms.
In the folder [`javafx-app\src\test\resources\it1901\`](/javafx-app/src/test/resources/it1901/) there are the input files for the tests. The expected hashes are produced with command line tools like md5sum and sha256sum.

### UI Tests

The tests simulate clicks on the buttons and other elements to check if the UI  behaves as expected.

#### [AppTest.java](/javafx-app/src/test/java/ntnu/it1901/ui/AppTest.java)

This suite tests basic elements to verify that the UI is loaded as expected.

#### [PrimaryControllerTest.java](/javafx-app/src/test/java/ntnu/it1901/ui/PrimaryControllerTest.java)

This test suite checks the primary view (Overview). There are tests for checking basic interactions as loading files, copying to clipboard and labels.

For example the test for copy to clipboard will attempt to press the "Copy" button and then will look for expected elements in the clipboard contents such as content labels and the hashes themselves.

#### [SecondaryControllerTest.java](/javafx-app/src/test/java/ntnu/it1901/ui/SecondaryControllerTest.java)

This test suite checks the secondary view (Details). There are tests for checking basic interactions as verifying the hash values using the text inputs and also label content.



---------------------------------------------------------------

# hilala-checksum

Individual exercise IT1901 – checksum calculator  
Author: **Hilal Ates Köse (hilala)**  

**Repo:** [https://git.ntnu.no/IT1901-2025-individual/hilala](https://git.ntnu.no/IT1901-2025-individual/hilala)  

---

## Setup

The repository contains a JavaFX project with a Maven setup for minimum Java 17 (tested with Java 17.0.16-tem and 21.0.8-tem), JavaFX 21, and JUnit (Jupiter) 5.12.2 and Test FX 4.0.18 for testing.

---

## Application implementation status

This project is the start of a file checksum calculator.  
- Core logic is in `ChecksumCalculator.java` (partly implemented).  
- FXML files: `ChecksumPrimary.fxml` and `ChecksumSecondary.fxml` (mostly complete).  
- Controllers: `ChecksumPrimaryController` and `ChecksumSecondaryController` (some functionality missing).  

The app starts in the **Overview (ChecksumPrimary view)** with a table and buttons for navigation and actions.  
- **"Load more files"** → select multiple files, table updates, checksums calculated asynchronously (MD5, SHA256).  
- **"Copy"** → copies details of the selected file to clipboard.  
- **"Details ..."** → opens **ChecksumSecondary view**, where you can compare expected vs calculated hashes (checkmark/fail icon shown accordingly).  

---

## Package structure

- Main class: `ntnu.it1901.hilala.checksum.ui.ChecksumApp`  
- Controllers:  
  - `ntnu.it1901.hilala.checksum.ui.ChecksumPrimaryController`  
  - `ntnu.it1901.hilala.checksum.ui.ChecksumSecondaryController`  
- Core logic: `ntnu.it1901.hilala.checksum.core.*`  
- Utilities: `ntnu.it1901.hilala.checksum.core.utils.*`  

---

## How to build and run

From project root:

```bash
cd javafx-app
mvn clean install
mvn test       # run tests (some fail until implementation is complete)
mvn javafx:run # run the app
```

---

## Tests

The project is set up with automatic tests using JUnit and TestFX.  
Tests are located in `src/test/java/ntnu/it1901/hilala/checksum/...` and cover:  
- Core logic (`ChecksumCalculatorTest`, `FileSizeUtilTest`)  
- UI controllers (`ChecksumPrimaryControllerTest`, `ChecksumSecondaryControllerTest`, `AppTest`)  

Currently some tests fail until the exercise is fully completed (Trinn 3).
