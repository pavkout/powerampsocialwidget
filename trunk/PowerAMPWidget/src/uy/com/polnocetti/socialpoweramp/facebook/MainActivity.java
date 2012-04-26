package uy.com.polnocetti.socialpoweramp.facebook;

import uy.com.polnocetti.socialpoweramp.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

public class MainActivity extends Activity {

	Intent intent;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		setContentView(R.layout.mainfb);

		intent = new Intent(getApplicationContext(), FacebookConnect.class);
		
		try {
			ConnectivityManager cMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
			String status = netInfo.getState().toString();

			if (status.equals("CONNECTED")) {
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "No connection available", Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Connection refused", Toast.LENGTH_SHORT).show();
		}	
		
		finish();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

}