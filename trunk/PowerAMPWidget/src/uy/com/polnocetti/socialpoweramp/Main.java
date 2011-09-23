package uy.com.polnocetti.socialpoweramp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Main extends Activity {
	
	private static final String TAG = "PowerAMP Social Widget...........................................................................Log";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "actividad");
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.main);

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, ButtonWidget.mTitulo);
		emailIntent.setType("text/plain");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		finish();
	}

}