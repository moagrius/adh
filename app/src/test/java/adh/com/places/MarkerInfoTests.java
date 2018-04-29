package adh.com.places;

import org.junit.Test;

import adh.com.places.map.MarkerInfo;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertEquals;

public class MarkerInfoTests {
  @Test
  public void nameSetter_returnsCorrectValueFromGetter() throws Exception {
    MarkerInfo info = new MarkerInfo();
    info.setName("bob");
    assertEquals("bob", info.getName());
    assertNull(info.getIdentifier());
  }
}
