package hpi.com.hpifitness;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import hpi.com.hpifitness.persistance.Keys;
import hpi.com.hpifitness.persistance.PersistanceManager;

public class SplashActivity extends AppCompatActivity {

    private PersistanceManager persistanceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        persistanceManager = new PersistanceManager(SplashActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // goto home page
                Intent i;
                if (persistanceManager.getValue(Keys._currentuser) != null) {
                    i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, 1000);
    }
}
