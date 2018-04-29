package adh.com.places.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import adh.com.places.R;
import adh.com.places.utils.Files;
import adh.com.places.venues.VenueDetailActivity;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

  public static final String EXTRA_MARKERS = "EXTRA_MARKERS";

  private GoogleMap mMap;
  private List<MarkerInfo> mMarkerInfoList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);
    String compressedMarkersJson = getIntent().getStringExtra(EXTRA_MARKERS);
    if (compressedMarkersJson != null) {
      String markersJson = Files.uncompress(compressedMarkersJson);
      mMarkerInfoList = new Gson().fromJson(markersJson, new TypeToken<List<MarkerInfo>>() {}.getType());
    }
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mMap.setOnInfoWindowClickListener(this::onInfoWindowClick);
    LatLng austin = new LatLng(Austin.LATITUDE, Austin.LONGITUDE);
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(austin, 10f));
    if (mMarkerInfoList != null) {
      for (MarkerInfo info : mMarkerInfoList) {
        addMarker(info);
      }
    }
  }

  private void addMarker(MarkerInfo info) {
    LatLng position = new LatLng(info.getLatitude(), info.getLongitude());
    MarkerOptions markerOptions = new MarkerOptions().position(position).title(info.getName());
    Marker marker = mMap.addMarker(markerOptions);
    marker.setTag(info);
  }

  public void onInfoWindowClick(Marker marker) {
    MarkerInfo info = (MarkerInfo) marker.getTag();
    if (info == null) {
      return;
    }
    Intent intent = new Intent(this, VenueDetailActivity.class);
    intent.putExtra(VenueDetailActivity.EXTRA_VENUE_IDENTIFIER, info.getIdentifier());
    startActivity(intent);
  }

}
