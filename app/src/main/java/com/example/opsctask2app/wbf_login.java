package com.example.opsctask2app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class wbf_login extends AppCompatActivity {

    //Setting Variables
    FirebaseAuth fireBAuth;
    EditText lgnEmail, lgnPassword;
    ProgressBar progressBarLogin;
    TextView tvFogotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_login);

        //Forgot Password navigation
        tvFogotPass = (TextView) findViewById(R.id.tvForgotPassword);
        tvFogotPass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(wbf_login.this, wbf_forgot_password.class));
            }
        });

        progressBarLogin = (ProgressBar)findViewById(R.id.progressBarLogin);
        //stackoverflow.com. 2021. Android progress bar not hiding: Android Tutorial.
        progressBarLogin.setVisibility(RelativeLayout.INVISIBLE);

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF Login");
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

        fireBAuth = FirebaseAuth.getInstance();
        lgnEmail = findViewById(R.id.txtLgnEmail);
        lgnPassword = findViewById(R.id.txtLgnPass);

        //Button login validation
        final Button loginToDash = (Button) findViewById(R.id.btnLoginToDash);
        loginToDash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = lgnEmail.getText().toString();
                String password = lgnPassword.getText().toString();

                //Validation
                if(TextUtils.isEmpty(email)) {
                    lgnEmail.setError("Email is a required field");
                    return;
                }
                else if(lgnPassword.length() < 7) {
                    lgnPassword.setError("Password must be 8 or more characters");
                    return;
                }
                //If validation succeeds, call login user class and log user into system
                else {
                    //stackoverflow.com. 2021. Android progress bar not hiding: Android Tutorial.
                    progressBarLogin.setVisibility(RelativeLayout.VISIBLE);
                    loginUser(email, password);
                }
            }
        });
    }

    //Firebase class for signing user into system (Blog.mindorks.com. 2021.)
    private void loginUser(String email, String password) {
        try {
            fireBAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(wbf_login.this, "Logged in as: " + email.toString(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(wbf_login.this, wbf_dashboard.class));
                    } else {
                        Toast.makeText(wbf_login.this, "Username or Password Incorrect", Toast.LENGTH_LONG).show();
                    }
                    //stackoverflow.com. 2021. Android progress bar not hiding: Android Tutorial.
                    progressBarLogin.setVisibility(RelativeLayout.INVISIBLE);
                }
            });
        } catch (Exception e) {
            //Do Nothing
        }
    }

}
