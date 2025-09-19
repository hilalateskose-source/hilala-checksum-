package ntnu.it1901.hilala.checksum.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Test suite FileSizeUtil.
 */
class FileSizeUtilTest {

  @ParameterizedTest
  @DisplayName("Checksum calculator should give the same results as other tools")
  @CsvSource({
    "0,0 B,0 B",
    "27,27 B,27 B",
    "999,999 B,999 B",
    "1000,1.0 kB,1000 B",
    "1001,1.0 kB,1001 B",
    "1023,1.0 kB,1023 B",
    "1024,1.0 kB,1.0 KiB",
    "1025,1.0 kB,1.0 KiB",
    "1728,1.7 kB,1.7 KiB",
    "110592,110.6 kB,108.0 KiB",
    "999999,1.0 MB,976.6 KiB",
    "1000000,1.0 MB,976.6 KiB",
    "1000001,1.0 MB,976.6 KiB",
    "1048575,1.0 MB,1.0 MiB",
    "1048576,1.0 MB,1.0 MiB",
    "1048577,1.0 MB,1.0 MiB",
    "7077888,7.1 MB,6.8 MiB",
    "452984832,453.0 MB,432.0 MiB",
    "999999999,1.0 GB,953.7 MiB",
    "1000000000,1.0 GB,953.7 MiB",
    "1073741823,1.1 GB,1.0 GiB",
    "1073741824,1.1 GB,1.0 GiB",
    "1073741825,1.1 GB,1.0 GiB",
    "28991029248,29.0 GB,27.0 GiB",
    "1855425871872,1.9 TB,1.7 TiB",
    "9223372036854775807,9.2 EB,8.0 EiB"
  })
  void check_humanReadableByteCount(ArgumentsAccessor argumentsAccessor) {
    long bytes = argumentsAccessor.getLong(0);
    String expectedHumanReadableStd = argumentsAccessor.getString(1);
    String expectedHumanReadableBin = argumentsAccessor.getString(2);
    try {
      assertEquals(expectedHumanReadableBin, FileSizeUtil.humanReadableByteCountBin(bytes));
      assertEquals(expectedHumanReadableStd, FileSizeUtil.humanReadableByteCountStd(bytes));  
    } catch (Exception e) {
      fail(e);
    }
  }
}
