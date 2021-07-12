package com.example.opsctask2app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class wbf_dashboard extends AppCompatActivity {
    //Setting Variables
    LinearLayout CardFolders, AddFile, SignOut, AddItem, ItemGraph, TotalItems, TotalValue, Scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_dashboard);

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ItemGraph=findViewById(R.id.btnItems);
        ItemGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(wbf_dashboard.this, wbf_items_graph.class));
            }
        });

        //Navigate to categories page
        CardFolders=findViewById(R.id.btnFolders);
        CardFolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(wbf_dashboard.this, wbf_categories.class));
            }
        });

        TotalItems=findViewById(R.id.btnTotalItems);
        TotalItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(wbf_dashboard.this, wbf_total_items.class));
            }
        });

        TotalValue=findViewById(R.id.btnTotalValue);
        TotalValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(wbf_dashboard.this, wbf_total_value.class));
            }
        });

        //Navigate to add category page
        AddFile=findViewById(R.id.btnAddFile);
        AddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(wbf_dashboard.this, wbf_add_cat.class));
            }
        });

        //Navigate to add item page
        AddItem=findViewById(R.id.btnaddItem);
        AddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(wbf_dashboard.this, wbf_add_item.class));
            }
        });

        //Navigate to wbf_login page
        SignOut=findViewById(R.id.btnSignOut);
        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(wbf_dashboard.this, wbf_login.class));
            }
        });

        //Navigate to wbf_scanner page
        Scanner=findViewById(R.id.btnBarcodeScanner);
        Scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(wbf_dashboard.this, wbf_scanner.class));
            }
        });


    }
}
