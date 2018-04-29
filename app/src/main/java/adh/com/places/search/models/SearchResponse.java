package adh.com.places.search.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

  @SerializedName("response")
  private Response mResponse;

  public Response getResponse() {
    return mResponse;
  }

  public static class Response {

    @SerializedName("venues")
    private List<Venue> mVenues;

    public List<Venue> getVenues() {
      return mVenues;
    }

  }

}
