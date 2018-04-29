package adh.com.places;

import android.content.Intent;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

import adh.com.places.map.MapActivity;
import adh.com.places.search.SearchActivity;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class SearchActivityTests {

  @Test
  public void fab_shouldBeHiddenAtStart() throws Exception {
    SearchActivity activity = Robolectric.setupActivity(SearchActivity.class);
    assertEquals(View.GONE, activity.findViewById(R.id.fab).getVisibility());
  }

  @Test
  public void clickingFab_shouldStartMapActivity() throws Exception {
    SearchActivity activity = Robolectric.setupActivity(SearchActivity.class);
    activity.findViewById(R.id.fab).performClick();
    Intent expectedIntent = new Intent(activity, MapActivity.class);
    Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
    assertEquals(expectedIntent.getComponent(), actual.getComponent());
  }

}
