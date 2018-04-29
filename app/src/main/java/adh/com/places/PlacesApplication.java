package adh.com.places;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import adh.com.places.favorites.FavoritesManager;
import adh.com.places.format.TypeManager;
import adh.com.places.search.SearchService;
import adh.com.places.search.models.SearchResponse;
import adh.com.places.search.models.SingleVenueResponse;
import adh.com.places.utils.Files;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacesApplication extends Application {

  public static final String SHARED_PREFS_FILE = "prefs";

  private TypeManager mTypeManager;
  private SearchService mSearchService;
  private Picasso mImageLoader;
  private FavoritesManager mFavoritesManager;

  public static PlacesApplication from(Context context) {
    return (PlacesApplication) context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    // debugging
    Stetho.initializeWithDefaults(this);
    // debug

    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(new Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        MockResponse response = new MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .setResponseCode(200);
        String path = request.getPath();
        Log.d("ADH", "path=" + path);
        if (path.contains("venues/search")){
          try {
            response.setBody(Files.readFileFromAssets(PlacesApplication.this, "list.json"));
            return response;
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        if (path.contains("venues")) {
          try {
            response.setBody(Files.readFileFromAssets(PlacesApplication.this, "detail.json"));
            return response;
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        return new MockResponse().setResponseCode(404);
      }
    });
    new Thread(() -> {
      try {
        mockWebServer.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();


    // manage favorites
    mFavoritesManager = new FavoritesManager(this);
    // type manager instance
    mTypeManager = new TypeManager();
    mTypeManager.register(this, "garamond", "garamond.otf");
    mTypeManager.register(this, "futura", "futura.otf");
    // search service instance
    OkHttpClient httpClient = new OkHttpClient.Builder()
        .addNetworkInterceptor(new StethoInterceptor())
        .build();

    mSearchService = new Retrofit.Builder()
        .client(httpClient)
        //.baseUrl("https://api.foursquare.com/v2/")
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchService.class);

    // image loader
    mImageLoader = Picasso.with(this);

  }

  public TypeManager getTypeManager() {
    return mTypeManager;
  }

  public SearchService getSearchService() {
    return mSearchService;
  }

  public Picasso getImageLoader() {
    return mImageLoader;
  }

  public FavoritesManager getFavoritesManager() {
    return mFavoritesManager;
  }

  private static class MockSearchService implements SearchService {

    @Override
    public Call<SingleVenueResponse> fetch(String identifier) {
      return null;
    }

    @Override
    public Call<SearchResponse> search(String clientId, String clientSecret, String latlng, String query, int limit, String version) {
      return null;
    }

  }

}
