package hpi.com.hpifitness.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Georgey on 26-01-2017.
 */
public class CustomAlarmManager {

    private Context context;
    private AlarmManager alarmManager;

    public CustomAlarmManager(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setRepeatingAlarm() {

        Intent intent = new Intent(context, CustomBroadcastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar firstTriggerCalendar = Calendar.getInstance();
        firstTriggerCalendar.clear();
        firstTriggerCalendar.setTimeInMillis(System.currentTimeMillis());
        // firstTriggerCalendar.add(Calendar.HOUR, 1);

        alarmManager.cancel(pendingIntent);
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                firstTriggerCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HOUR,                // 5000, // for testing
                pendingIntent);

        System.err.println("repeating alarm set with id " + 1
                + " @ " +
                firstTriggerCalendar.getTime());
    }


    public void deleteAlarm(int alarmId) {
        Intent intent = new Intent(context, CustomBroadcastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);

        alarmManager.cancel(pendingIntent);

        System.err.println("alarm canceled with id " + alarmId);

    }

}
