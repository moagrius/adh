package adh.com.places.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Venue {

  @SerializedName("id")
  private String mId;
  @SerializedName("url")
  private String mUrl;
  @SerializedName("name")
  private String mName;
  @SerializedName("description")
  private String mDescription;
  @SerializedName("categories")
  private List<VenueCategory> mVenueCategories;
  @SerializedName("location")
  private Location mLocation;
  @SerializedName("contact")
  private Contact mContact;

  public String getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public String getUrl() {
    return mUrl;
  }

  public String getDescription() {
    return mDescription;
  }

  public List<VenueCategory> getVenueCategories() {
    return mVenueCategories;
  }

  public long getDistance() {
    return mLocation == null ? 0 : mLocation.distance;
  }

  public double getLatitude() {
    return mLocation == null ? 0 : mLocation.lat;
  }

  public double getLongitude() {
    return mLocation == null ? 0 : mLocation.lng;
  }

  public String getLatLng() {
    return mLocation == null ? null : (mLocation.lat + "," + mLocation.lng);
  }
  
  public String[] getFormattedAddress() {
    return mLocation == null ? null : mLocation.formattedAddress;
  }
  
  public String getFormattedPhone() {
    return mContact == null ? null : mContact.formattedPhone;
  }

  private static class Location {
    long distance;
    double lat;
    double lng;
    String[] formattedAddress;
  }
  
  private static class Contact {
    String formattedPhone;
  }

}
