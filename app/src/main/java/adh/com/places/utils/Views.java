package adh.com.places.utils;

import android.view.View;
import android.view.ViewGroup;

public class Views {

  public static void fadeIn(View view) {
    view.setVisibility(View.VISIBLE);
    view.animate().alpha(1f);
  }

  public static void fadeOut(View view) {
    view.animate().alpha(0f).withEndAction(() -> view.setVisibility(View.GONE));
  }

  public static ViewGroup getParent(View view) {
    return (ViewGroup) view.getParent();
  }

}
