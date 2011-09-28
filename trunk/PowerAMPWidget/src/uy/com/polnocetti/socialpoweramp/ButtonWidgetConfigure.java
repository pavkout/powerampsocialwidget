package uy.com.polnocetti.socialpoweramp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ButtonWidgetConfigure extends Activity {

	Button configOkButton, restoreButton, artistBtn, albumBtn, songBtn, hashPA, hashnow;
	Spinner spinnerApp;
	HashMap<String, String> map;
	String selectedApp;

	private String[][] apps = {{"Official", "com.twitter.android"}, {"Twicca", "jp.r246.twicca"}, {"Übersocial", "com.twidroid"},
			{"Tweetcaster", "com.handmark.tweetcaster"}, {"Tweetdeck", "com.thedeck.android"}, {"Seesmic", "com.seesmic"},
			{"Plume", "com.thedeck.android"}};

	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private static final String TAG = "PowerAMP Social Widget...........................................................................Log";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setResult(RESULT_CANCELED);

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

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String textoPatron = prefs.getString("pattern", "");
		String appSel = prefs.getString("appselected", "");

		if (textoPatron.trim().length() == 0) {// empty
			textoPatron = getResources().getString(R.string.imlistening).replace("song", "<song>").replace("artist", "<artist>");
		}

		EditText text = (EditText) findViewById(R.id.pattern);
		text.setText(textoPatron);

		int selected = 0;
		int count = 0;

		ArrayList<String> spinnerArray = new ArrayList<String>();
		map = new HashMap<String, String>();

		for (int i = 0; i < apps.length; i++) {
			if (appInstalledOrNot(apps[i][1])) {
				Log.i(TAG, ((Integer) count).toString());
				map.put(apps[i][0], apps[i][1]);
				spinnerArray.add(apps[i][0]);
				if (!appSel.trim().equals("") && apps[i][1].equals(appSel)) {
					selected = count;
				}
				count++;
			}
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerApp = (Spinner) findViewById(R.id.spinner1);
		spinnerApp.setAdapter(adapter);
		spinnerApp.setOnItemSelectedListener(new MyOnItemSelectedListener());

		spinnerApp.setSelection(selected);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		/*
		 * //If they gave us an intent without the widget id, just bail. if
		 * (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) { //
		 * finish(); }
		 */
	}

	private Button.OnClickListener configOkButtonOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {

			TextView text = (TextView) findViewById(R.id.pattern);
			String textoNuevo = text.getText().toString();

			if (selectedApp.trim().length() == 0) {
				Toast me = Toast.makeText(getApplicationContext(), "No twitter client selected.", Toast.LENGTH_SHORT * 2);
				me.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
				me.show();
			} else {
				if (!textoNuevo.contains("<song>") || !textoNuevo.contains("<artist>")) {
					Toast me = Toast.makeText(getApplicationContext(), "<song> and <artist> tags are mandatory.", Toast.LENGTH_SHORT * 2);
					me.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
					me.show();
				} else {
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
					Editor textoPatron = prefs.edit();

					textoPatron.remove("pattern");
					textoPatron.putString("pattern", textoNuevo);

					textoPatron.remove("appselected");
					textoPatron.putString("appselected", selectedApp);

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
			String textoPatron = getResources().getString(R.string.imlistening).replace("song", "<song>").replace("artist", "<artist>");

			TextView text = (TextView) findViewById(R.id.pattern);
			text.setText(textoPatron);
		}
	};

	private Button.OnClickListener artistOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			TextView text = (TextView) findViewById(R.id.pattern);
			text.setText(text.getText().toString() + " <artist>");
		}
	};

	private Button.OnClickListener albumOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			TextView text = (TextView) findViewById(R.id.pattern);
			text.setText(text.getText().toString() + " <album>");
		}
	};

	private Button.OnClickListener songOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			TextView text = (TextView) findViewById(R.id.pattern);
			text.setText(text.getText().toString() + " <song>");
		}
	};
	
	private Button.OnClickListener nowOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			TextView text = (TextView) findViewById(R.id.pattern);
			text.setText(text.getText().toString() + " #nowlistening");
		}
	};
	
	private Button.OnClickListener paOnClickListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			TextView text = (TextView) findViewById(R.id.pattern);
			text.setText(text.getText().toString() + " #PowerAMP");
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

}