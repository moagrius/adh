package adh.com.places;

import android.util.Log;

import org.robolectric.RuntimeEnvironment;
import org.robolectric.TestLifecycleApplication;

import java.io.IOException;
import java.lang.reflect.Method;

import adh.com.places.search.SearchService;
import adh.com.places.utils.Files;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacesTestApplication extends PlacesApplication implements TestLifecycleApplication {

  @Override
  public void onCreate() {

  }

  private Dispatcher mDispatcher = new Dispatcher() {
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
          response.setBody(Files.readFileFromAssets(RuntimeEnvironment.application, "list.json"));
          return response;
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (path.contains("venues")) {
        try {
          response.setBody(Files.readFileFromAssets(RuntimeEnvironment.application, "detail.json"));
          return response;
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      return new MockResponse().setResponseCode(404);
    }
  };

  private MockWebServer mMockWebServer;
  private SearchService mMockSearchService;

  public SearchService getSearchService() {
    if (mMockSearchService == null) {
      mMockSearchService = new Retrofit.Builder()
          .baseUrl(getMockWebServer().url("/"))
          .addConverterFactory(GsonConverterFactory.create())
          .build()
          .create(SearchService.class);
    }
    return mMockSearchService;
  }

  public MockWebServer getMockWebServer() {
    if (mMockWebServer == null) {
      mMockWebServer = new MockWebServer();
      mMockWebServer.setDispatcher(mDispatcher);
    }
    return mMockWebServer;
  }

  @Override
  public void beforeTest(Method method) {

  }

  @Override
  public void prepareTest(Object test) {

  }

  @Override
  public void afterTest(Method method) {

  }

}
