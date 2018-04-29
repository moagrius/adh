package adh.com.places.utils;

import android.net.Uri;

import adh.com.places.map.Austin;

public class StaticMaps {

  private static final String GOOGLE_STATIC_MAPS_API_KEY = "AIzaSyCvYwUH6O48Bv1vRWlq1drsNi_TbCXAAvs";
  private static final String BASE_URL = "https://maps.googleapis.com/maps/api/staticmap?";

  public static String getUrl(int width, int height, String latlng) {
    return Uri.parse(BASE_URL)
        .buildUpon()
        .appendQueryParameter("center", Austin.LATLNG)
        .appendQueryParameter("size", width + "x" + height)
        .appendQueryParameter("maptype", "roadmap")
        .appendQueryParameter("markers", Austin.LATLNG)
        .appendQueryParameter("markers", latlng)
        .appendQueryParameter("key", GOOGLE_STATIC_MAPS_API_KEY)
        .appendQueryParameter("style", "feature:all|invert_lightness:true")
        .build()
        .toString();
  }

}
