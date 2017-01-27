package hpi.com.hpifitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hpi.com.hpifitness.entity.User;
import hpi.com.hpifitness.persistance.PersistanceManager;
import hpi.com.hpifitness.utils.JSONHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText metFullName, metUserName, metPassword, metConfirmPassword;
    private Button mbtnRegister, mbtnBack;
    private PersistanceManager persistanceManager;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
        mbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    user = new User();
                    persistanceManager = new PersistanceManager(getBaseContext());
                    if ((User) persistanceManager.getValue(metUserName.getText().toString().trim(), User.class) == null) {
                        user.setFullname(metFullName.getText().toString().trim());
                        user.setUsername(metUserName.getText().toString().trim());
                        user.setPassword(metPassword.getText().toString().trim());
                        persistanceManager.setValue(user.getUsername(), JSONHelper.Serialize(user));
                        Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                    } else {
                        metFullName.setText("");
                        metUserName.setText("");
                        metPassword.setText("");
                        metConfirmPassword.setText("");
                        Toast.makeText(RegisterActivity.this, "User Exist", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    //Validate Registration Field
    private boolean validate() {
        if (metFullName.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Please Enter Full Name", Toast.LENGTH_LONG).show();
            return false;
        }
        if (metUserName.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Please Enter User Name", Toast.LENGTH_LONG).show();
            return false;
        }

        if (metPassword.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
            return false;
        }

        if (metConfirmPassword.getText().toString().trim().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Please Enter Confirm Password", Toast.LENGTH_LONG).show();
            return false;
        }

        if (metPassword.getText().toString().length() < 4) {
            Toast.makeText(RegisterActivity.this, "Password should be greater than 4 character", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!metPassword.getText().toString().equals(metConfirmPassword.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Confirm Password should match with Password", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void initialize() {
        metFullName = (EditText) findViewById(R.id.et_fullname);
        metUserName = (EditText) findViewById(R.id.et_username);
        metPassword = (EditText) findViewById(R.id.et_password);
        metConfirmPassword = (EditText) findViewById(R.id.et_confirmpassword);
        mbtnBack = (Button) findViewById(R.id.btn_back);
        mbtnRegister = (Button) findViewById(R.id.btn_register);
    }
}
