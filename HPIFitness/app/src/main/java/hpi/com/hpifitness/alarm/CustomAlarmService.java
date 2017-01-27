package hpi.com.hpifitness.alarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import hpi.com.hpifitness.HomeActivity;
import hpi.com.hpifitness.R;
import hpi.com.hpifitness.persistance.PersistanceManager;
import hpi.com.hpifitness.utils.CustomNotificationManager;

/**
 * Created by Georgey on 26-01-2017.
 */

public class CustomAlarmService extends Service {

    private PersistanceManager persistanceManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.err.println("Alarm Service onStartCommand()...");


        persistanceManager = new PersistanceManager(getBaseContext());

        setNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    private void setNotification() {
        String tickerTitle, contentTitle, contentBody;

        tickerTitle = "The Human Project";

        contentTitle = "The Human Project";
        contentBody = "Lets go for a walk";


        Intent toOpenIntent = new Intent(getApplicationContext(), HomeActivity.class);

        CustomNotificationManager.showNotification(getApplicationContext(),
                toOpenIntent,
                tickerTitle,
                contentTitle,
                contentBody,
                R.drawable.walk);
    }
}
