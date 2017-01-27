package hpi.com.hpifitness;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.google.android.gms.location.LocationRequest;
import com.tarek360.instacapture.InstaCapture;
import com.tarek360.instacapture.listener.ScreenCaptureListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hpi.com.hpifitness.adaptor.StaticsAdaptor;
import hpi.com.hpifitness.alarm.CustomAlarmManager;
import hpi.com.hpifitness.entity.User;
import hpi.com.hpifitness.entity.Walk;
import hpi.com.hpifitness.persistance.Keys;
import hpi.com.hpifitness.persistance.PersistanceManager;
import hpi.com.hpifitness.utils.CustomeAsyncTask;
import hpi.com.hpifitness.utils.JSONHelper;

import static android.content.ContentValues.TAG;

/**
 * We are using Share preference to store data, Saving User data as a collection in JSON Format - To serialization and deserialization data we are using GSON Parser.
 * For Calculating distance we used GPS(Plugin - easylocation)
 * To share screen shot (plugin - instacapture) and we save locally the image and share to other media same as share option in android
 * We use Alarm Manager and Local push Notification for Alerting user between one hour.
 * For Alerting Milestone we use AlertDialog.
 * **/

public class HomeActivity extends EasyLocationAppCompatActivity {
    private Switch mswOffice;
    private CustomAlarmManager mcustomAlarmManager;
    private PersistanceManager persistanceManager;
    private ImageButton mbtnLogout, mbtnShare;
    private TextView mtvDisplayName, mtvTodayStatus;
    private ListView mLVWalk;
    private Button mbtnStartWalk;
    private Location tempLocation = null;
    private float distance = 0;
    private User mUser;
    private ArrayList<Walk> mArrayListWalk;
    private Dialog mAlertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initialize();
        persistanceManager = new PersistanceManager(HomeActivity.this);
        mswOffice.setChecked(persistanceManager.getBoolean(Keys._office));
        mcustomAlarmManager = new CustomAlarmManager(HomeActivity.this);
        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.HOUR_OF_DAY);
        cal.clear(Calendar.AM_PM);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        Calendar comCal = Calendar.getInstance();
        comCal.clear(Calendar.HOUR_OF_DAY);
        comCal.clear(Calendar.AM_PM);
        comCal.clear(Calendar.MINUTE);
        comCal.clear(Calendar.SECOND);
        comCal.clear(Calendar.MILLISECOND);
        mUser = new User();
        mUser = (User) persistanceManager.getValue(persistanceManager.getValue(Keys._currentuser), User.class);
        mArrayListWalk = new ArrayList<>();
        if (persistanceManager.getBoolean(Keys._walkstatus)) {
            mbtnStartWalk.setText("Stop Walking");
        } else {
            mbtnStartWalk.setText("Start Walking");
        }

        if (persistanceManager.getValue(Keys._distancecovered) == null) {
            persistanceManager.setValue(Keys._distancecovered, "0.0");
        } else {
            distance = Float.valueOf(persistanceManager.getValue(Keys._distancecovered));
        }

        if (persistanceManager.getInteger(Keys._milestone) == 0) {
            persistanceManager.setInteger(Keys._milestone, 0);
        }

        if (persistanceManager.getValue(Keys._time) == null) {
            persistanceManager.setValue(Keys._time, cal.getTime().toString());
        } else {
            Date date = new Date(persistanceManager.getValue(Keys._time));
            comCal.setTime(date);
            if (comCal.compareTo(cal) != 0) {
                saveDate();
            }
        }

        if (mUser.getWalk() != null) {
            mArrayListWalk.addAll(mUser.getWalk());
            for (Walk walk : mArrayListWalk) {
                Date date = new Date(walk.getDate());
                comCal.setTime(date);
                if (comCal.compareTo(cal) == 0) {
                    persistanceManager.setValue(Keys._distancecovered, walk.getDistance());
                    persistanceManager.setInteger(Keys._milestone, Integer.parseInt(walk.getMilestone()));
                    persistanceManager.setValue(Keys._time, walk.getDate());
                }
            }
        }
        loadData();

        mtvDisplayName.setText("Welcome " + mUser.getFullname());
        mtvTodayStatus.setText(" Today \n Distance :  " + String.format("%.2f", Float.valueOf(persistanceManager.getValue(Keys._distancecovered)) * 3.28) + "              Milestone :  " + persistanceManager.getInteger(Keys._milestone));

        mswOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mswOffice.isChecked()) {
                    mcustomAlarmManager.setRepeatingAlarm();
                    persistanceManager.setBoolean(Keys._office, true);
                    mbtnStartWalk.setText("Start Walking");
                    stopLocationUpdates();
                    persistanceManager.setBoolean(Keys._walkstatus, false);
                } else {
                    persistanceManager.setBoolean(Keys._office, false);
                    mcustomAlarmManager.deleteAlarm(1);
                }
            }
        });

        mbtnStartWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationService();
            }
        });


        mbtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot();
            }
        });

        mbtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDate();
                persistanceManager.setValue(Keys._distancecovered, "0.0");
                persistanceManager.setInteger(Keys._milestone, 0);
                persistanceManager.setValue(Keys._time, null);
                persistanceManager.setValue(Keys._currentuser, null);
                persistanceManager.setBoolean(Keys._walkstatus, false);
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void initialize() {
        mswOffice = (Switch) findViewById(R.id.sw_office);
        mbtnShare = (ImageButton) findViewById(R.id.btn_share);
        mbtnLogout = (ImageButton) findViewById(R.id.btn_logout);
        mbtnStartWalk = (Button) findViewById(R.id.btn_walk);
        mtvDisplayName = (TextView) findViewById(R.id.tv_displayname);
        mtvTodayStatus = (TextView) findViewById(R.id.tv_todaystatus);
        mLVWalk = (ListView) findViewById(R.id.lv_walk);
    }

    // To save user details like date, distance walked, milestone etc
    private void saveDate() {
        new CustomeAsyncTask(HomeActivity.this) {
            @Override
            public void doInBackground() {
                Calendar cal = Calendar.getInstance();
                cal.clear(Calendar.HOUR_OF_DAY);
                cal.clear(Calendar.AM_PM);
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);
                Calendar comCal = Calendar.getInstance();
                comCal.clear(Calendar.HOUR_OF_DAY);
                comCal.clear(Calendar.AM_PM);
                comCal.clear(Calendar.MINUTE);
                comCal.clear(Calendar.SECOND);
                comCal.clear(Calendar.MILLISECOND);
                Date date = new Date(persistanceManager.getValue(Keys._time));
                comCal.setTime(date);
                Walk mWalk = new Walk();
                mWalk.setDate(comCal.getTime().toString());
                mWalk.setDistance(String.valueOf(distance));
                mWalk.setMilestone(String.valueOf(persistanceManager.getInteger(Keys._milestone)));

                mArrayListWalk.add(mWalk);
                mUser.setWalk(mArrayListWalk);
                persistanceManager.setValue(persistanceManager.getValue(Keys._currentuser), JSONHelper.Serialize(mUser));
                persistanceManager.setValue(Keys._distancecovered, "0.0");
                persistanceManager.setInteger(Keys._milestone, 0);
                persistanceManager.setValue(Keys._time, cal.getTime().toString());
            }

            @Override
            public void postExecute() {

            }
        }.execute();
    }

    // To load date to list view
    private void loadData() {
        new CustomeAsyncTask(HomeActivity.this) {
            StaticsAdaptor adapter;

            @Override
            public void doInBackground() {
                try {
                    if (mArrayListWalk != null) {
                        adapter = new StaticsAdaptor(HomeActivity.this, mArrayListWalk);
                        mLVWalk.setAdapter(adapter);
                    }
                } catch (Exception ex) {
                    System.err.println(ex.getLocalizedMessage());
                }
            }

            @Override
            public void postExecute() {

            }
        }.execute();

    }

    private void takeScreenshot() {

        InstaCapture.getInstance(HomeActivity.this).capture().setScreenCapturingListener(new ScreenCaptureListener() {

            @Override
            public void onCaptureStarted() {
                //TODO..
            }

            @Override
            public void onCaptureFailed(Throwable e) {
                //TODO..
            }

            @Override
            public void onCaptureComplete(Bitmap bitmap) {
                if (isStoragePermissionGranted()) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    File f = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "hpiscreenshot.jpg");
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FileOutputStream fo = null;
                    try {
                        fo = new FileOutputStream(f);

                        try {
                            fo.write(bytes.toByteArray());

                            fo.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    shareImage(f);
                }
            }
        });
    }

    private void locationService() {
        if (persistanceManager.getBoolean(Keys._walkstatus)) {
            mbtnStartWalk.setText("Stop Walking");
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(5000)
                    .setFastestInterval(5000);
            EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                    .setLocationRequest(locationRequest)
                    .setFallBackToLastLocationTime(3000)
                    .build();
            requestLocationUpdates(easyLocationRequest);
            persistanceManager.setBoolean(Keys._walkstatus, false);
        } else {
            mbtnStartWalk.setText("Start Walking");
            stopLocationUpdates();
            persistanceManager.setBoolean(Keys._walkstatus, true);
        }
    }

    // Share Image is to share the screenshot to user updates to social media, e-email etc
    void shareImage(File file) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        File f = file;
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            isStoragePermissionGranted();
            //resume tasks needing this permission
        }
    }

    @Override
    public void onLocationPermissionGranted() {

    }

    @Override
    public void onLocationPermissionDenied() {
        stopLocationUpdates();
        persistanceManager.setBoolean(Keys._walkstatus, false);
        Toast.makeText(HomeActivity.this, "HPI Cann't Calculate Distance", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationReceived(Location location) {
        if (tempLocation == null) {
            tempLocation = location;
        }
        distance += tempLocation.distanceTo(location);
        tempLocation = location;
        persistanceManager.setValue(Keys._distancecovered, String.valueOf(distance));
        if (persistanceManager.getInteger(Keys._milestone) < (int) (distance * 3.28 / 1000)) {
            int mile = (int) (distance * 3.28 / 1000);
            persistanceManager.setInteger(Keys._milestone, mile);
            mAlertDialog = new Dialog(HomeActivity.this);
            mAlertDialog.setContentView(R.layout.dialog_alert);
            Button btnAlertOK = (Button) mAlertDialog.findViewById(R.id.btn_alert_ok);
            TextView tvAlertBody = (TextView) mAlertDialog.findViewById(R.id.tv_alert_body);
            tvAlertBody.setText("You reached " + persistanceManager.getInteger(Keys._milestone) + " milestone");
            btnAlertOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAlertDialog.dismiss();
                }
            });
            mAlertDialog.show();
        }
        mtvTodayStatus.setText(" Today \n Distance :  " + String.format("%.2f", Float.valueOf(persistanceManager.getValue(Keys._distancecovered)) * 3.28) + "              Milestone :  " + persistanceManager.getInteger(Keys._milestone));
      //  Toast.makeText(HomeActivity.this, location.toString() + "Distance = " + String.valueOf(distance), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationProviderEnabled() {

    }

    @Override
    public void onLocationProviderDisabled() {
        stopLocationUpdates();
        persistanceManager.setBoolean(Keys._walkstatus, false);
        Toast.makeText(HomeActivity.this, "HPI Cann't Calculate Distance", Toast.LENGTH_LONG).show();
    }
}
