package adh.com.places.search;

import adh.com.places.search.models.SearchResponse;
import adh.com.places.search.models.SingleVenueResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SearchService {

  @GET("venues/{identifier}")
  Call<SingleVenueResponse> fetch(@Path("identifier") String identifier);

  @GET("venues/search")
  Call<SearchResponse> search(
      @Query("client_id") String clientId,
      @Query("client_secret") String clientSecret,
      @Query("ll") String latlng,
      @Query("query") String query,
      @Query("limit") int limit,
      @Query("v") String version);

}