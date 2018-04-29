package adh.com.places;

import android.app.Application;

import org.junit.runners.model.InitializationError;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestLifecycle;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;

import java.lang.reflect.Method;

public class PlacesTestRunner extends RobolectricTestRunner {

  public PlacesTestRunner(Class<?> testClass) throws InitializationError {
    super(testClass);
  }

  @Override
  protected Class<? extends TestLifecycle> getTestLifecycleClass() {
    return PlacesTestLifecycle.class;
  }

  @Override
  protected Config buildGlobalConfig() {
    return new Config.Builder()
        .build();
  }

  public static class PlacesTestLifecycle extends DefaultTestLifecycle {

    @Override
    public Application createApplication(Method method, AndroidManifest appManifest, Config config) {
      return new PlacesTestApplication();
    }

  }
}
