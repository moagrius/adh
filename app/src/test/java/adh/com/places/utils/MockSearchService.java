package adh.com.places.utils;


import com.google.gson.Gson;

import adh.com.places.models.SearchResponse;
import adh.com.places.models.SingleVenueResponse;
import adh.com.places.search.SearchService;
import retrofit2.Call;

public class MockSearchService implements SearchService {

  @Override
  public Call<SingleVenueResponse> fetch(String identifier) {
    String json = TestUtils.getStringFromAssetsFile("detail.json");
    SingleVenueResponse response = new Gson().fromJson(json, SingleVenueResponse.class);
    return new MockSearchCall<>(response);
  }

  @Override
  public Call<SearchResponse> search(String clientId, String clientSecret, String latlng, String query, int limit, String version) {
    String json = TestUtils.getStringFromAssetsFile("list.json");
    SearchResponse response = new Gson().fromJson(json, SearchResponse.class);
    return new MockSearchCall<>(response);
  }

}
