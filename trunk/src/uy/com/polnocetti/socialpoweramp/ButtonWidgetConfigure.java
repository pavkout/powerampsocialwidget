package uy.com.polnocetti.socialpoweramp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ButtonWidgetConfigure extends Activity {

	Button configOkButton, restoreButton, artistBtn, albumBtn, songBtn, hashPA, hashnow, hashShuffling, hashplay,
			btnAndroid;
	Spinner spinnerApp;
	HashMap<String, String> map;
	String selectedApp;

	public static String[][] apps = { { "Official", "com.twitter.android" }, { "Twicca", "jp.r246.twicca" },
			{ "Ubersocial", "com.twidroid" }, { "Twidroyd Pro", "com.twidroidpro" },
			{ "Tweetcaster", "com.handmark.tweetcaster" }, { "Tweetcaster", "com.handmark.tweetcaster.premium" },
			{ "Tweetdeck", "com.thedeck.android.app" }, { "Seesmic", "com.seesmic" },
			{ "Plume", "com.levelup.touiteur" }, { "Plume", "com.levelup.touiteurpremium" },
			{ "Tweettopics", "com.javielinux.tweettopics.lite" }, { "Tweettopics", "com.javielinux.tweettopics.pro" },
			{ "HTC Peep", "com.htc.htctwitter" }, { "Tweetdark", "com.tweetdark.wjddesigns.free" },
			{ "Tweetdark Donate", "com.tweetdark.wjddesigns" } };

	private ArrayList<String> installedApps = new ArrayList<String>();
	private ArrayList<String> installedPack = new ArrayList<String>();

	private Drawable[] icons;

	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setResult(RESULT_CANCELED);
		getInstalledApps();

		if (appInstalledOrNot("com.maxmpz.audioplayer")) {

			setContentView(R.layout.configure);

			configOkButton = (Button) findViewById(R.id.okconfig);
			configOkButton.setOnClickListener(configOkButtonOnClickListener);

			restoreButton = (Button) findViewById(R.id.restore);
			restoreButton.setOnClickListener(configRestoreButton);

			artistBtn = (Button) findViewById(R.id.btnArtist);
			artistBtn.setOnClickListener(artistOnClickListener);

			albumBtn = (Button) findViewById(R.id.btnAlbum);
			albumBtn.setOnClickListener(albumOnClickListener);

			songBtn = (Button) findViewById(R.id.btnSong);
			songBtn.setOnClickListener(songOnClickListener);

			hashnow = (Button) findViewById(R.id.btnnowlistening);
			hashnow.setOnClickListener(nowOnClickListener);

			hashPA = (Button) findViewById(R.id.btnPoweramptag);
			hashPA.setOnClickListener(paOnClickListener);

			hashplay = (Button) findViewById(R.id.btnplaying);
			hashplay.setOnClickListener(playingOnClickListener);

			hashShuffling = (Button) findViewById(R.id.btnshuffling);
			hashShuffling.setOnClickListener(shufflingOnClickListener);

			btnAndroid = (Button) findViewById(R.id.btnAndroid);
			btnAndroid.setOnClickListener(androidOnClickListener);

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			String textoPatron = prefs.getString("pattern", "");
			String appSel = prefs.getString("appselected", "");
			boolean facebook = prefs.getBoolean("facebook", false);

			((CheckBox) findViewById(R.id.checkBox)).setChecked(facebook || installedApps.size() == 0);

			if (textoPatron.trim().length() == 0) {// empinstalledApps.size() ==
				// 0ty
				textoPatron = getResources().getString(R.string.imlistening).replace("song", "<song>")
						.replace("artist", "<artist>");
			}

			EditText text = (EditText) findViewById(R.id.pattern);
			text.setText(textoPatron);

			int selected = 0;

			String[] spinnerArray = new String[installedApps.size()];
			map = new HashMap<String, String>();

			for (String app : installedApps) {
				map.put(installedPack.get(installedApps.indexOf(app)), app);
				spinnerArray[installedApps.indexOf(app)] = installedPack.get(installedApps.indexOf(app));
				if (!appSel.trim().equals("") && app.equals(appSel)) {
					selected = installedApps.indexOf(app);
				}
			}

			spinnerApp = (Spinner) findViewById(R.id.spinner1);
			spinnerApp.setAdapter(new MyCustomAdapter(ButtonWidgetConfigure.this, R.layout.row, spinnerArray));
			spinnerApp.setOnItemSelectedListener(new MyOnItemSelectedListener());

			spinnerApp.setSelection(selected);
			spinnerApp.setClickable(installedApps.size() > 0);

			if (installedApps.size() == 0) {
				((CheckBox) findViewById(R.id.checkBox)).setClickable(false);
				spinnerApp.setVisibility(View.GONE);
			}

			Intent intent = getIntent();
			Bundle extras = intent.getExtras();
			if (extras != null) {
				mAppWidgetId = extras
						.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			}
		} else {
			Toast me = Toast.makeText(getApplicationContext(), "PowerAMP is not installed.", Toast.LENGTH_SHORT * 2);
			me.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
			me.show();
			finish();
		}

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	private Button.OnClickListener configOkButtonOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {

			EditText text = (EditText) findViewById(R.id.pattern);
			String textoNuevo = text.getText().toString();

			if (((selectedApp == null) || (selectedApp != null && selectedApp.trim().length() == 0))
					&& installedApps.size() > 0) {
				Toast me = Toast.makeText(getApplicationContext(), "No twitter client selected.",
						Toast.LENGTH_SHORT * 2);
				me.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
				me.show();
			} else {
				if (!textoNuevo.contains("<song>") || !textoNuevo.contains("<artist>")) {
					Toast me = Toast.makeText(getApplicationContext(), "<song> and <artist> tags are mandatory.",
							Toast.LENGTH_SHORT * 2);
					me.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
					me.show();
				} else {
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
					Editor textoPatron = prefs.edit();

					textoPatron.remove("pattern");
					textoPatron.putString("pattern", textoNuevo);

					textoPatron.remove("appselected");
					textoPatron.putString("appselected", selectedApp);

					textoPatron.remove("facebook");
					CheckBox cb = (CheckBox) findViewById(R.id.checkBox);
					textoPatron.putBoolean("facebook", cb.isChecked());

					textoPatron.commit();

					Intent resultValue = new Intent();
					resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
					setResult(RESULT_OK, resultValue);
					finish();
				}
			}
		}
	};

	private Button.OnClickListener configRestoreButton = new Button.OnClickListener() {

		public void onClick(View arg0) {
			String textoPatron = getResources().getString(R.string.imlistening).replace("song", "<song>")
					.replace("artist", "<artist>");

			EditText text = (EditText) findViewById(R.id.pattern);
			text.setText(textoPatron);
		}
	};

	private int at;

	private String FormatearTexto(TextView text, String textoNuevo) {
		at = text.getSelectionStart();
		String pre = text.getText().toString().substring(0, at);
		String post = text.getText().toString().substring(at, text.getText().toString().length());
		return pre + textoNuevo + post;
	}

	private Button.OnClickListener artistOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			EditText text = (EditText) findViewById(R.id.pattern);
			text.setText(FormatearTexto(text, " <artist>"));
			text.setSelection(at + (" <artist>").length());
		}
	};

	private Button.OnClickListener albumOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			EditText text = (EditText) findViewById(R.id.pattern);
			text.setText(FormatearTexto(text, " <album>"));
			text.setSelection(at + (" <album>").length());
		}
	};

	private Button.OnClickListener songOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			EditText text = (EditText) findViewById(R.id.pattern);
			text.setText(FormatearTexto(text, " <song>"));
			text.setSelection(at + (" <song>").length());
		}
	};

	private Button.OnClickListener nowOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			EditText text = (EditText) findViewById(R.id.pattern);
			text.setText(FormatearTexto(text, " #nowlistening"));
			text.setSelection(at + (" #nowlistening").length());
		}
	};

	private Button.OnClickListener paOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			EditText text = (EditText) findViewById(R.id.pattern);
			text.setText(FormatearTexto(text, " #PowerAMP"));
			text.setSelection(at + (" #PowerAMP").length());
		}
	};

	private Button.OnClickListener shufflingOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			EditText text = (EditText) findViewById(R.id.pattern);
			text.setText(FormatearTexto(text, " #shufflingto"));
			text.setSelection(at + (" #shufflingto").length());
		}
	};

	private Button.OnClickListener playingOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			EditText text = (EditText) findViewById(R.id.pattern);
			text.setText(FormatearTexto(text, " #nowplaying"));
			text.setSelection(at + (" #nowplaying").length());
		}
	};

	private Button.OnClickListener androidOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			EditText text = (EditText) findViewById(R.id.pattern);
			text.setText(FormatearTexto(text, " #Android"));
			text.setSelection(at + (" #Android").length());
		}
	};

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			selectedApp = map.get(parent.getItemAtPosition(pos).toString());
		}

		public void onNothingSelected(AdapterView<?> parent) {
			selectedApp = "";
		}
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

	void getInstalledApps() {

		for (int i = 0; i < apps.length; i++) {
			if (appInstalledOrNot(apps[i][1])) {
				installedApps.add(apps[i][1]);
				installedPack.add(apps[i][0]);
			}
		}

		icons = new Drawable[installedApps.size()];
		for (String app : installedApps) {
			List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
			for (int j = 0; j < packs.size(); j++) {
				PackageInfo p = packs.get(j);
				String pname = p.packageName;
				if (app.contains(pname)) {
					icons[installedApps.indexOf(app)] = p.applicationInfo.loadIcon(getPackageManager());
					break;
				}
			}
		}
	}

	public class MyCustomAdapter extends ArrayAdapter<String> {

		public MyCustomAdapter(Context context, int textViewResourceId, String[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			TextView label = (TextView) row.findViewById(R.id.appName);

			label.setText(installedPack.get(position));

			ImageView icon = (ImageView) row.findViewById(R.id.icon);
			icon.setImageDrawable(icons[position]);

			return row;
		}
	}

}