package com.example.opsctask2app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class wbf_categories extends AppCompatActivity {

    //Setting Variables
    ListView lstViewCat;
    ArrayList<String> arrayLstCat = new ArrayList<>();
    DatabaseReference dbRef;
    DatabaseReference fRef;
    ProgressBar progressBarCat;
    String CodeM[];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_categories);

        progressBarCat = (ProgressBar)findViewById(R.id.progressBarCat);

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setting Firebase data into array adapter then into List View
        //stackoverflow.com. 2021. Android List View Text Color: Android Tutorial.
        final ArrayAdapter<String> arrayAdptCat = new ArrayAdapter<String>(wbf_categories.this, android.R.layout.simple_list_item_1, arrayLstCat);
        lstViewCat = (ListView) findViewById(R.id.lstViewCat);
        lstViewCat.setAdapter(arrayAdptCat);

        //Pushing CatCode to wbf_stock_cat_m class
        lstViewCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(wbf_categories.this, wbf_stock_cat_m.class);
                Bundle bm = new Bundle();
                bm.putString("CatCodeM", CodeM[position]);
                intent.putExtras(bm);
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
                CodeM = new String[(int)dataSnapshot.getChildrenCount()];
                int index = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map mapStock = new HashMap();
                    mapStock = (HashMap) ds.getValue();
                    CodeM[index] = mapStock.get("CatID").toString();
                    arrayLstCat.add(mapStock.get("CatDesc").toString());
                    index++;
                }
                arrayAdptCat.notifyDataSetChanged();
                //stackoverflow.com. 2021. Android progress bar not hiding: Android Tutorial.
                progressBarCat.setVisibility(RelativeLayout.INVISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
    }
}

