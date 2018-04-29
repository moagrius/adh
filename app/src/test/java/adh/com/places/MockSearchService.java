package adh.com.places;


import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import adh.com.places.models.SearchResponse;
import adh.com.places.models.SingleVenueResponse;
import adh.com.places.search.SearchService;
import adh.com.places.utils.Files;
import retrofit2.Call;

public class MockSearchService implements SearchService {

  @Override
  public Call<SingleVenueResponse> fetch(String identifier) {
    return new MockSearchCall<>(new SingleVenueResponse());
  }

  @Override
  public Call<SearchResponse> search(String clientId, String clientSecret, String latlng, String query, int limit, String version) {
    String path = "../app/src/main/assets/list.json";
    File file = new File(path);
    if (!file.exists()) {
      File here = new File(".");
      throw new RuntimeException("File not found, location is " + here.getAbsolutePath());
    }
    try {
      String json = Files.readFile(file);
      SearchResponse response = new Gson().fromJson(json, SearchResponse.class);
      return new MockSearchCall<>(response);
    } catch (IOException e) {
      throw new RuntimeException("failed to read assets file");
    }
  }
}
