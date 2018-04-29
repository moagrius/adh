package adh.com.places;

import android.app.Application;

import org.robolectric.annotation.Implementation;

import adh.com.places.search.SearchService;

public class PlacesTestApplication extends Application {

  private MockSearchService mMockSearchService = new MockSearchService();

  @Implementation
  public SearchService getSearchService() {
    return mMockSearchService;
  }

}
