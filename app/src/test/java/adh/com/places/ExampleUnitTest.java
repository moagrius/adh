package adh.com.places;

import android.app.Activity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import adh.com.places.models.SearchResponse;
import adh.com.places.models.Venue;
import adh.com.places.search.SearchService;
import adh.com.places.utils.Files;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.Assert.assertEquals;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest {

  private Activity mActivity;
  private MockWebServer mMockWebServer;
  private SearchService mSearchService;
  private Dispatcher mDispatcher = new Dispatcher() {
    @Override
    public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
      MockResponse response = new MockResponse()
          .addHeader("Content-Type", "application/json; charset=utf-8")
          .addHeader("Cache-Control", "no-cache")
          .setResponseCode(200);
      String path = request.getPath();
      System.out.println("path=" + path);
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

  @Before
  public void setUp() throws IOException {
    //mActivity = Robolectric.setupActivity(.class);
    mMockWebServer = new MockWebServer();
    mMockWebServer.setDispatcher(mDispatcher);
    mSearchService = new Retrofit.Builder()
        .baseUrl(mMockWebServer.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchService.class);
  }

  @After
  public void shutDown() throws IOException {
    mMockWebServer.shutdown();
  }

  @Test
  public void listFetch_returnsResults() throws IOException {
    Call<SearchResponse> call = mSearchService.search(null, null, null, null, 0, null);
    List<Venue> venues = call.execute().body().getResponse().getVenues();
    assertEquals(venues.size(), 20);
  }
}