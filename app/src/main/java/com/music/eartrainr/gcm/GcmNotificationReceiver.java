package com.music.eartrainr.gcm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.music.eartrainr.GameManager;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.activity.IntervalDetectionGameActivity;

/**
 * Created by rapha on 3/26/2016.
 */
public class GcmNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean matchResponse = intent.getBooleanExtra(GcmIntentService.notificationAction.MATCH_RESPONSE, false);
        if (!matchResponse) {
            //TODO notify API that things are cancelled (Send GCM to other player to let them know)

            Wtf.log("The challenge was denied!");
        } else {
            //TODO notify API that things are approved (Send GCM to other player to let them know)
            context.startActivity(IntervalDetectionGameActivity.makeMultiplayerIntent(context, true, intent.getExtras()));
            Wtf.log("Challenge accepted!");
        }
        cancelNotification(context);
    }

    private void cancelNotification (Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(GcmIntentService.notificationAction.NOTIFICATION_ID);
        Intent closeDialogIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeDialogIntent);
    }
}
