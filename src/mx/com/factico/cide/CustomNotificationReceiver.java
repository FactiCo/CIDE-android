package mx.com.factico.cide;

import mx.com.factico.cide.dialogues.Dialogues;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
{
"action":"mx.com.factico.cide.CUSTOM_NOTIFICATION",
"title": "My notification title Factico",
"alert": "My notification message Factico",
"data": {"autor":{"mail":"carlos@factico.com.mx","nombre":"Carlos","id_FB":987654321},"categoria":"Justicia","descripcion":"Lorem Ipsum","titulo":"Titulo de la propuesta","id":1234567890}
}
**/
public class CustomNotificationReceiver extends BroadcastReceiver {
	private final static String TAG_CLASS = CustomNotificationReceiver.class.getName();
	
	private NotificationCompat.Builder mBuilder;
	private Intent resultIntent;
	private int mNotificationId = 001;
	private Uri notifySound;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		// Getting JSON data and put them into variables
		try {
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
			
			String title = json.getString("title").toString(); // Getting custom title to show in custom notification
			String message = json.getString("alert").toString();  // Getting custom message to show in custom notification
			String data = json.getString("data").toString();  // Getting custom data to send to Activity
			
			Dialogues.Log(TAG_CLASS, "Title: " + title + ", Message" + message, Log.INFO);
			
			generateNotification(context, title, message, data);
		} catch (JSONException e) {}
	}
	
	protected void generateNotification(Context context, String title, String message, String data) {
		// Custom sound to show in custom notification
		notifySound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setSmallIcon(R.drawable.ic_launcher); // Setting custom icon to show in custom notification
		mBuilder.setContentTitle(title); // Setting custom title to show in custom notification
		mBuilder.setContentText(message); // Setting custom message to show in custom notification
		mBuilder.setSound(notifySound); // Setting custom sound to show in custom notification
		mBuilder.setAutoCancel(true);
		
		// Setting custom activity to show in custom notification
		resultIntent = new Intent(context, PropuestaActivity.class);
		resultIntent.putExtra("data", data);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(mNotificationId, mBuilder.build());
	}
}
