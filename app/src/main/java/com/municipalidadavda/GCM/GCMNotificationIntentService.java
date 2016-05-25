package com.municipalidadavda.GCM;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.municipalidadavda.JSONParser;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.RequestParams;
import com.municipalidadavda.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GCMNotificationIntentService extends IntentService {
	// Sets an ID for the notification, so it can be updated
	public static final int notifyID = 9001;
	GoogleCloudMessaging gcmObj;
	String regId = "", imeiId;
	Context applicationContext;
	RequestParams params = new RequestParams();

	public static final String REG_ID = "regId";
	public static final String EMAIL_ID = "eMailId";
	public static final String IMEI_ID = "imeiId";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000; //ver que pasa con esto
	TelephonyManager manager;
	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCMNotificationIntentService";


	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				sendNotification("" + extras.get(ApplicationConstants.MSG_KEY)); //When Message is received normally from GCM Cloud Server
			}
		}

		//Obtengo el IMEI
		manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		imeiId = manager.getDeviceId();

		//Obtengo email de preferencias
		SharedPreferences pref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String EMAIL_ID = pref.getString("emailID", "por_defecto@email.com");

		GcmBroadcastReceiver.completeWakefulIntent(intent);
		registerInBackground(EMAIL_ID);
	}


		// AsyncTask to register Device in GCM Server
		public void registerInBackground(final String emailID) {
			new AsyncTask<Void, Void, String>() {
				@Override
				protected String doInBackground(Void... params) {
					String msg = "";
					try {
						if (gcmObj == null) {
							gcmObj = GoogleCloudMessaging.getInstance(applicationContext);
						}
						regId = gcmObj.register(ApplicationConstants.GOOGLE_PROJ_ID);
						msg = "Registration ID :" + regId;

					} catch (IOException ex) {
						msg = "Error :" + ex.getMessage();
					}
					return msg;
				}

				@Override
				protected void onPostExecute(String msg) {
					if (!regId.isEmpty()) {
						storeRegIdinSharedPref(applicationContext, regId, emailID, imeiId);

					} else {

						Log.i("GCMNotificationIntentService", "Reg ID Creación Fallo.\n\nO bien no se ha habilitado Internet o " +
								"servidor de GCM es ocupado ahora. Asegúrese de Internet habilitado y registrate de nuevo después de un tiempo.");
					}
				}
			}.execute(null, null, null);
		}

		// Store RegId and Email entered by User in SharedPref
		private void storeRegIdinSharedPref(Context context, String regId,
				String emailID, String imei) {
			SharedPreferences prefs = getSharedPreferences("UserDetails",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(REG_ID, regId);
			editor.putString(EMAIL_ID, emailID);
			editor.putString(IMEI_ID,imei);
			editor.commit();
			storeRegIdinServer(regId, emailID, imeiId);

		}

	// Share RegID and Email ID with GCM Server Application (Php)
	private void storeRegIdinServer(String regId2, String emailID, String imeiId) {
		//prgDialog.show();
		params.put("emailId", emailID);
		params.put("regId", regId);
		params.put("imei",imeiId);
		System.out.println("Email id = " + emailID + " Reg Id = " + regId + "Imei Usuario:" + imeiId);

		// Make RESTful webservice call using AsyncHttpClient object
		/*AsyncHttpClient client = new AsyncHttpClient();
		client.post(ApplicationConstants.APP_SERVER_URL, params,
				new AsyncHttpResponseHandler() {
					// When the response returned by REST has Http
					// response code '200'
					@Override
					public void onSuccess(String response) {

						Log.i("GCMNotificationIntentService", "EL ESTADO HTTP ESTÁ OK");
					}

					// When the response returned by REST has Http
					// response code other than '200' such as '404',
					// '500' or '403' etc
					@Override
					public void onFailure(int statusCode, Throwable error,
										  String content) {

						// When Http response code is '404'
						if (statusCode == 404) {
						Log.e("GCMNotificationIntentService", "Requested resource not found");
						}

						// When Http response code is '500'
						else if (statusCode == 500) {
						Log.e("GCMNotificationIntentService", "Something went wrong at server end");
						}

						// When Http response code other than 404, 500
						else {
						Log.e("GCMNotificationIntentService", "Unexpected Error occcured! [Most common Error: Device might "
								+ "not be connected to Internet or remote server is not up and running], check for other errors as well");
						}
					}
				});*/
	}

	private void sendNotification(String greetMsg) {
	        Intent resultIntent = new Intent(this, GreetingActivity.class);
	        resultIntent.putExtra("greetjson", greetMsg);
	        resultIntent.setAction(Intent.ACTION_MAIN);
	        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
					resultIntent, PendingIntent.FLAG_ONE_SHOT);
	        
	        NotificationCompat.Builder mNotifyBuilder;
	        NotificationManager mNotificationManager;
	        
	        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	        
	        mNotifyBuilder = new NotificationCompat.Builder(this)
	                .setContentTitle("Municipalidad de Avellaneda")
	                .setContentText("Nuevas promociones on-line.")
	                .setSmallIcon(R.drawable.logomunipeq);
	        // Set pending intent
	        mNotifyBuilder.setContentIntent(resultPendingIntent);
	        
	        // Set Vibrate, Sound and Light	        
	        int defaults = 0;
	        defaults = defaults | Notification.DEFAULT_LIGHTS;
	        defaults = defaults | Notification.DEFAULT_VIBRATE;
	        defaults = defaults | Notification.DEFAULT_SOUND;

	        
	        mNotifyBuilder.setDefaults(defaults);
	        // Set the content for Notification
			JSONParser jsp = new JSONParser();


		try {
			JSONObject obj = new JSONObject(greetMsg);

			mNotifyBuilder.setContentText(obj.getString("greetMsg"));
	        // Set autocancel
	        mNotifyBuilder.setAutoCancel(true);
	        // Post a notification
	        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
