package com.example.opsctask2app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class wbf_scanner extends AppCompatActivity {

    //Setting Variables
    private CodeScanner codeScanner;
    private CodeScannerView codeScanViewable;
    private TextView scannerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_scanner);

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF Product Scanner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Checking API access layer for Android Device
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 123);
        }
        else {
            startScanning();
        }
    }

    private void startScanning() {

        //github.com. 2021. How to implement a scanner in Android Studio
        codeScanViewable = findViewById(R.id.viewForScanner);
        scannerID = findViewById(R.id.tvScannerID);
        scannerID.setText("Ready to scan product...");
        codeScanner = new CodeScanner(this, codeScanViewable);

        //Starting scanning process
        codeScanner.startPreview();
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scannerID.setText(result.getText());
                    }
                });
            }
        });

        //Clear previous scan and start scanning process again
        codeScanViewable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Updating code scanner preview
                codeScanner.startPreview();
                scannerID.setText("Ready to scan product...");
            }
        });
    }

    //Method for Checking API access layer for Android Device
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123){
            //Granting permissions
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Scanning Permissions Granted", Toast.LENGTH_SHORT).show();
                startScanning();
            } else {
                Toast.makeText(this, "Scanning Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Add action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnNavHome) {
            startActivity(new Intent(wbf_scanner.this, wbf_dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }
}