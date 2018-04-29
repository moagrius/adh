package adh.com.places.utils;

import android.content.Context;
import android.net.Uri;

public class Resources {

  private static final String RESOURCE_PREFIX_SCHEME = "android.resource://";
  private static final String RESOURCE_RAW_PATH_COMPONENT = "/raw/";

  public static Uri getUriFromRaw(Context context, String file) {
    String path = RESOURCE_PREFIX_SCHEME + context.getPackageName() + RESOURCE_RAW_PATH_COMPONENT + file;
    return Uri.parse(path);
  }

}
