package com.example.opsctask2app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class wbf_stock_cat extends AppCompatActivity {

    //Setting Variables
    DatabaseReference dbRef;
    DatabaseReference fRef;
    ProgressBar progressBarStock;
    TextView noItems;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    RecyclerView ItemsView;

    String Code[];
    String ItemName[];
    String ItemDesc[];
    String Price[];
    String Amount[];
    String DateCreated[];
    String CatID[];
    String ImgURL[];
    String CategoryID = "-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_stock_cat);

        noItems = (TextView) findViewById(R.id.tvNoItems);
        noItems.setVisibility(RelativeLayout.INVISIBLE);

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressBarStock = (ProgressBar)findViewById(R.id.progressBarStock);

        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF Stock");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();

        if(b != null){
            CategoryID = b.getString("CatCode");
        }

        //Calling data from Firebase as a JSON Object (Blog.mindorks.com. 2021.)
        dbRef = FirebaseDatabase.getInstance().getReference();
        fRef = dbRef.child("Items");
        Query query = fRef.orderByChild("CatID").equalTo(Integer.parseInt(CategoryID));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                CatID = new String[(int)dataSnapshot.getChildrenCount()];
                ItemDesc = new String[(int)dataSnapshot.getChildrenCount()];
                ItemName = new String[(int)dataSnapshot.getChildrenCount()];
                Amount = new String[(int)dataSnapshot.getChildrenCount()];
                Price = new String[(int)dataSnapshot.getChildrenCount()];
                Code = new String[(int)dataSnapshot.getChildrenCount()];
                DateCreated = new String[(int)dataSnapshot.getChildrenCount()];
                ImgURL = new String[(int)dataSnapshot.getChildrenCount()];

                int index = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    Map mapStock = new HashMap();
                    mapStock = (HashMap) ds.getValue();
                    CatID[index] = mapStock.get("CatID").toString();
                    ItemDesc[index] = mapStock.get("ItemDesc").toString();
                    ItemName[index] = mapStock.get("ItemName").toString();
                    Amount[index] = mapStock.get("Amount").toString();
                    Price[index] = mapStock.get("Price").toString();
                    DateCreated[index] = mapStock.get("DateFound").toString();
                    ImgURL[index] = mapStock.get("imgURL").toString();

                    index++;
                }
                progressBarStock.setVisibility(RelativeLayout.INVISIBLE);

                ItemsView = findViewById(R.id.ReclViewItems);
                try
                {
                    //Checking if category does have items
                    if(ItemDesc.length == 0)
                    {
                        noItems.setVisibility(RelativeLayout.VISIBLE);
                        Toast.makeText(wbf_stock_cat.this, "No Items, please add stock", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //If there is items to show(data retrieved from Firebase), then load Adapter into Item View
                        wbf_stock_adapter stockAdapter = new wbf_stock_adapter(wbf_stock_cat.this, ItemDesc, ItemName, ImgURL, Price, DateCreated, Amount, CatID);
                        ItemsView.setAdapter(stockAdapter);
                        ItemsView.setLayoutManager(new LinearLayoutManager(wbf_stock_cat.this));
                    }
                }
                catch (Exception e) {
                    Toast.makeText(wbf_stock_cat.this, "No Items", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do Nothing
            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
    }
}


