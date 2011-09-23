package uy.com.polnocetti.socialpoweramp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {
			
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.main);

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		String mensaje = String.format(getResources().getString(R.string.imlistening), ButtonWidget.mTitulo, ButtonWidget.mArtist);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mensaje);
		emailIntent.setType("text/plain");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		finish();
	}

}