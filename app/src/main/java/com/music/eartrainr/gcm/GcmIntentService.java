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
import com.music.eartrainr.Bus;
import com.music.eartrainr.GameManager;
import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.event.MultiPlayerEvent;
import com.music.eartrainr.interfaces.ResultCodes;

import java.io.IOException;

import static com.music.eartrainr.gcm.GcmIntentService.ACTION.*;


public class GcmIntentService extends GcmListenerService {

    public interface ACTION {
        String MATCH_RESPONSE = "match_response";
        String ACTION = "action";
        int ACTION_CHALLENGE = 0;
        int ACTION_CHALLENGE_ACCEPTED = 1;
        int ACTION_CHALLENGE_DECLINED = 2;
        int ACTION_CHALLENGE_CANCELLED = 3;
        int NOTIFICATION_ID = 0;
        String START_TIME = "startTime";
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {

        final int action = Integer.valueOf(data.getString(ACTION));

        switch (action) {
            case ACTION_CHALLENGE:
                showChallengeNotification(data);
                break;
            case ACTION_CHALLENGE_ACCEPTED:
                prepareGame(data);
                break;
            case ACTION_CHALLENGE_DECLINED:
                showStandardNotification(data);
                break;
            case ACTION_CHALLENGE_CANCELLED:
                showStandardNotification(data);
                cancelGame();
                break;
        }
    }
    private void prepareGame(final Bundle data) {
        String id = data.getString(GameManager.GAMES.GAME_ID);
        String startTime = data.getString(START_TIME);
        Bus.post(new MultiPlayerEvent().prepareStart(startTime));
    }
    private void cancelGame() {
        Bus.post(new MultiPlayerEvent().cancelGame());
    }

    private void showChallengeNotification(final Bundle data) {
        Bundle bundle = data.getBundle("notification");
        String body = bundle.getString("body");
        String title = bundle.getString("title");
        String id = data.getString(GameManager.GAMES.GAME_ID);

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent acceptIntent = new Intent(this, GcmNotificationReceiver.class);
        acceptIntent.setAction("accept");
        acceptIntent.putExtra(MATCH_RESPONSE, true);
        acceptIntent.putExtra(GameManager.GAMES.MESSAGE, "Waiting for game to start");
        acceptIntent.putExtra(GameManager.GAMES.GAME_ID, id);
        acceptIntent.putExtra(GameManager.GAMES.MULTIPLAYER, true);

        PendingIntent pAcceptIntent = PendingIntent.getBroadcast(this, ResultCodes.ACCEPT_GAME, acceptIntent, 0);

        Intent declineIntent = new Intent(this, GcmNotificationReceiver.class);
        declineIntent.setAction("decline");
        declineIntent.putExtra(GameManager.GAMES.GAME_ID, id);
        declineIntent.putExtra(MATCH_RESPONSE, false);
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
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void showStandardNotification(final Bundle data) {
        Bundle bundle = data.getBundle("notification");
        String title = bundle.getString("title");
        String body = bundle.getString("body");

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_done_white_18dp)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(Notification.PRIORITY_MAX)
            .setWhen(0);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
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
