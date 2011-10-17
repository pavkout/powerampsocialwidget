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

	SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (appInstalledOrNot("com.maxmpz.audioplayer")) {

			prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			String textoPatron = prefs.getString("pattern", "");

			Intent tweetlIntent = findTwitterClient();

			String mensaje = textoPatron.replace("<song>", ButtonWidget.mTitulo).replace("<artist>", ButtonWidget.mArtist)
					.replace("<album>", ButtonWidget.mAlbum);
			tweetlIntent.putExtra(android.content.Intent.EXTRA_TEXT, mensaje);
			startActivity(Intent.createChooser(tweetlIntent, "Share music via: "));
		} else {
			Toast.makeText(this, R.string.powerAmpIsNotInstalled, Toast.LENGTH_LONG);
		}
		finish();
	}

	public Intent findTwitterClient() {

		Intent tweetIntent = new Intent(Intent.ACTION_SEND);
		tweetIntent.setType("text/plain");

		final PackageManager packageManager = getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

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