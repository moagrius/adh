package adh.com.places;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.picasso.Picasso;

import adh.com.places.favorites.FavoritesManager;
import adh.com.places.format.TypeManager;
import adh.com.places.search.SearchService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacesApplication extends Application {

  public static final String SHARED_PREFS_FILE = "prefs";

  private TypeManager mTypeManager;
  private SearchService mSearchService;
  private Picasso mImageLoader;
  private FavoritesManager mFavoritesManager;

  public static PlacesApplication from(Context context) {
    return (PlacesApplication) context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    // debug
    Stetho.initializeWithDefaults(this);
    // image loader
    mImageLoader = Picasso.with(this);
    // favorites manager instance
    mFavoritesManager = new FavoritesManager(this);
    // type manager instance
    mTypeManager = new TypeManager();
    mTypeManager.register(this, "garamond", "garamond.otf");
    mTypeManager.register(this, "futura", "futura.otf");
    // search service instance
    OkHttpClient httpClient = new OkHttpClient.Builder()
        .addNetworkInterceptor(new StethoInterceptor())
        .build();
    mSearchService = new Retrofit.Builder()
        .client(httpClient)
        .baseUrl("https://api.foursquare.com/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchService.class);
  }

  public TypeManager getTypeManager() {
    return mTypeManager;
  }

  public SearchService getSearchService() {
    return mSearchService;
  }

  public Picasso getImageLoader() {
    return mImageLoader;
  }

  public FavoritesManager getFavoritesManager() {
    return mFavoritesManager;
  }

}
