package com.example.opsctask2app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class wbf_browse extends AppCompatActivity
{
    //Setting Variables
    ListView lstViewBrowse;
    ArrayList<String> arrayLstBrowse = new ArrayList<>();
    DatabaseReference dbRef;
    DatabaseReference fRef;
    ProgressBar progressBarBrowse;
    String Code[];
    ImageView ProdImage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_browse);

        progressBarBrowse = (ProgressBar)findViewById(R.id.progressBarBrowse);

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF Browse Stock");
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

        //Setting Firebase data into array adapter then into List View
        //(stackoverflow.com. 2021. Android List View Text Color: Android Tutorial.)
        final ArrayAdapter<String> arrayAdptBrowse = new ArrayAdapter<String>(wbf_browse.this, android.R.layout.simple_list_item_1, arrayLstBrowse);
        lstViewBrowse = (ListView) findViewById(R.id.lstViewBrowse);
        lstViewBrowse.setAdapter(arrayAdptBrowse);

        //Pushing CatCode to wbf_stock_cat class
        lstViewBrowse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(wbf_browse.this, wbf_stock_cat.class);
                Bundle b = new Bundle();
                b.putString("CatCode", Code[position]);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        //Calling Firebase data for List View (Blog.mindorks.com. 2021.)
        dbRef = FirebaseDatabase.getInstance().getReference();
        fRef = dbRef.child("Categories");
        Query query = fRef.orderByChild("IsCategory").equalTo("True");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Code = new String[(int)dataSnapshot.getChildrenCount()];
                int index = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map mapStock = new HashMap();
                    mapStock = (HashMap) ds.getValue();
                    Code[index] = mapStock.get("CatID").toString();
                    arrayLstBrowse.add(mapStock.get("CatDesc").toString());
                    index++;
                }
                arrayAdptBrowse.notifyDataSetChanged();
                //stackoverflow.com. 2021. Android progress bar not hiding: Android Tutorial.
                progressBarBrowse.setVisibility(RelativeLayout.INVISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do Nothing
            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
    }
}
