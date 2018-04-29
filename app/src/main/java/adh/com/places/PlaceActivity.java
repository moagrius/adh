package adh.com.places;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import adh.com.places.search.models.SingleVenueResponse;
import adh.com.places.search.models.Venue;
import adh.com.places.utils.StaticMaps;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceActivity extends Activity {

  public static final String EXTRA_VENUE_IDENTIFIER = "EXTRA_VENUE_IDENTIFIER";

  private String mVenueIdentifier;
  private Venue mVenue;

  private CoordinatorLayout mContainer;
  private CollapsingToolbarLayout mCollapsingToolbarLayout;
  private ImageView mMapView;
  private ImageView mFavoriteView;
  private TextView mTitleView;
  private TextView mLinkView;
  private TextView mPhoneView;
  private TextView mAddressView;
  private TextView mDescriptionView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_place);
    mVenueIdentifier = getIntent().getStringExtra(EXTRA_VENUE_IDENTIFIER);
    if (mVenueIdentifier == null) {
      return;
    }
    PlacesApplication application = PlacesApplication.from(this);
    Typeface garamond = application.getTypeManager().get("garamond");
    Typeface futura = application.getTypeManager().get("futura");
    mContainer = findViewById(R.id.container);
    mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
    mMapView = findViewById(R.id.image);
    mFavoriteView = findViewById(R.id.favorite);
    mFavoriteView.setOnClickListener(this::toggleFavorite);
    mTitleView = findViewById(R.id.detail_title);
    mTitleView.setTypeface(garamond);
    mLinkView = findViewById(R.id.detail_link);
    mLinkView.setTypeface(futura);
    mPhoneView = findViewById(R.id.detail_phone);
    mPhoneView.setTypeface(futura);
    mAddressView = findViewById(R.id.detail_address);
    mAddressView.setTypeface(garamond);
    mDescriptionView = findViewById(R.id.detail_description);
    mDescriptionView.setTypeface(garamond);
    mContainer.post(this::fetch);
  }

  private void toggleFavorite(View view) {
    PlacesApplication application = PlacesApplication.from(this);
    boolean isFavorite = application.getFavoritesManager().toggle(mVenue.getId());
    mFavoriteView.setImageResource(isFavorite ? R.drawable.ic_fav : R.drawable.ic_fav_empty);
  }

  private void onFetchSuccess() {
    Log.d("ADH", "onFetchSuccess");
    mCollapsingToolbarLayout.setTitle(mVenue.getName());
    mTitleView.setText(mVenue.getName());
    mLinkView.setText(mVenue.getUrl());
    mPhoneView.setText(mVenue.getFormattedPhone());
    mAddressView.setText(TextUtils.join("\n", mVenue.getFormattedAddress()));
    mDescriptionView.setText(mVenue.getDescription());
    int width = mContainer.getMeasuredWidth();
    int height = mContainer.getMeasuredHeight() / 2;
    String url = StaticMaps.getUrl(width, height, mVenue.getLatLng());
    PlacesApplication.from(this).getImageLoader().load(url).into(mMapView);
    mMapView.getLayoutParams().height = height;
    mMapView.getLayoutParams().width = width;
    mMapView.requestLayout();
  }

  private void onFetchFailed() {
    Log.d("ADH", "onFetchFailed");
    // TODO:
  }

  private void fetch() {
    PlacesApplication.from(this).getSearchService().fetch(mVenueIdentifier).enqueue(mFetchVenueCallback);
  }

  private Callback<SingleVenueResponse> mFetchVenueCallback = new Callback<SingleVenueResponse>() {
    @Override
    public void onResponse(Call<SingleVenueResponse> call, Response<SingleVenueResponse> response) {
      if (response.isSuccessful()) {
        mVenue = response.body().getVenue();
        onFetchSuccess();
      } else {
        onFetchFailed();
      }
    }

    @Override
    public void onFailure(Call<SingleVenueResponse> call, Throwable t) {
      Log.d("ADH", "onFailure: " + t.getMessage());
      onFetchFailed();
    }
  };

}
