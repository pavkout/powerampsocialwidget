package uy.com.polnocetti.socialpoweramp;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.maxmpz.audioplayer.player.PowerAMPiAPI;

public class ButtonWidget extends AppWidgetProvider {

	private static final String TAG = "PowerAMP Social Widget...........................................................................Log";
	public static String ACTION_WIDGET_RECEIVER = "PowerAMPIntentReceiver";
	private Bundle mCurrentTrack;
	private Intent mTrackIntent;
	static String mTitulo;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);

		Intent active = new Intent(context, ButtonWidget.class);
		active.setAction(ACTION_WIDGET_RECEIVER);
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);

		remoteViews.setOnClickPendingIntent(R.id.button_one, actionPendingIntent);
		registerAndLoadStatus(context);

		PendingIntent songPendingIntent = PendingIntent.getBroadcast(context, 0, mTrackIntent, 0);

		try {
			songPendingIntent.send();
		} catch (CanceledException e) {

			e.printStackTrace();
		}

		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[]{appWidgetId});
			}
		} else {
			// check, if our Action was called
			if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
				if (ButtonWidget.mTitulo != null)
					Toast.makeText(context, ButtonWidget.mTitulo, Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(context, R.string.powerAmpIsNotRunning, Toast.LENGTH_SHORT).show();
			} else {
				// do nothing
			}
			super.onReceive(context, intent);
		}
	}
	private BroadcastReceiver mTrackReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			mTrackIntent = intent;
			mCurrentTrack = null;
			if (mTrackIntent != null) {
				mCurrentTrack = mTrackIntent.getBundleExtra(PowerAMPiAPI.TRACK);
				if (mCurrentTrack != null) {
					ButtonWidget.mTitulo = mCurrentTrack.getString(PowerAMPiAPI.Track.TITLE);
				}
			}
		}
	};

	private void registerAndLoadStatus(Context context) {
		mTrackIntent = context.getApplicationContext().registerReceiver(mTrackReceiver, new IntentFilter(PowerAMPiAPI.ACTION_TRACK_CHANGED));
	}
}