package com.municipalidadavda.gcmessage;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.municipalidadavda.R;
import com.municipalidadavda.activity.global.Principal;


public class GCMIntentService extends IntentService {

    private static final int NOTIF_ALERTA_ID = 1;


    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    protected void onHandleIntent(Intent intent)
    {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                mostrarNotification("Error de envío" ,extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                mostrarNotification("Mensaje eliminado del servidor",extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                mostrarNotification(extras.getString("title"), extras.getString("message")); //When Message is received normally from GCM Cloud Server
            }
        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void mostrarNotification(String titulo, String mensaje)
    {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logomunipeq)
                        .setContentTitle(titulo)
                        .setContentText(mensaje);

        Intent intent =  new Intent(this, Principal.class);
        PendingIntent contIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        mBuilder.setContentIntent(contIntent);
        mBuilder.setAutoCancel(true);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mBuilder.setDefaults(defaults);
        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
    }

}
