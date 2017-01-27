package hpi.com.hpifitness.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Georgey on 26-01-2017.
 */


public class CustomBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //  Toast.makeText(context, "recvd..", Toast.LENGTH_SHORT).show();

        Intent service = new Intent(context, CustomAlarmService.class);

        context.startService(service);
    }
}