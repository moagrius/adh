package adh.com.places.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaeldunn on 4/29/16.
 */
public class MaterialBackgroundView extends View {

  private static final int SHADOW_COLOR = 0xD3141414;
  private static final int SHADOW_BLUR = 4;

  private List<Layer> mLayers = new ArrayList<>();

  private Paint mNoisePaint = new Paint();
  private Paint mShadowPaint = new Paint();

  {
    mShadowPaint.setColor(SHADOW_COLOR);
    mShadowPaint.setAntiAlias(true);
  }

  private RectF mMeasuringRect = new RectF();

  public MaterialBackgroundView(Context context) {
    this(context, null);
  }

  public MaterialBackgroundView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  /**
   * This View represents a specific graphic similar to Material Design background images
   */
  public MaterialBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initShadowPaint();
    initNoisePaint();
    // blur won't work with hardware acceleration
    setLayerType(LAYER_TYPE_SOFTWARE, null);
  }

  /**
   * The shadow will be built by filling the entire plane with a paint that has a blur applied to it,
   * and placing that one z-level beneath the gradient and noise layers.
   */
  private void initShadowPaint() {
    float radius = SHADOW_BLUR * getResources().getDisplayMetrics().density;
    BlurMaskFilter blurMaskFilter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.OUTER);
    mShadowPaint.setMaskFilter(blurMaskFilter);
  }

  /**
   * Decode the noise overlay image and apply it to a bitmap shader, then set that shader to a Paint.
   * Drawing with this paint will produce a repeating tile of the original noise image.
   */
  private void initNoisePaint() {
    try {
      InputStream inputStream = getContext().getAssets().open("noise.png");
      Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
      Shader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
      mNoisePaint.setShader(bitmapShader);
    } catch (IOException e) {
      // shut up lint
    }
  }

  /**
   * Any number of layers can be added, each with a starting Y position (offset) and an angle.
   * Each layer will be drawn to the right and bottom edges of the View.
   *
   * @param layer
   */
  public void addLayer(Layer layer) {
    mLayers.add(layer);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    //super.onDraw(canvas);
    canvas.drawPaint(mNoisePaint);
    for (Layer layer : mLayers) {
      layer.constructPath(canvas.getWidth(), canvas.getHeight());
      layer.computeGradients(mMeasuringRect);
      canvas.drawPath(layer.getPath(), mShadowPaint);
      layer.drawGradient(canvas);
      canvas.drawPath(layer.getPath(), mNoisePaint);
    }
    //super.onDraw(canvas);
  }

  /**
   * A Layer instance represents an angled plane that overlays this View.  Each Layer is filled with a gradient
   * from the topmost opaque pixel to the bottom most, has a layer of noise applied over that gradient, and a shadow
   * drawn as if light were from the bottom of the screen.
   */
  public static class Layer {

    private int mColorStart;
    private int mColorEnd;
    private int mOffset;
    private double mAngle;
    private Paint mPaint = new Paint();
    private Path mPath = new Path();

    {
      mPaint.setAntiAlias(true);
    }

    public int getColorStart() {
      return mColorStart;
    }

    public void setColorStart(int colorStart) {
      mColorStart = colorStart;
    }

    public int getColorEnd() {
      return mColorEnd;
    }

    public void setColorEnd(int colorEnd) {
      mColorEnd = colorEnd;
    }

    public int getOffset() {
      return mOffset;
    }

    public void setOffset(int offset) {
      mOffset = offset;
    }

    public double getAngle() {
      return mAngle;
    }

    public void setAngle(double angle) {
      mAngle = angle;
    }

    public Paint getPaint() {
      return mPaint;
    }

    public Path getPath() {
      return mPath;
    }

    private int getTermination(int width) {
      double radians = Math.toRadians(mAngle);
      double slope = Math.tan(radians);
      return (int) (mOffset + (slope * width));
    }

    public void constructPath(int width, int height) {
      int termination = getTermination(width);
      mPath.reset();
      mPath.moveTo(0, mOffset);
      mPath.lineTo(width, termination);
      mPath.lineTo(width, height);
      mPath.lineTo(0, height);
      mPath.lineTo(0, termination);
      mPath.close();
    }

    public void computeGradients(RectF measuringRect) {
      mPath.computeBounds(measuringRect, true);
      Shader shader = new LinearGradient(0, measuringRect.top, 0, measuringRect.bottom, mColorStart, mColorEnd, Shader.TileMode.CLAMP);
      mPaint.setShader(shader);
    }

    public void drawGradient(Canvas canvas) {
      canvas.drawPath(mPath, mPaint);
    }

  }

}
