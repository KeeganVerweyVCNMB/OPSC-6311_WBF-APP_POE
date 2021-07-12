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

public class wbf_total_value extends AppCompatActivity {

    //Setting Variables
    private TextView fTotalSell;
    private TextView fTotalCost;
    private TextView fProfit;
    ProgressBar progressBarTotalVal;
    private DatabaseReference mDatabase;

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_total_value);

        progressBarTotalVal = (ProgressBar)findViewById(R.id.progressBarTotalVal);

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF Total Value");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        barChart = (BarChart)findViewById(R.id.bargraph);
        barChart.setNoDataText("Calculation In Progress");

        fTotalSell = (TextView)findViewById(R.id.totalSellAmtID);
        fTotalCost = (TextView)findViewById(R.id.totalCostAmtID);
        fProfit = (TextView)findViewById(R.id.totalProfitMarginID);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Items");

        //Calculating count of all items in firebase database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumSell = 0;
                int sumCost = 0;
                int profit = 0;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String,Object> map = (Map<String,Object>) ds.getValue();
                    Object priceSell = map.get("Price");
                    Object priceCost = map.get("CostPrice");

                    int dbValueSell = Integer.parseInt(String.valueOf(priceSell));
                    int dbValueCost = Integer.parseInt(String.valueOf(priceCost));
                    sumSell += dbValueSell;
                    sumCost += dbValueCost;
                    profit = sumSell - sumCost;

                    //Setting data for text display
                    fTotalSell.setText("R " + String.valueOf(sumSell));
                    fTotalCost.setText("R " + String.valueOf(sumCost));
                    fProfit.setText("R " + String.valueOf(profit));
                }
                //github.com. 2021. Creating graphs in Android Studio
                //Setting graph entries
                ArrayList<BarEntry> barEntries = new ArrayList<>();
                barEntries.add(new BarEntry( 1, sumCost, 0));
                barEntries.add(new BarEntry(2, sumSell, 1));

                //Setting custom graph styling
                BarDataSet barDataSet = new BarDataSet(barEntries, "Store Prices");
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
            startActivity(new Intent(wbf_total_value.this, wbf_dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
