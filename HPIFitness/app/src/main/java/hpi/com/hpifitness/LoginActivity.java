package hpi.com.hpifitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hpi.com.hpifitness.entity.User;
import hpi.com.hpifitness.persistance.Keys;
import hpi.com.hpifitness.persistance.PersistanceManager;

public class LoginActivity extends AppCompatActivity {

    private Button mbtnLogin, mbtnRegister;
    private EditText metUserName, metPassword;
    private PersistanceManager persistanceManager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        // Log in Function - Used Shared Preference to Manage this project
        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    user = new User();
                    persistanceManager = new PersistanceManager(getBaseContext());
                    user = (User) persistanceManager.getValue(metUserName.getText().toString().trim(), User.class);
                    if (user != null) {
                        if (user.getUsername().equals(metUserName.getText().toString().trim()) && user.getPassword().equals(metPassword.getText().toString().trim())) {
                            // Used for one time Login - Using username we are fetching user details.
                            persistanceManager.setValue(Keys._currentuser, metUserName.getText().toString().trim());
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                        } else {
                            clearLoginData();
                            Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        clearLoginData();
                        Toast.makeText(LoginActivity.this, "User Doesn't Exist", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void clearLoginData() {
        metUserName.setText("");
        metPassword.setText("");
    }

    //Validate user login form
    private boolean validation() {
        if (metUserName.getText().toString().trim().length() == 0) {
            Toast.makeText(LoginActivity.this, "Please Enter User Name", Toast.LENGTH_LONG).show();
            return false;
        }

        if (metPassword.getText().toString().trim().length() == 0) {
            Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void initialize() {
        metUserName = (EditText) findViewById(R.id.et_username);
        metPassword = (EditText) findViewById(R.id.et_password);
        mbtnLogin = (Button) findViewById(R.id.btn_login);
        mbtnRegister = (Button) findViewById(R.id.btn_register);
    }
}
