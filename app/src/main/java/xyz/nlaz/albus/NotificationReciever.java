package xyz.nlaz.albus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

/**
 * Created by Nick on 11/25/2016.
 */
public class NotificationReciever extends BroadcastReceiver {

    private String[] content = {
        "Here's a fresh stack to review!",
        "Have you reviewed your notes today? You can do it now!",
        "Here are 10 more notes to review!",
        "Time to learn! Here's some fresh notes to review."
    };

    @Override
    public void onReceive(Context context, Intent intent){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent review_intent = new Intent(context, ReviewActivity.class);
        review_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,review_intent,PendingIntent.FLAG_UPDATE_CURRENT);

        int random = new Random().nextInt(content.length);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_flash)
                .setContentTitle("Albus")
                .setContentText(content[random])
                .setAutoCancel(true);
        notificationManager.notify(100, builder.build());
    }
}

