package adh.com.places.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import adh.com.places.BaseActivity;
import adh.com.places.PlacesApplication;
import adh.com.places.R;
import adh.com.places.map.Austin;
import adh.com.places.map.MapActivity;
import adh.com.places.map.MarkerInfo;
import adh.com.places.models.SearchResponse;
import adh.com.places.models.Venue;
import adh.com.places.utils.Files;
import adh.com.places.utils.Views;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity {

  // so these are pretty obvious as-is.  with proguard, they names would be obfuscated but still
  // trivial to find.  some would argue they should be in a keystore, but while it's possible to spend
  // a great deal of time making it more of a hassle, in my opinion there's not much you can do
  // once a secret touches the client to be truly secure.  in a live app i'd probably make more effort
  // than this.
  private static final String FS_CLIENT_ID = "D1P1401XOX1EJ21YWZNVLLPXG5FOG3S55NWWYUKHEFYGYXYQ";
  private static final String FS_CLIENT_SECRET = "MCQRCCSQTDAUSILNQTBIP3EKUQZFVJUW3VCHYRAMQ05CU2RP";

  private static final String FS_LATLNG = Austin.LATLNG;
  private static final String FS_VERSION = "20180425";
  private static final short FS_DEFAULT_LIMIT = 20;

  private static final short TYPEAHEAD_DELAY = 250;
  private static final short MINIMUM_QUERY_LENGTH = 4;

  private EditText mEditText;
  private RecyclerView mRecyclerView;
  private FloatingActionButton mFab;
  private ProgressBar mSpinner;
  private Call<SearchResponse> mPendingRequest;
  private SearchAdapter mAdapter = new SearchAdapter();
  private Handler mHandler = new Handler();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    mEditText = findViewById(R.id.search);
    mFab = findViewById(R.id.fab);
    mSpinner = findViewById(R.id.spinner);
    mRecyclerView = findViewById(R.id.recyclerview);
    mRecyclerView.setVerticalFadingEdgeEnabled(false);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mAdapter);
    mFab.setOnClickListener(mOpenMapClickListener);
    PlacesApplication application = PlacesApplication.from(this);
    mEditText.setTypeface(application.getTypeManager().get("futura"));
    mEditText.addTextChangedListener(mTextWatcher);
    initializeErrorHandler(mFab);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mAdapter.notifyDataSetChanged();
    showState();
  }

  private void showState() {
    if (isFetching()) {
      Views.fadeOut(mFab);
      Views.fadeIn(mSpinner);
    } else {
      // only show FAB if we have results
      if (!mAdapter.getItems().isEmpty()) {
        Views.fadeIn(mFab);
      } else {
        Views.fadeOut(mFab);
      }
      Views.fadeOut(mSpinner);
    }
  }

  private boolean isFetching() {
    return mPendingRequest != null;
  }

  private void performThrottledSearch() {
    mHandler.removeCallbacks(this::performSearch);
    mHandler.postDelayed(this::performSearch, TYPEAHEAD_DELAY);
  }

  private void performSearch() {
    String query = mEditText.getText().toString();
    try {
      query = URLEncoder.encode(query, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      showError("We weren't able to encode some characters - please try again");
      showState();
      return;
    }
    if (!validate(query)) {
      showError("Search term must be at least " + MINIMUM_QUERY_LENGTH + " characters");
      mAdapter.getItems().clear();
      mAdapter.notifyDataSetChanged();
      showState();
      return;
    }
    if (mPendingRequest != null) {
      mPendingRequest.cancel();
    }
    mPendingRequest = PlacesApplication.from(this).getSearchService().search(FS_CLIENT_ID, FS_CLIENT_SECRET, FS_LATLNG, query, FS_DEFAULT_LIMIT, FS_VERSION);
    mPendingRequest.enqueue(mSearchCallback);
    showState();
  }

  private boolean validate(String query) {
    return query != null && query.length() >= MINIMUM_QUERY_LENGTH;
  }

  // fires when fetch is complete, whether successful or not
  private void onFetchResolved() {
    mPendingRequest = null;
    showState();
  }

  private View.OnClickListener mOpenMapClickListener = v -> {
    List<MarkerInfo> markers = new ArrayList<>();
    // transform to MarkerInfo instances for a smaller payload
    for (Venue venue : mAdapter.getItems()) {
      MarkerInfo info = new MarkerInfo();
      info.setName(venue.getName());
      info.setIdentifier(venue.getId());
      info.setLatitude(venue.getLatitude());
      info.setLongitude(venue.getLongitude());
      markers.add(info);
    }
    String json = new Gson().toJson(markers);
    String compressed = Files.compress(json);
    Intent intent = new Intent(SearchActivity.this, MapActivity.class);
    intent.putExtra(MapActivity.EXTRA_MARKERS, compressed);
    startActivity(intent);
  };

  private Callback<SearchResponse> mSearchCallback = new Callback<SearchResponse>() {
    @Override
    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
      if (response.isSuccessful()) {
        List<Venue> venues = response.body().getResponse().getVenues();
        mAdapter.getItems().clear();
        mAdapter.getItems().addAll(venues);
        mAdapter.notifyDataSetChanged();
      } else {
        showError("We were able to contact the server, but the response was unrecognizable.  Please contact support@example.com");
      }
      onFetchResolved();
    }

    @Override
    public void onFailure(Call<SearchResponse> call, Throwable t) {
      showError("There was an error performing this search: " + t.getLocalizedMessage());
      onFetchResolved();
    }
  };

  private TextWatcher mTextWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      // no op
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      // no op
    }

    @Override
    public void afterTextChanged(Editable s) {
      performThrottledSearch();
    }
  };

}
