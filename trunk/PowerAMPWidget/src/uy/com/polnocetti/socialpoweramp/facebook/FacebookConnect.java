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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		shareFacebook();
	}

	public void shareFacebook() {
		String permissions[] = {"publish_stream"};

		fbManager = new FBLoginManager(this, R.layout.black, "320366701321201", permissions);

		if (fbManager.existsSavedFacebook()) {
			fbManager.loadFacebook();
		} else {

			fbManager.login();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		fbManager.loginSuccess(data);
	}

	public void loginFail() {
		fbManager.displayToast("Login failed!");

	}

	public void logoutSuccess() {
		fbManager.displayToast("Logout success!");
	}

	public void loginSuccess(Facebook facebook) {

		GraphApi graphApi = new GraphApi(facebook);

		try {
			graphApi.setStatus("Tsting", "https://lh5.ggpht.com/A4JydoUo3x0cC6_KVbj3Dy7XyJZL9b_QEZq8P6JWHmqUh4mkiNfRsu-XjaFWQ03_xQ=w124");
		} catch (EasyFacebookError e) {
			e.toString();
		}
		
		finish();
		
	}

}