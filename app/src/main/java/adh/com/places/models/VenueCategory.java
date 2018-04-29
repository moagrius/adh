package adh.com.places.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VenueCategory implements Serializable {

  private static final int DEFAULT_SIZE = 88;  // largest available.  at xxx, that's 22dpi
  private static final String COLOR_URL_COMPONENT = "bg_";

  @SerializedName("name")
  private String mName;
  @SerializedName("icon")
  private Icon mIcon;

  public String getName() {
    return mName;
  }

  public String getUrl() {
    if (mIcon == null) {
       return null;
    }
    return mIcon.prefix + COLOR_URL_COMPONENT + DEFAULT_SIZE + mIcon.suffix;
  }

  private static class Icon implements Serializable {
    String prefix;
    String suffix;
  }

}
