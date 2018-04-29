package adh.com.places.search.models;

public class SingleVenueResponse {

  private Response response;

  public Venue getVenue() {
    return response.venue;
  }

  private static class Response {
    Venue venue;
  }

  /*
  @SerializedName("venue")
  private Venue mVenue;

  public Venue getVenue() {
    return mVenue;
  }
  */

}
