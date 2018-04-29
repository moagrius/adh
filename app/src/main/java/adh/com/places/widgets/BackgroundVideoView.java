package adh.com.places.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class BackgroundVideoView extends VideoView {

  // this is a custom view that serves as a silent moving background for any screen
  // it's limited to 16:9 aspect ratio in portrait orientation

  private static final float ASPECT_RATIO = 16 / (float) 9;

  public BackgroundVideoView(Context context) {
    super(context);
  }

  public BackgroundVideoView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public BackgroundVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = MeasureSpec.getSize(widthMeasureSpec);
    int height = MeasureSpec.getSize(heightMeasureSpec);
    setMeasuredDimension((int) (width * ASPECT_RATIO), height);
  }

}
