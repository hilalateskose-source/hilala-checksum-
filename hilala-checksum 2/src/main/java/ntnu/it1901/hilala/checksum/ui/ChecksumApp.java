package ntnu.it1901.hilala.checksum.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ChecksumApp extends Application {

  private static Scene scene;

  // >>> TESTLER İÇİN EKLENECEK: Son yüklenen controller'ı tut
  private static Object lastController;

  @Override
  public void start(Stage stage) throws IOException {
    scene = new Scene(loadFxml("ChecksumPrimary"), 950, 500); // 'primary' küçük harfli
    stage.setScene(scene);
    stage.show();
  }

  static void setRoot(String name) throws IOException {
    scene.setRoot(loadFxml(name));
  }

  private static Parent loadFxml(String name) throws IOException {
    String path = "/ntnu/it1901/hilala/checksum/ui/" + name + ".fxml";
    URL url = ChecksumApp.class.getResource(path);
    if (url == null) {
      throw new IllegalStateException("FXML not found on classpath: " + path);
    }
    FXMLLoader loader = new FXMLLoader(url);
    Parent root = loader.load();
    // >>> TESTLER İÇİN: controller referansını sakla
    lastController = loader.getController();
    return root;
  }

  // >>> TESTLERİN İSTEDİĞİ GETTER’LAR
  public static Scene getScene() {
    return scene;
  }

  public static Controller getController() {
    return (Controller) lastController; // Controller arayüzünü controller’lar zaten implemente ediyor
  }

  public static void main(String[] args) {
    launch();
  }
}

