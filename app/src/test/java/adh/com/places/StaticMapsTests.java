package adh.com.places;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import adh.com.places.utils.StaticMaps;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class StaticMapsTests {

  @Test
  public void getUrl_returnsAppropriateString() throws Exception {
    String expected = "https://maps.googleapis.com/maps/api/staticmap?center=30.2672%2C-97.7431&size=100x100&maptype=roadmap&markers=30.2672%2C-97.7431&markers=0%2C0&key=AIzaSyCvYwUH6O48Bv1vRWlq1drsNi_TbCXAAvs&style=feature%3Aall%7Cinvert_lightness%3Atrue";
    assertEquals(expected, StaticMaps.getUrl(100, 100, "0,0"));
  }

}

