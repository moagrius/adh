package adh.com.places.venues;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import adh.com.places.BaseActivity;
import adh.com.places.PlacesApplication;
import adh.com.places.R;
import adh.com.places.models.SingleVenueResponse;
import adh.com.places.models.Venue;
import adh.com.places.utils.StaticMaps;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VenueDetailActivity extends BaseActivity {

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
    mContainer = findViewById(R.id.container);
    mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
    mMapView = findViewById(R.id.image);
    mFavoriteView = findViewById(R.id.favorite);
    mTitleView = findViewById(R.id.detail_title);
    mLinkView = findViewById(R.id.detail_link);
    mPhoneView = findViewById(R.id.detail_phone);
    mAddressView = findViewById(R.id.detail_address);
    mDescriptionView = findViewById(R.id.detail_description);
    initializeErrorHandler(mContainer);
    mVenueIdentifier = getIntent().getStringExtra(EXTRA_VENUE_IDENTIFIER);
    if (mVenueIdentifier == null) {
      showError("We're unable to show venue details with a valid identifier");
    } else {
      mContainer.post(this::fetch);
      decorateViews();
    }
  }

  private void decorateViews() {
    mFavoriteView.setOnClickListener(this::toggleFavorite);
    PlacesApplication application = PlacesApplication.from(this);
    Typeface garamond = application.getTypeManager().get("garamond");
    Typeface futura = application.getTypeManager().get("futura");
    mTitleView.setTypeface(garamond);
    mLinkView.setTypeface(futura);
    mPhoneView.setTypeface(futura);
    mAddressView.setTypeface(garamond);
    mDescriptionView.setTypeface(garamond);
    if (application.getFavoritesManager().contains(mVenueIdentifier)) {
      mFavoriteView.setImageResource(R.drawable.ic_fav);
    }
  }

  private void toggleFavorite(View view) {
    PlacesApplication application = PlacesApplication.from(this);
    boolean isFavorite = application.getFavoritesManager().toggle(mVenue.getId());
    mFavoriteView.setImageResource(isFavorite ? R.drawable.ic_fav : R.drawable.ic_fav_empty);
  }

  private void onFetchSuccess() {
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

  private void fetch() {
    PlacesApplication.from(this).getSearchService().fetch(
        mVenueIdentifier,
        PlacesApplication.FS_CLIENT_ID,
        PlacesApplication.FS_CLIENT_SECRET,
        PlacesApplication.FS_VERSION
    ).enqueue(mFetchVenueCallback);
  }

  private Callback<SingleVenueResponse> mFetchVenueCallback = new Callback<SingleVenueResponse>() {
    @Override
    public void onResponse(Call<SingleVenueResponse> call, Response<SingleVenueResponse> response) {
      if (response.isSuccessful()) {
        mVenue = response.body().getVenue();
        onFetchSuccess();
      } else {
        showError("We were able to contact the server, but the response was unrecognizable.  Please contact support@example.com");
      }
    }

    @Override
    public void onFailure(Call<SingleVenueResponse> call, Throwable t) {
      showError("There was an error performing this search: " + t.getLocalizedMessage());
    }
  };

}
