package com.music.eartrainr.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.music.eartrainr.GameManager;
import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.activity.IntervalDetectionGameActivity;
import com.music.eartrainr.interfaces.ResultCodes;

import java.io.IOException;

public class GcmIntentService extends GcmListenerService {

    public interface notificationAction {
        String MATCH_RESPONSE = "match_response";
        int NOTIFICATION_ID = 0;
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Bundle bundle = data.getBundle("notification");
        String body = bundle.getString("body");
        String title = bundle.getString("title");
        int id = Integer.valueOf(data.getString(GameManager.GAMES.GAME_ID));

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent acceptIntent = new Intent(this, GcmNotificationReceiver.class);
        acceptIntent.setAction("accept");
        acceptIntent.putExtra(notificationAction.MATCH_RESPONSE, true);
        acceptIntent.putExtra(GameManager.GAMES.MESSAGE, "Waiting for game to start");
        acceptIntent.putExtra(GameManager.GAMES.GAME_ID, id);
        acceptIntent.putExtra(GameManager.GAMES.MULTIPLAYER, true);

        PendingIntent pAcceptIntent = PendingIntent.getBroadcast(this, ResultCodes.ACCEPT_GAME, acceptIntent, 0);

        Intent declineIntent = new Intent(this, GcmNotificationReceiver.class);
        declineIntent.setAction("decline");
        declineIntent.putExtra(notificationAction.MATCH_RESPONSE, false);
        PendingIntent pDeclineIntent = PendingIntent.getBroadcast(this, ResultCodes.DECLINE_GAME, declineIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_done_white_18dp)
                .setContentTitle(title)
                .setContentText(body)
                .addAction(R.drawable.ic_done_white_18dp, "Accept", pAcceptIntent)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Decline", pDeclineIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(0)
                .setDeleteIntent(pDeclineIntent);
        notificationManager.notify(notificationAction.NOTIFICATION_ID, mBuilder.build());
    }

    public String getRegistrationToken() {
        String token = "";
        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken(getApplicationContext().getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            Wtf.log();
        }
        return token;
    }

/*    public static Intent makeMultiplayerIntent(Context context) {
        final Intent intent = new Intent(context, GcmIntentService.class);
    }*/
}
