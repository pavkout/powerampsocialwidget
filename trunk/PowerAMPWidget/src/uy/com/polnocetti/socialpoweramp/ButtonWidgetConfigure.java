package uy.com.polnocetti.socialpoweramp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ButtonWidgetConfigure extends Activity {

	Button configOkButton, restoreButton, artistBtn, albumBtn, songBtn;
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
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String textoPatron = prefs.getString("pattern", "");

		if (textoPatron.trim().length() == 0) {// empty
			textoPatron = getResources().getString(R.string.imlistening).replace("song", "<song>").replace("artist", "<artist>");
		}

		EditText text = (EditText) findViewById(R.id.pattern);
		text.setText(textoPatron);

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

			if (!textoNuevo.contains("<song>") || !textoNuevo.contains("<artist>")) {				
				Toast me = Toast.makeText(getApplicationContext(),"<song> and <artist> tags are mandatory.", Toast.LENGTH_SHORT * 2);
				me.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0,0);
				me.show();
			} else {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				Editor textoPatron = prefs.edit();

				textoPatron.remove("pattern");
				textoPatron.putString("pattern", textoNuevo);

				textoPatron.commit();

				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
				setResult(RESULT_OK, resultValue);
				finish();
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
	
}