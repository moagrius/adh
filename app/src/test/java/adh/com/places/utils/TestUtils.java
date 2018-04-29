package adh.com.places.utils;

import java.io.File;
import java.io.IOException;

public class TestUtils {

  private static final String ASSET_PATH = "../app/src/test/assets/";

  public static String getStringFromAssetsFile(String name) {
    File file = new File(ASSET_PATH + name);
    if (!file.exists()) {
      throw new RuntimeException("File not found");
    }
    try {
      return Files.readFile(file);
    } catch (IOException e) {
      throw new RuntimeException("failed to read assets file");
    }
  }
}
