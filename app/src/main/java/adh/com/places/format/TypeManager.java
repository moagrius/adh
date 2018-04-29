package adh.com.places.format;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

public class TypeManager extends HashMap<String, Typeface> {

  public void register(Context context, String key, String file) {
    put(key, Typeface.createFromAsset(context.getAssets(), file));
  }

}
