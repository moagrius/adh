package adh.com.places;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

public abstract class BaseActivity extends Activity {

  public static final String INTENT_ERROR = "INTENT_ERROR";
  public static final String EXTRA_ERROR_MESSAGE = "EXTRA_ERROR_MESSAGE";

  private Snackbar mSnackBar;
  private String mLastErrorShown;

  @Override
  protected void onResume() {
    super.onResume();
    LocalBroadcastManager.getInstance(this).registerReceiver(mErrorReceiver, new IntentFilter(INTENT_ERROR));
  }

  @Override
  protected void onPause() {
    super.onPause();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(mErrorReceiver);
  }

  /**
   * Route errors that should be shown to the user through a common Snackbar.
   * @param view The view to which the Snackbar will find a CoordinatorLayout
   */
  protected void initializeErrorHandler(View view) {
    mSnackBar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
  }

  protected void showError(String message) {
    if (!mSnackBar.isShown() || !message.equals(mLastErrorShown)) {
      mSnackBar.setText(message).show();
      mLastErrorShown = message;
    }
  }

  private BroadcastReceiver mErrorReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String message = intent.getStringExtra(EXTRA_ERROR_MESSAGE);
      showError(message);
    }
  };

}
