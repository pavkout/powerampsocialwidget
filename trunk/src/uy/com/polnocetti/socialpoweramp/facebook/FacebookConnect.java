package uy.com.polnocetti.socialpoweramp.facebook;

import uy.com.polnocetti.socialpoweramp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.easy.facebook.android.apicall.GraphApi;
import com.easy.facebook.android.error.EasyFacebookError;
import com.easy.facebook.android.facebook.FBLoginManager;
import com.easy.facebook.android.facebook.Facebook;
import com.easy.facebook.android.facebook.LoginListener;

public class FacebookConnect extends Activity implements LoginListener {

	private FBLoginManager fbManager;
	private String cancion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			cancion = null;
		} else {
			cancion = extras.getString("CancionActiva");
		}

		if (cancion != null && !cancion.trim().equals("")) {
			shareFacebook();
		} else {
			finish();
		}
	}

	public void shareFacebook() {
		String permissions[] = { "publish_stream" };

		fbManager = new FBLoginManager(this, R.layout.black, "320366701321201", permissions);

		if (fbManager.existsSavedFacebook()) {
			fbManager.loadFacebook();
		} else {

			fbManager.login();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			fbManager.loginSuccess(data);
		} catch (Exception e) {
			fbManager.displayToast("Something went wrong.. Sorry");
			// Log.e(Main.TAG, e.getMessage());
		}
	}

	public void loginFail() {
		fbManager.displayToast("Login failed!");

	}

	public void logoutSuccess() {
		fbManager.displayToast("Logout success!");
	}

	public void loginSuccess(Facebook facebook) {

		try {
			GraphApi graphApi = new GraphApi(facebook);
			graphApi.setStatus(cancion);
			fbManager.displayToast("Status posted!");
		} catch (EasyFacebookError e) {
			fbManager.displayToast("Something went wrong.. Sorry");
			// Log.e(Main.TAG, e.getMessage());
		}

		finish();

	}

}