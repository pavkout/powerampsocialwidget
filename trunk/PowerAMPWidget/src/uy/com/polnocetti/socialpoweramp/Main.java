package uy.com.polnocetti.socialpoweramp;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.maxmpz.audioplayer.player.PowerAMPiAPI;

public class Main extends Activity {

	SharedPreferences prefs;
	public static final String TAG = "PowerAMP Social Widget................................................Log";
	private Bundle mCurrentTrack;
	private Intent mTrackIntent;
	public String mTitulo;
	public String mArtist;
	public String mAlbum;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ButtonWidget.doUpdate = true;
		boolean did = false;
		if (appInstalledOrNot("com.maxmpz.audioplayer")) {
			prefs = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			if (prefs.getString("appselected", "").equals("")) {
				startActivityForResult(new Intent(this,
						ButtonWidgetConfigure.class), 99);
			} else {
				register(this);
				did = true;
			}
		} else {
			Toast.makeText(this, R.string.powerAmpIsNotInstalled,
					Toast.LENGTH_LONG);
			did = true;
		}
		if (did)
			finish();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 99) {
			if (resultCode == RESULT_OK) {
				register(this);
			} else {
				Toast.makeText(this, R.string.appNotConfigured,
						Toast.LENGTH_LONG);
			}
		}
		finish();
	}

	private void register(Context context) {
		try {
			mTrackIntent = context.getApplicationContext().registerReceiver(
					mTrackReceiver,
					new IntentFilter(PowerAMPiAPI.ACTION_TRACK_CHANGED));
			startActivity(mTrackIntent);
		} catch (Exception e) {
			Log.e(TAG, e.getStackTrace().toString());
		}
	}

	private BroadcastReceiver mTrackReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				if (ButtonWidget.doUpdate) {
					ButtonWidget.doUpdate = false;
					mTrackIntent = intent;
					mCurrentTrack = null;
					if (mTrackIntent != null) {
						mCurrentTrack = mTrackIntent
								.getBundleExtra(PowerAMPiAPI.TRACK);
						if (mCurrentTrack != null) {
							mTitulo = mCurrentTrack
									.getString(PowerAMPiAPI.Track.TITLE);
							mArtist = mCurrentTrack
									.getString(PowerAMPiAPI.Track.ARTIST);
							mAlbum = mCurrentTrack
									.getString(PowerAMPiAPI.Track.ALBUM);
						}
					}
					prefs = PreferenceManager
							.getDefaultSharedPreferences(getBaseContext());
					String textoPatron = prefs.getString("pattern", "");

					Intent tweetlIntent = findTwitterClient();

					String mensaje = textoPatron.replace("<song>", mTitulo)
							.replace("<artist>", mArtist)
							.replace("<album>", mAlbum);
					tweetlIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							mensaje);
					startActivity(Intent.createChooser(tweetlIntent,
							"Share music via: "));
				}
			} catch (Exception e) {
				Log.e(TAG, e.getStackTrace().toString());
			}
		}
	};

	public Intent findTwitterClient() {

		Intent tweetIntent = new Intent(Intent.ACTION_SEND);
		tweetIntent.setType("text/plain");

		final PackageManager packageManager = getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(
				tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

		for (ResolveInfo resolveInfo : list) {
			String p = resolveInfo.activityInfo.packageName;
			String p1 = resolveInfo.activityInfo.name;
			if (p.contains(prefs.getString("appselected", ""))) {
				if (p != null && !p1.contains("Facebook")) {
					tweetIntent.setPackage(p);
					return tweetIntent;
				}
			}
		}
		return tweetIntent;
	}

	private boolean appInstalledOrNot(String uri) {
		PackageManager pm = getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}
}