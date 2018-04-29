package adh.com.places;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

import adh.com.places.search.SearchService;

@Implements(PlacesApplication.class)
public class ShadowPlacesApplication {

  private MockSearchService mMockSearchService = new MockSearchService();

  @RealObject
  private PlacesApplication mRealPlacesApplication;

  @Implementation
  public SearchService getSearchService() {
    return mMockSearchService;
  }

}