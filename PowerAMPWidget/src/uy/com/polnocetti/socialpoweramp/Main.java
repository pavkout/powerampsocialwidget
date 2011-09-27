package uy.com.polnocetti.socialpoweramp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Main extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (appInstalledOrNot("com.maxmpz.audioplayer")) {
			Intent emailIntent = findTwitterClient();
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	        String textoPatron = prefs.getString("pattern", "");
			
			String mensaje = String.format(textoPatron, ButtonWidget.mTitulo, ButtonWidget.mArtist);
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mensaje);
			startActivity(Intent.createChooser(emailIntent, "Share music via: "));
		} else {
			Toast.makeText(this, R.string.powerAmpIsNotInstalled, Toast.LENGTH_LONG);
		}
		finish();
	}

	public Intent findTwitterClient() {
		final String[] twitterApps = {"com.twitter.android", "com.twidroid", "com.handmark.tweetcaster", "com.thedeck.android", "com.seesmic"};
		Intent tweetIntent = new Intent();
		tweetIntent.setType("text/plain");
		final PackageManager packageManager = getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

		for (int i = 0; i < twitterApps.length; i++) {
			for (ResolveInfo resolveInfo : list) {
				String p = resolveInfo.activityInfo.packageName;
				if (p != null && p.startsWith(twitterApps[i])) {
					tweetIntent.setPackage(p);
					return tweetIntent;
				}
			}
		}
		return null;
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