module it1901Course {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.slf4j;
    
    opens ntnu.it1901.hilala.checksum.ui to javafx.fxml;
    opens ntnu.it1901.hilala.checksum.core to javafx.base;
    opens ntnu.it1901.hilala.checksum.core.utils to javafx.base;

    exports ntnu.it1901.hilala.checksum.ui;
}
