package uy.com.polnocetti.socialpoweramp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ButtonWidgetConfigure extends Activity {

	Button configOkButton;
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setResult(RESULT_CANCELED);

		setContentView(R.layout.config);

		configOkButton = (Button) findViewById(R.id.okconfig);
		configOkButton.setOnClickListener(configOkButtonOnClickListener);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String textoPatron = prefs.getString("pattern", "");
        
        if (textoPatron.trim().length() == 0){//empty
        	textoPatron = getResources().getString(R.string.imlistening);
        }
        
        TextView text = (TextView) findViewById(R.id.pattern);
        text.setText(textoPatron);
        
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// If they gave us an intent without the widget id, just bail.
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			//finish();
		}
	}

	private Button.OnClickListener configOkButtonOnClickListener = new Button.OnClickListener() {
		
		public void onClick(View arg0) {
			      	        
	        TextView text = (TextView) findViewById(R.id.pattern);
	        String textoNuevo = text.getText().toString();
	        
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
	};

}