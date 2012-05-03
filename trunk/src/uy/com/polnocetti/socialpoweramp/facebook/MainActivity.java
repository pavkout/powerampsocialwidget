package uy.com.polnocetti.socialpoweramp.facebook;

import uy.com.polnocetti.socialpoweramp.ButtonWidget;
import uy.com.polnocetti.socialpoweramp.ButtonWidgetConfigure;
import uy.com.polnocetti.socialpoweramp.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.maxmpz.audioplayer.player.PowerAMPiAPI;

public class MainActivity extends Activity {

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
			prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			if (prefs.getString("pattern", "").trim().equals("")) {
				startActivityForResult(new Intent(this, ButtonWidgetConfigure.class), 99);
			} else {
				register(this);
				did = true;
			}
		} else {
			Toast.makeText(this, R.string.powerAmpIsNotInstalled, Toast.LENGTH_LONG);
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
				Toast.makeText(this, R.string.appNotConfigured, Toast.LENGTH_LONG);
			}
		}
		finish();
	}

	private void register(Context context) {
		try {
			mTrackIntent = context.getApplicationContext().registerReceiver(mTrackReceiver,
					new IntentFilter(PowerAMPiAPI.ACTION_TRACK_CHANGED));
			startActivity(mTrackIntent);
		} catch (Exception e) {
			Log.e(TAG, "Ex: " + e.getMessage());
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
						mCurrentTrack = mTrackIntent.getBundleExtra(PowerAMPiAPI.TRACK);
						if (mCurrentTrack != null) {
							mTitulo = mCurrentTrack.getString(PowerAMPiAPI.Track.TITLE);
							mArtist = mCurrentTrack.getString(PowerAMPiAPI.Track.ARTIST);
							mAlbum = mCurrentTrack.getString(PowerAMPiAPI.Track.ALBUM);
						}
					}
					prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
					String textoPatron = prefs.getString("pattern", "");
					
					String mensaje = textoPatron.replace("<song>", mTitulo).replace("<artist>", mArtist)
							.replace("<album>", mAlbum);
					doFb(mensaje,prefs.getBoolean("albumArt", false));
				}
			} catch (Exception e) {
				Log.e(TAG, "Ex: " + e.getMessage());
			}
		}
	};

	private void doFb(String mensaje, boolean albumArt) {

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		try {
			ConnectivityManager cMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
			String status = netInfo.getState().toString();

			if (status.equals("CONNECTED")) {
				Intent fbIntent = new Intent(getApplicationContext(), FacebookConnect.class);
				fbIntent.putExtra("CancionActiva", mensaje);
				fbIntent.putExtra("CancionActiva_Artist", mArtist);
				fbIntent.putExtra("CancionActiva_Album", mAlbum);
				fbIntent.putExtra("CancionActiva_AlbumArt", albumArt);
				
				startActivity(fbIntent);
			} else {
				Toast.makeText(getApplicationContext(), "No connection available", Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			Log.e(TAG, "Ex: " + e.getMessage());
			Toast.makeText(getApplicationContext(), "Connection refused", Toast.LENGTH_SHORT).show();
		}

		finish();
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