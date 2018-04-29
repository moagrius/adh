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
import adh.com.places.models.SingleVenueResponse;
import adh.com.places.models.Venue;
import adh.com.places.utils.PlacesTestApplication;
import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(application = PlacesTestApplication.class)
public class ServicesTests {

  private PlacesTestApplication mApplication;

  @Before
  public void setUp() throws IOException {
    Activity activity = Robolectric.setupActivity(Activity.class);
    mApplication = (PlacesTestApplication) activity.getApplication();
  }

  @After
  public void shutDown() throws IOException {
    mApplication = null;
  }

  @Test
  public void listFetch_returnsResults() throws IOException {
    Call<SearchResponse> call = mApplication.getSearchService().search(null, null, null, null, 0, null);
    Response<SearchResponse> response = call.execute();
    List<Venue> venues = response.body().getResponse().getVenues();
    assertEquals(20, venues.size());
    assertEquals("51a81f83498e28b902053728", venues.get(0).getId());
  }

  @Test
  public void detailFetch_returnsResult() throws IOException {
    Call<SingleVenueResponse> call = mApplication.getSearchService().fetch(null);
    Response<SingleVenueResponse> response = call.execute();
    Venue venue = response.body().getVenue();
    assertEquals("51a81f83498e28b902053728", venue.getId());
    assertEquals("Chick-fil-A", venue.getName());
  }

}