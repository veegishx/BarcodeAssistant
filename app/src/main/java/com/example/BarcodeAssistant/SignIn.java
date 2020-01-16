package com.example.BarcodeAssistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.BarcodeAssistant.Database.DatabaseHelper;
import com.example.BarcodeAssistant.Database.DatabaseHelper;

public class SignIn extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button signInBtn, getStartedBtn;
    private DatabaseHelper db;
    private final String PREFERENCE_FILE = "PreferenceFile";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(PREFERENCE_FILE, MODE_PRIVATE);
        if(!(sharedPreferences.getBoolean("session", false))) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_in);

            db = new DatabaseHelper(this);

            usernameEditText = (EditText) findViewById(R.id.userNameEditText);
            passwordEditText = (EditText) findViewById(R.id.passwordEditText);

            signInBtn = (Button) findViewById(R.id.createAccountBtn);
            getStartedBtn = (Button) findViewById(R.id.backToLoginBtn);

            getStartedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SignIn.this, RegisterActivity.class);
                    startActivity(i);
                }
            });

            signInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String user = usernameEditText.getText().toString().trim();
                    String pwd = passwordEditText.getText().toString().trim();
                    Boolean res = db.checkUser(user, pwd);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (res) {
                        editor.putBoolean("session", true);
                        editor.apply();
                        Intent HomePage = new Intent(SignIn.this, MainActivity.class);
                        startActivity(HomePage);
                    } else {
                        editor.putBoolean("session", false);
                        editor.apply();
                        Toast.makeText(SignIn.this, "Login Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Intent HomePage = new Intent(SignIn.this, MainActivity.class);
            startActivity(HomePage);
        }
    }
}
