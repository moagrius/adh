package adh.com.places;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.io.IOException;

import adh.com.places.utils.Files;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

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

  @Before
  public void setUp() throws IOException {
   //         .baseUrl(mockWebServer.url("/"))
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(mDispatcher);
    mockWebServer.start();
  }

  @Test
  public void addition_isCorrect() {
    assertEquals(4, 2 + 2);
  }
}