package adh.com.places.search;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adh.com.places.PlaceActivity;
import adh.com.places.PlacesApplication;
import adh.com.places.R;
import adh.com.places.search.models.Venue;
import adh.com.places.search.models.VenueCategory;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchItemHolder> {

  private List<Venue> mItems = new ArrayList<>();

  public List<Venue> getItems() {
    return mItems;
  }

  @Override
  public SearchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);
    View itemView = inflater.inflate(R.layout.row_search_result, parent, false);
    PlacesApplication application = PlacesApplication.from(context);
    SearchItemHolder holder = new SearchItemHolder(itemView);
    holder.itemView.setOnClickListener(mRowClickListener);
    holder.favoriteView.setOnClickListener(mFavoriteClickListener);
    Typeface typeface = application.getTypeManager().get("garamond");
    holder.titleView.setTypeface(typeface);
    return holder;
  }

  @Override
  public void onBindViewHolder(SearchItemHolder holder, int position) {
    try {
      Venue venue = mItems.get(position);
      holder.itemView.setTag(position);
      holder.favoriteView.setTag(position);
      Context context = holder.itemView.getContext();
      Resources resources = context.getResources();
      PlacesApplication application = PlacesApplication.from(context);
      holder.titleView.setText(venue.getName());
      String distanceLabel = resources.getString(R.string.template_distance, venue.getDistance() / (float) 1000);
      holder.distanceView.setText(distanceLabel);
      int favoriteIconResourceId = application.getFavoritesManager().contains(venue.getId()) ? R.drawable.ic_fav : R.drawable.ic_fav_empty;
      holder.favoriteView.setImageResource(favoriteIconResourceId);
      if (venue.getVenueCategories() != null && !venue.getVenueCategories().isEmpty()) {
        VenueCategory category = venue.getVenueCategories().get(0);
        Log.d("ADH", "found icon: " + category.getUrl());
        application.getImageLoader().load(category.getUrl()).into(holder.iconView);
        String categoryLabel = resources.getString(R.string.template_category, category.getName());
        holder.categoryView.setText(categoryLabel);
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      Log.e("ADH", "item not found in recycler");
    }
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }

  private View.OnClickListener mRowClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      Integer position = (Integer) v.getTag();
      if (position != null) {
        try {
          Venue venue = mItems.get(position);
          Intent intent = new Intent(v.getContext(), PlaceActivity.class);
          intent.putExtra(PlaceActivity.EXTRA_VENUE_IDENTIFIER, venue.getId());
          v.getContext().startActivity(intent);
        } catch (ArrayIndexOutOfBoundsException e) {
          // position not in bounds
        }
      }
    }
  };

  private View.OnClickListener mFavoriteClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      Integer position = (Integer) v.getTag();
      if (position != null) {
        try {
          Venue venue = mItems.get(position);
          PlacesApplication application = PlacesApplication.from(v.getContext());
          application.getFavoritesManager().toggle(venue.getId());
          notifyItemChanged(position);
        } catch (ArrayIndexOutOfBoundsException e) {
          // bad position
        }
      }
    }
  };

  static class SearchItemHolder extends RecyclerView.ViewHolder {

    TextView titleView;
    TextView categoryView;
    TextView distanceView;
    ImageView favoriteView;
    ImageView iconView;

    SearchItemHolder(View itemView) {
      super(itemView);
      titleView = itemView.findViewById(R.id.search_row_title);
      categoryView = itemView.findViewById(R.id.search_row_category);
      distanceView = itemView.findViewById(R.id.search_row_distance);
      iconView = itemView.findViewById(R.id.icon);
      favoriteView = itemView.findViewById(R.id.favorite);
    }

  }

}