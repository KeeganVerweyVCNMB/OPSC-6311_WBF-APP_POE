package com.example.opsctask2app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class wbf_registration extends AppCompatActivity {
    //Setting Variables
    FirebaseAuth fireBAuth;
    EditText rgstName, rgstSurname, rgstEmail, rgstPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_registration);

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Checking if device is connected to WIFI or Mobile Data
        //stackoverflow.com. 2021. How to check currently internet connection is available or not in android: Android Tutorial.
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //Do Nothing
        }
        else {
            Toast.makeText(this, "No internet connectivity", Toast.LENGTH_SHORT).show();
        }

        //Setting UI into variables
        fireBAuth = FirebaseAuth.getInstance();
        rgstName = findViewById(R.id.txtName);
        rgstSurname = findViewById(R.id.txtSurname);
        rgstEmail = findViewById(R.id.txtEmail);
        rgstPassword = findViewById(R.id.txtPassword);

        final Button btnConfirmRegister = (Button) findViewById(R.id.btnConfirmRegister);
        btnConfirmRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = rgstName.getText().toString();
                String surname = rgstSurname.getText().toString();
                String email = rgstEmail.getText().toString();
                String password = rgstPassword.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                //Validation for registration
                if(TextUtils.isEmpty(name)) {
                    rgstName.setError("Name is a required field");
                    return;
                }
                else if(TextUtils.isEmpty(surname)) {
                    rgstSurname.setError("Surname is a required field");
                    return;
                }
                else if(TextUtils.isEmpty(email)) {
                    rgstEmail.setError("Email is a required field");
                    return;
                }
                else if(!email.matches(emailPattern)) {
                    rgstEmail.setError("Please enter a valid email address");
                    return;
                }
                else if(password.length() < 7) {
                    rgstPassword.setError("Password must be 8 or more characters");
                    return;
                }
                else {
                    registerNewUser(email, password);
                }

            }
        });
    }

    //Firebase class for registering new user with Firebase Authentication (Blog.mindorks.com. 2021.)
    private void registerNewUser(String email, String password) {
        try {
            fireBAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(wbf_registration.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(wbf_registration.this, wbf_login.class));
                    }
                    else {
                        Toast.makeText(wbf_registration.this, "Error Registering", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch (Exception e)
        {
            //Do Nothing
        }
    }
}
