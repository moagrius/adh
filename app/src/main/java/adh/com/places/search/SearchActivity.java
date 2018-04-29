package adh.com.places.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import adh.com.places.PlacesApplication;
import adh.com.places.R;
import adh.com.places.map.Austin;
import adh.com.places.map.MapActivity;
import adh.com.places.map.MarkerInfo;
import adh.com.places.search.models.SearchResponse;
import adh.com.places.search.models.Venue;
import adh.com.places.utils.Files;
import adh.com.places.utils.Throttler;
import adh.com.places.utils.Views;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends Activity {

  // TODO: throttle is backwards
  // TODO: KeyStore
  private static final String FS_CLIENT_ID = "D1P1401XOX1EJ21YWZNVLLPXG5FOG3S55NWWYUKHEFYGYXYQ";
  private static final String FS_CLIENT_SECRET = "MCQRCCSQTDAUSILNQTBIP3EKUQZFVJUW3VCHYRAMQ05CU2RP";
  private static final String FS_LATLNG = Austin.LATLNG;
  private static final String FS_VERSION = "20180425";
  private static final int FS_DEFAULT_LIMIT = 20;

  private static final int MINIMUM_QUERY_LENGTH = 4;

  private EditText mEditText;
  private RecyclerView mRecyclerView;
  private FloatingActionButton mFab;
  private ProgressBar mSpinner;
  private Call<SearchResponse> mPendingRequest;
  private SearchAdapter mAdapter = new SearchAdapter();
  private Throttler mThrottler = new Throttler(250);

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(adh.com.places.R.layout.activity_search);

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
  }

  @Override
  protected void onResume() {
    super.onResume();
    mAdapter.notifyDataSetChanged();
    showState();
  }

  private void showState() {
    Log.d("ADH", "items? " + mAdapter.getItemCount());
    if (isFetching()) {
      Views.fadeOut(mFab);
      Views.fadeIn(mSpinner);
    } else {
      Log.d("ADH", "not fetching");
      // only show FAB if we have results
      if (!mAdapter.getItems().isEmpty()) {
        Log.d("ADH", "not fetching");
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
    mThrottler.attempt(this::performSearch);
  }

  private void performSearch() {
    Log.d("ADH", "performSearch");
    String query = mEditText.getText().toString();
    if (!validate(query)) {
      mAdapter.getItems().clear();
      mAdapter.notifyDataSetChanged();
      showState();
      return;
    }
    Log.d("ADH", "past validation");
    if (mPendingRequest != null) {
      mPendingRequest.cancel();
    }
    try {
      query = URLEncoder.encode(query, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      Log.d("ADH", "could not encode response");
      showState();
      return;
    }
    mPendingRequest = PlacesApplication.from(this).getSearchService().search(
        FS_CLIENT_ID,
        FS_CLIENT_SECRET,
        FS_LATLNG,
        query,
        FS_DEFAULT_LIMIT,
        FS_VERSION
    );
    mPendingRequest.enqueue(mSearchCallback);
    showState();
  }

  private boolean validate(String query) {
    return query != null && query.length() >= MINIMUM_QUERY_LENGTH;
  }

  private void onFetchComplete() {
    mPendingRequest = null;
    showState();
  }

  private View.OnClickListener mOpenMapClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      List<MarkerInfo> markers = new ArrayList<>();
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
    }
  };

  private Callback<SearchResponse> mSearchCallback = new Callback<SearchResponse>() {
    @Override
    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
      Log.d("ADH", "got response");
      if (response.isSuccessful()) {
        Log.d("ADH", "respose successful");
        List<Venue> venues = response.body().getResponse().getVenues();
        mAdapter.getItems().clear();
        mAdapter.getItems().addAll(venues);
        mAdapter.notifyDataSetChanged();
      } else {
        Log.d("ADH", "Couldn't parse FS json response");
      }
      onFetchComplete();
    }

    @Override
    public void onFailure(Call<SearchResponse> call, Throwable t) {
      Log.d("ADH", "network error when performing FS search");
      onFetchComplete();
    }
  };

  private TextWatcher mTextWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      // no op
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      performThrottledSearch();
    }

    @Override
    public void afterTextChanged(Editable s) {
      // no op
    }
  };

}
