package mx.com.factico.cide;

import java.util.Arrays;
import java.util.List;

import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.utils.IntentUtils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

public class FacebookLoginActivity extends ActionBarActivity implements OnClickListener {
	public static final String TAG_CLASS = FacebookLoginActivity.class.getName();

	private final List<String> PERMISSIONS_TO_READ = Arrays.asList("public_profile");
	private final List<String> PERMISSIONS_TO_PUBLISH = Arrays.asList("publish_actions");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook);

		initUI();

		if (IntentUtils.appIsALreadyInstalled(this, getResources().getString(R.string.facebook_package)))
			initFacebookSession();
		else
			Dialogues.Toast(getApplicationContext(), getResources().getString(R.string.facebook_is_not_installed), Toast.LENGTH_LONG);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Logs 'app deactivate' App Event.
		AppEventsLogger.deactivateApp(this);
	}

	private void initUI() {
		Button btnPostMessage = (Button) findViewById(R.id.facebook_btn_post);
		btnPostMessage.setOnClickListener(this);
	}

	private void initFacebookSession() {
		Session session = new Session(this);
		Session.setActiveSession(session);
		Session.OpenRequest request = new Session.OpenRequest(this);
		request.setPermissions(PERMISSIONS_TO_READ);
		request.setCallback(SessionStatusCallback);
		session.openForRead(request);
	}

	private Session.StatusCallback SessionStatusCallback = new Session.StatusCallback() {

		// callback when session changes state
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if (session.isOpened()) {
				// make request to the /me API
				Request.newMeRequest(session, new Request.GraphUserCallback() {

					// callback after Graph API response with user object
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							TextView welcome = (TextView) findViewById(R.id.facebook_tv_status);
							welcome.setText("Hola " + user.getName() + "!");
							welcome.append("\nCon id: " + user.getId());

							Settings.sdkInitialize(getBaseContext());
						}
					}
				}).executeAsync();
			} else {
				TextView welcome = (TextView) findViewById(R.id.facebook_tv_status);
				welcome.setText(getResources().getString(R.string.facebook_session_is_not_open));
			}
		}
	};

	private void postStatusUpdate() {
		// Session session = Session.getActiveSession();

		Session session = new Session(this);
		Session.setActiveSession(session);
		Session.OpenRequest openRequest = new Session.OpenRequest(this);
		openRequest.setPermissions(PERMISSIONS_TO_PUBLISH);
		openRequest.setCallback(SessionStatusCallback);
		session.openForPublish(openRequest);

		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this).setCallback(SessionStatusCallback));
		} else {
			Session.openActiveSession(this, true, SessionStatusCallback);
			Log.d("myLogs", "Test 1");
			final String message = "Test, message to post";
			Request request = Request.newStatusUpdateRequest(Session.getActiveSession(), message, new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					showPublishResult(message, response.getGraphObject(), response.getError());
				}
			});
			request.executeAsync();
		}
	}

	private void showPublishResult(String message, GraphObject result, FacebookRequestError error) {
		String title = null;
		String alertMessage = null;
		if (error == null) {
			title = "Success";

			alertMessage = "All is good";
		} else {
			title = "Error";
			alertMessage = error.getErrorMessage();
		}

		new AlertDialog.Builder(this).setTitle(title).setMessage(alertMessage)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// finish();
						dialog.dismiss();
					}
				}).show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		Toast.makeText(getApplicationContext(), "onActivityResult", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.facebook_btn_post:
			postStatusUpdate();
			break;

		default:
			break;
		}
	}
}
