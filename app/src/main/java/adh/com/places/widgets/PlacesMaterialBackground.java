package adh.com.places.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import adh.com.places.R;

/**
 * Created by michaeldunn on 11/11/16.
 *
 * This is just a MaterialBackgroundView with some pre-configured overlays
 */
public class PlacesMaterialBackground extends MaterialBackgroundView {

  public PlacesMaterialBackground(Context context) {
    this(context, null);
  }

  public PlacesMaterialBackground(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PlacesMaterialBackground(Context context, AttributeSet attrs, int defStyleAttr) {

    super(context, attrs, defStyleAttr);

    int primaryColor = ContextCompat.getColor(context, R.color.colorPrimary);
    int secondaryColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);
    int accentColor = ContextCompat.getColor(context, R.color.colorAccent);

    setBackgroundColor(0xFF444444);

    Layer firstLayer = new Layer();
    firstLayer.setAngle(15);
    firstLayer.setColorStart(accentColor);
    firstLayer.setColorEnd(accentColor);

    Layer secondLayer = new Layer();
    secondLayer.setAngle(15);
    secondLayer.setColorStart(primaryColor);
    secondLayer.setColorEnd(primaryColor);

    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    firstLayer.setOffset((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, metrics));
    secondLayer.setOffset((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 360, metrics));

    addLayer(firstLayer);
    //addLayer(secondLayer);

    invalidate();

  }

}
