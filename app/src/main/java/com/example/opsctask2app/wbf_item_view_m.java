package com.example.opsctask2app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;


public class wbf_item_view_m extends AppCompatActivity {

    //Setting Variables
    ImageView mainImageViewM;
    TextView titleM;
    TextView DescriptionM;
    TextView ItmCodeM;
    TextView ItmQtyM;
    TextView ItemPriceM;
    String dtItemDescM, dtItemNameM, dtCatIDM, dtAmountM, dtPriceM;
    String dtImageM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_item_view_m);

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF View Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setting UI into variables
        mainImageViewM = findViewById(R.id.ImgItemViewM);
        titleM = findViewById(R.id.ItemTitleM);
        DescriptionM = findViewById(R.id.ItemDescM);
        ItmCodeM = findViewById(R.id.itemCodeM);
        ItmQtyM = findViewById(R.id.ItemQtyM);
        ItemPriceM = findViewById(R.id.ItemsSellM);

        //Calling getData and setData methods
        getData();
        setData();
    }

    //Calling Firebase data from adapter class
    private void getData()
    {
        if((getIntent().hasExtra("images"))&&(getIntent().hasExtra("ItemDesc"))&&(getIntent().hasExtra("ItemName"))
                &&(getIntent().hasExtra("CatID"))&&(getIntent().hasExtra("Price")&&(getIntent().hasExtra("Amount"))))
        {
            dtItemDescM = getIntent().getStringExtra("ItemDesc");
            dtItemNameM = getIntent().getStringExtra("ItemName");
            dtImageM = getIntent().getStringExtra("images");
            dtCatIDM = getIntent().getStringExtra("CatID");
            dtPriceM = getIntent().getStringExtra("Price");
            dtAmountM = getIntent().getStringExtra("Amount");
        }
        else {
            Toast.makeText(this,"Process Failed",Toast.LENGTH_SHORT).show();
        }
    }
    //Setting adapter class data into UI
    private void setData()
    {
        titleM.setText(dtItemDescM);
        DescriptionM.setText(dtItemNameM);
        Picasso.with(this).load(dtImageM).into(mainImageViewM);
        ItmCodeM.setText(dtCatIDM);
        ItemPriceM.setText("R " + dtPriceM);
        ItmQtyM.setText(dtAmountM);
    }
}