package com.example.opsctask2app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

public class wbf_item_view extends AppCompatActivity {

    //Setting Variables
    ImageView mainImageView;
    TextView title;
    TextView Description;
    TextView ItmCode;
    TextView ItmQty;
    TextView ItemPrice;
    String dtItemDesc, dtItemName, dtCatID , dtAmount, dtPrice;
    String dtImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_item_view);

        //Disable dark mode setting
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF View Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Calling UI into variables
        mainImageView = findViewById(R.id.ImgItemView);
        title = findViewById(R.id.ItemTitle);
        Description = findViewById(R.id.ItemDesc);
        ItmCode = findViewById(R.id.itemCode);
        ItmQty = findViewById(R.id.ItemQty);
        ItemPrice = findViewById(R.id.ItemsSell);

        //Calling getData and setData Methods
        getData();
        setData();
    }

    //Calling Firebase data from adapter class
    private void getData()
    {
        if((getIntent().hasExtra("images"))&&(getIntent().hasExtra("ItemDesc"))&&(getIntent().hasExtra("ItemName"))
                &&(getIntent().hasExtra("CatID"))&&(getIntent().hasExtra("Price")&&(getIntent().hasExtra("Amount"))))
        {
            dtItemDesc = getIntent().getStringExtra("ItemDesc");
            dtItemName = getIntent().getStringExtra("ItemName");
            dtImage = getIntent().getStringExtra("images");
            dtCatID = getIntent().getStringExtra("CatID");
            dtPrice = getIntent().getStringExtra("Price");
            dtAmount = getIntent().getStringExtra("Amount");
        }
        else {
            Toast.makeText(this,"Process Failed",Toast.LENGTH_SHORT).show();
        }
    }
    //Setting adapter class data into UI
    private void setData()
    {
        title.setText(dtItemDesc);
        Description.setText(dtItemName);
        Picasso.with(this).load(dtImage).into(mainImageView);
        ItmCode.setText(dtCatID);
        ItemPrice.setText("R " + dtPrice);
        ItmQty.setText(dtAmount);
    }
}