package uy.com.polnocetti.socialpoweramp;

import uy.com.polnocetti.socialpoweramp.facebook.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainPop extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.container);
		
		ImageButton twitterButton = (ImageButton) findViewById(R.id.twitterButton);
		twitterButton.setOnClickListener(twitterButtonEvent);
		
		ImageButton facebookButton = (ImageButton) findViewById(R.id.facebookButton);
		facebookButton.setOnClickListener(facebookButtonEvent);
		
		ImageButton cogButton = (ImageButton) findViewById(R.id.cogButton);
		cogButton.setOnClickListener(cogButtonEvent);

	}

	private Button.OnClickListener twitterButtonEvent = new Button.OnClickListener() {

		public void onClick(View arg0) {
			startActivity(new Intent(getApplicationContext(),Main.class));
			finish();
		}

	};
	
	private Button.OnClickListener facebookButtonEvent = new Button.OnClickListener() {

		public void onClick(View arg0) {
			startActivity(new Intent(getApplicationContext(),MainActivity.class));
			finish();
		}

	};
	
	private Button.OnClickListener cogButtonEvent = new Button.OnClickListener() {

		public void onClick(View arg0) {
			startActivity(new Intent(getApplicationContext(),ButtonWidgetConfigure.class));
			finish();
		}

	};
	
}
