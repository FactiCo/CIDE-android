package mx.com.factico.cide;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyGcmBroadcastReceiver extends BroadcastReceiver {

	private int NOTIFICATION_ID = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			String action = intent.getAction();
			
			Bundle bundle = intent.getExtras();
			if(bundle != null) {
				String channel = bundle.getString("com.parse.Channel");
				
				String data = bundle.getString("com.parse.Data");
				
				if(data != null) {
					JSONObject json = new JSONObject(data);
					Log.i("Got push", "got action " + action + " on channel " + channel + " with:");
	
					if (action.equalsIgnoreCase("mx.com.factico.cide")) {
						String title = "title";
						String message = "";
						if (json.has("header"))
							title = json.getString("header");
						if (json.has("msg"))
							message = json.getString("msg");
						generateNotification(context, title, message, json);
					}
				}
			}
		} catch (JSONException e) {
			Log.d("TAG", "JSONException: " + e.getMessage());
		}
	}

	private void generateNotification(Context context, String title, String message, JSONObject json) {
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
		
		NotificationManager mNotifM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title).setContentText(message).setStyle(new NotificationCompat.BigTextStyle().bigText(message))
				.addAction(0, "Press to open", contentIntent).setAutoCancel(true).setDefaults(new NotificationCompat().DEFAULT_ALL);
		// .setNumber(++numMessages);

		mBuilder.setContentIntent(contentIntent);

		mNotifM.notify(NOTIFICATION_ID , mBuilder.build());
	}
}
