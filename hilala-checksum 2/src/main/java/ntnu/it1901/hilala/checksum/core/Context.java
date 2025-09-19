package ntnu.it1901.hilala.checksum.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Context will keep state between scenes.
 */
@SuppressWarnings({"java:S6548"}) //singleton is required here. options considered
public class Context {

  private static final Context instance = new Context();

  private List<FileEntry> items = new ArrayList<>();

  private FileEntry selectedEntry = null;

  
  private Context() {}

  /**
   * Get current context.
   *
   * @return Context instance
   */
  public static Context getInstance() {
    return instance;
  }

  /**
   * Set the list of file entries.
   *
   * @param items arraylist with file entries
   */
  public void setItems(List<FileEntry> items) {
    this.items = items;
  }

  /**
   * Get the list of file entries.
   *
   * @return arraylist with file entries.
   */
  public List<FileEntry> getItems() {
    return items;
  }

  /**
   * Set the selected item in file entries.
   *
   * @param item FileEntry with selection, null for no selection
   */
  public void setSelectedEntry(FileEntry item) {
    this.selectedEntry = item;
  }

  /**
   * Get the the selected item in file entries.
   *
   * @return  FileEntry with selection, null for no selection
   */
  public FileEntry getSelectedEntry() {
    return this.selectedEntry;
  }
}
