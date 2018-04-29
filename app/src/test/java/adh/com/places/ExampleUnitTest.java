package adh.com.places;

import android.app.Activity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import adh.com.places.models.SearchResponse;
import adh.com.places.models.Venue;
import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(application = PlacesTestApplication.class)
public class ExampleUnitTest {

  @Before
  public void setUp() throws IOException {

  }

  @After
  public void shutDown() throws IOException {

  }

  @Test
  public void listFetch_returnsResults() throws IOException {
    Activity activity = Robolectric.setupActivity(Activity.class);
    PlacesTestApplication application = (PlacesTestApplication) activity.getApplication();
    Call<SearchResponse> call = application.getSearchService().search(null, null, null, null, 0, null);
    Response<SearchResponse> response = call.execute();
    List<Venue> venues = response.body().getResponse().getVenues();
    assertEquals(venues.size(), 20);
  }
}