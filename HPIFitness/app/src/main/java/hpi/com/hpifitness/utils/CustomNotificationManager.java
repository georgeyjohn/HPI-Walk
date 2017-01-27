package hpi.com.hpifitness.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Georgey on 26-01-2017.
 */

public class CustomNotificationManager {
    public static void showNotification(Context context,
                                        Intent ActivityClassToOpenIntent,
                                        String tickerTitle,
                                        String contentTitle,
                                        String contentBody,
                                        int iconResourceId) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), ActivityClassToOpenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder;

        builder = new NotificationCompat.Builder(context)
                .setSmallIcon(iconResourceId)
                .setContentTitle(contentTitle)
                .setLights(Color.TRANSPARENT, 500, 500)
                .setContentText(contentBody)
                .setAutoCancel(true)
                .setColor(Color.TRANSPARENT)
                .setTicker(tickerTitle)
                .setVibrate(new long[]{0, 250, 200, 250, 150, 150, 75, 150, 75, 150})
                .setSound(alarmSound);


        builder.setContentIntent(pendingIntent);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
