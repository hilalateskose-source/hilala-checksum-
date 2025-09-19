package ntnu.it1901.hilala.checksum.core.utils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

//https://stackoverflow.com/questions/3758606/how-can-i-convert-byte-size-into-a-human-readable-format-in-java

/**
 * Utility class for getting human readable file sizes.
 */
public class FileSizeUtil {
  private FileSizeUtil() {}

  /**
   * Get human readable file size using standardized units.
   *
   * @param bytes file size inbytes
   * @return string representation of file size using standardized units
   */
  public static String humanReadableByteCountStd(long bytes) {
    if (-1000 < bytes && bytes < 1000) {
      return bytes + " B";
    }
    CharacterIterator ci = new StringCharacterIterator("kMGTPE");
    while (bytes <= -999_950 || bytes >= 999_950) {
      bytes /= 1000;
      ci.next();
    }
    return String.format("%.1f %cB", bytes / 1000.0, ci.current());
  }

  /**
   * Get human readable file size using binary units.
   *
   * @param bytes file size inbytes
   * @return string representation of file size using binary units
   */
  public static String humanReadableByteCountBin(long bytes) {
    long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
    if (absB < 1024) {
      return bytes + " B";
    }
    long value = absB;
    CharacterIterator ci = new StringCharacterIterator("KMGTPE");
    for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
      value >>= 10;
      ci.next();
    }
    value *= Long.signum(bytes);
    return String.format("%.1f %ciB", value / 1024.0, ci.current());
  }
}
