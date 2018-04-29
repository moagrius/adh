package adh.com.places.map;

public class MarkerInfo {

  private double mLatitude;
  private double mLongitude;
  private String mName;
  private String mIdentifier;

  public double getLatitude() {
    return mLatitude;
  }

  public void setLatitude(double latitude) {
    mLatitude = latitude;
  }

  public double getLongitude() {
    return mLongitude;
  }

  public void setLongitude(double longitude) {
    mLongitude = longitude;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name;
  }

  public String getIdentifier() {
    return mIdentifier;
  }

  public void setIdentifier(String identifier) {
    mIdentifier = identifier;
  }

}
