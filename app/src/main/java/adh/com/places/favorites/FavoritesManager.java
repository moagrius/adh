package adh.com.places.favorites;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import adh.com.places.PlacesApplication;

public class FavoritesManager {

  public static final String PREFS_FAVORITES_KEY = "favorites";

  private Context mContext;  // only application context
  private Set<String> mFavorites = new HashSet<>();

  public FavoritesManager(Context context) {
    mContext = context;
    Set<String> saved = getSharedPreferences().getStringSet(PREFS_FAVORITES_KEY, null);
    if (saved != null) {
      mFavorites.addAll(saved);
    }
  }

  private SharedPreferences getSharedPreferences() {
    return mContext.getSharedPreferences(PlacesApplication.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
  }

  private void save() {
    SharedPreferences.Editor editor = getSharedPreferences().edit();
    editor.putStringSet(PREFS_FAVORITES_KEY, mFavorites);
    editor.apply();
  }

  public boolean add(String identifier) {
    boolean changed = mFavorites.add(identifier);
    if (changed) {
      save();
    }
    return changed;
  }

  public boolean remove(String identifier) {
    boolean changed = mFavorites.remove(identifier);
    if (changed) {
      save();
    }
    return changed;
  }

  public boolean toggle(String identifier) {
    if (contains(identifier)) {
      remove(identifier);
      return false;
    }
    return add(identifier);
  }

  public boolean contains(String identifier) {
    return mFavorites.contains(identifier);
  }

}
