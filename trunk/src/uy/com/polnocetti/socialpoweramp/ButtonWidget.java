package uy.com.polnocetti.socialpoweramp;

import uy.com.polnocetti.socialpoweramp.facebook.MainActivity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class ButtonWidget extends AppWidgetProvider {

	public static final String TAG = "PowerAMP Social Widget................................................Log";
	public static String ACTION_WIDGET_RECEIVER = "PowerAMPIntentReceiver";
	public static String ACTION_WIDGET_ABOUT = "PowerAMPIntentReceiver_About";
	public static boolean doUpdate = false;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		try {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);

			Intent active = new Intent(context, ButtonWidget.class);
			active.setAction(ACTION_WIDGET_RECEIVER);
			PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);

			remoteViews.setOnClickPendingIntent(R.id.button_one, actionPendingIntent);

			Intent aactive = new Intent(context, ButtonWidget.class);
			aactive.setAction(ACTION_WIDGET_ABOUT);
			PendingIntent aactionPendingIntent = PendingIntent.getBroadcast(context, 0, aactive, 0);

			remoteViews.setOnClickPendingIntent(R.id.configbutton, aactionPendingIntent);

			appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		} catch (Exception e) {
			Log.e(TAG, "Ex: " + e.getMessage());
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			final String action = intent.getAction();
			if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
				final int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
						AppWidgetManager.INVALID_APPWIDGET_ID);
				if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
					this.onDeleted(context, new int[] { appWidgetId });
				}
			} else {
				if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
					doUpdate = true;

					Intent aintent = null;

					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
					boolean facebook = prefs.getBoolean("facebook", false);

					if (facebook)
						aintent = new Intent(context, MainActivity.class);
					else
						aintent = new Intent(context, Main.class);

					PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, aintent, 0);
					try {
						pendingIntent.send();
					} catch (CanceledException e) {
						Log.e(TAG, "Ex: " + e.getMessage());
					}
				} else if (intent.getAction().equals(ACTION_WIDGET_ABOUT)) {
					Intent aintent = new Intent(context, ButtonWidgetConfigure.class);
					PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, aintent, 0);
					try {
						pendingIntent.send();
					} catch (CanceledException e) {
						Log.e(TAG, "Ex: " + e.getMessage());
					}
				}
				super.onReceive(context, intent);
			}
		} catch (Exception e) {
			Log.e(TAG, "Ex: " + e.getMessage());
		}
	}
}