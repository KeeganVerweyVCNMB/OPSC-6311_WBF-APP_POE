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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class wbf_items_graph extends AppCompatActivity {

    //Setting Variables
    private TextView areGoalReached;
    private TextView displayGoalID;
    ProgressBar progressBarViewGoal;
    private DatabaseReference mDatabase, fDatabase;

    int sumItems;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_items_graph);

        progressBarViewGoal = (ProgressBar)findViewById(R.id.progressBarViewGoal);

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF Items Graph");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        barChart = (BarChart)findViewById(R.id.bargraphViewGoal);
        barChart.setNoDataText("Calculation In Progress");

        areGoalReached = (TextView)findViewById(R.id.areGoalReached);
        displayGoalID = (TextView)findViewById(R.id.displayGoalID);

        //Calculating count of all items in firebase database
        fDatabase = FirebaseDatabase.getInstance().getReference().child("Items");
        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object viewItems = map.get("Amount");

                    int dbItems = Integer.parseInt(String.valueOf(viewItems));
                    sumItems += dbItems;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Calculating count of all goals of child categories in firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Categories");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumGoals = 0;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String,Object> map = (Map<String,Object>) ds.getValue();
                    Object viewGoal = map.get("Goal");

                    int dbGoal = Integer.parseInt(String.valueOf(viewGoal));
                    sumGoals += dbGoal;

                    displayGoalID.setText(String.valueOf(sumGoals));
                }
                //Setting goal text according to calculations from firebase database
                if(sumItems > sumGoals)
                {
                    int overGoal = sumItems - sumGoals;
                    areGoalReached.setText("Store over goal by " + String.valueOf(overGoal) + " items");
                }
                else if(sumItems < sumGoals) {
                    int toGoGoal = sumGoals - sumItems;
                    areGoalReached.setText(String.valueOf(toGoGoal) + " before goal is reached");
                }
                else if(sumItems == sumGoals){
                    areGoalReached.setText("Goal Reached!");
                }

                //github.com. 2021. Creating graphs in Android Studio
                //Setting graph entries
                ArrayList<BarEntry> barEntries = new ArrayList<>();
                barEntries.add(new BarEntry( 1, sumGoals, 0));
                barEntries.add(new BarEntry( 2, sumItems, 1));

                //Setting custom graph styling
                BarDataSet barDataSet = new BarDataSet(barEntries, "WBF Store Goal");
                barDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                barDataSet.setValueTextSize(12);

                BarData populateData = new BarData(barDataSet);
                barChart.setData(populateData);

                barChart.getXAxis().setDrawGridLines(false);
                barChart.getXAxis().setDrawAxisLine(false);

                barChart.getAxisLeft().setDrawGridLines(false);
                barChart.getAxisRight().setEnabled(false);
                barChart.getAxisLeft().setEnabled(true);

                barChart.getAxisRight().setDrawGridLines(false);
                barChart.getAxisLeft().setAxisMinimum(0);
                barChart.getAxisRight().setAxisMinimum(0);

                barChart.setDrawValueAboveBar(false);

                barChart.getDescription().setText("");
                barChart.invalidate();

                progressBarViewGoal.setVisibility(RelativeLayout.INVISIBLE);
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
            startActivity(new Intent(wbf_items_graph.this, wbf_dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
