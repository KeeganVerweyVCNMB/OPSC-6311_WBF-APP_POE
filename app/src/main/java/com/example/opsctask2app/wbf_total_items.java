package com.example.opsctask2app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class wbf_total_items extends AppCompatActivity {

    //Setting Variables
    private TextView fTotalItems, fTotalAllItems;
    ProgressBar progressBarTotalVal;
    private DatabaseReference mDatabase;

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_total_items);

        progressBarTotalVal = (ProgressBar)findViewById(R.id.progressBarTotalItems);

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF Total Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pieChart = (PieChart)findViewById(R.id.graphTotalItems);
        pieChart.setNoDataText("Calculation In Progress");

        //Setting data for text display
        fTotalItems = (TextView)findViewById(R.id.totalItemsID);
        fTotalAllItems = (TextView)findViewById(R.id.totalItemsID2);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Items");

        //Calculating count of all items in firebase database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumItems = 0;
                int size = 0;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String,Object> map = (Map<String,Object>) ds.getValue();
                    Object inStock = map.get("Amount");

                    int dbValueSell = Integer.parseInt(String.valueOf(inStock));
                    sumItems += dbValueSell;
                    size = (int) snapshot.getChildrenCount();

                    fTotalItems.setText(String.valueOf(sumItems) + " items in stock");
                    fTotalAllItems.setText(String.valueOf(size) + " variants of items");
                }
                //github.com. 2021. Creating graphs in Android Studio
                //Setting graph entries
                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                pieEntries.add(new PieEntry(sumItems));
                pieEntries.add(new PieEntry(size));

                //Setting custom graph styling
                PieDataSet pieDataSet = new PieDataSet(pieEntries, "Items In Stock");
                pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                pieDataSet.setValueTextSize(12);

                PieData populateData = new PieData(pieDataSet);
                pieChart.setData(populateData);

                pieChart.getDescription().setText("");
                pieChart.invalidate();

                progressBarTotalVal.setVisibility(RelativeLayout.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            startActivity(new Intent(wbf_total_items.this, wbf_dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
