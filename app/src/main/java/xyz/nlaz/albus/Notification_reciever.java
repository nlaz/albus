package xyz.nlaz.albus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Nick on 11/25/2016.
 */
public class Notification_reciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent review_intent = new Intent(context, ReviewActivity.class);
        review_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,review_intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("Albus")
                .setContentText("Have you checked your memories today?")
                .setAutoCancel(true);
        notificationManager.notify(100, builder.build());
    }
}

