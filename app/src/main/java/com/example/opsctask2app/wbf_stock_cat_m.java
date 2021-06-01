package com.example.opsctask2app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
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

public class wbf_stock_cat_m extends AppCompatActivity {

    //Setting Variables
    DatabaseReference dbRef;
    DatabaseReference fRef;
    ProgressBar progressBarStockM;
    TextView noItemsM;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    RecyclerView ItemsViewM;

    String CodeM[];
    String ItemNameM[];
    String ItemDescM[];
    String PriceM[];
    String AmountM[];
    String DateCreatedM[];
    String CatIDM[];
    String ImgURLM[];
    String CategoryIDM = "-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_stock_cat_m);

        noItemsM = (TextView) findViewById(R.id.tvNoItemsM);
        noItemsM.setVisibility(RelativeLayout.INVISIBLE);

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        progressBarStockM = (ProgressBar)findViewById(R.id.progressBarStockM);

        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF Stock");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();

        if(b != null){
            CategoryIDM = b.getString("CatCodeM");
        }

        //Calling data from Firebase as a JSON Object (Blog.mindorks.com. 2021.)
        dbRef = FirebaseDatabase.getInstance().getReference();
        fRef = dbRef.child("Items");
        Query query = fRef.orderByChild("CatID").equalTo(Integer.parseInt(CategoryIDM));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                CatIDM = new String[(int)dataSnapshot.getChildrenCount()];
                ItemDescM = new String[(int)dataSnapshot.getChildrenCount()];
                ItemNameM = new String[(int)dataSnapshot.getChildrenCount()];
                AmountM = new String[(int)dataSnapshot.getChildrenCount()];
                PriceM = new String[(int)dataSnapshot.getChildrenCount()];
                CodeM = new String[(int)dataSnapshot.getChildrenCount()];
                DateCreatedM = new String[(int)dataSnapshot.getChildrenCount()];
                ImgURLM = new String[(int)dataSnapshot.getChildrenCount()];

                int index = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    Map mapStock = new HashMap();
                    mapStock = (HashMap) ds.getValue();
                    CatIDM[index] = mapStock.get("CatID").toString();
                    ItemDescM[index] = mapStock.get("ItemDesc").toString();
                    ItemNameM[index] = mapStock.get("ItemName").toString();
                    AmountM[index] = mapStock.get("Amount").toString();
                    PriceM[index] = mapStock.get("Price").toString();
                    DateCreatedM[index] = mapStock.get("DateFound").toString();
                    ImgURLM[index] = mapStock.get("imgURL").toString();

                    index++;
                }
                progressBarStockM.setVisibility(RelativeLayout.INVISIBLE);

                ItemsViewM = findViewById(R.id.ReclViewItemsM);
                try
                {
                    //Checking if category does have items
                    if(ItemDescM.length == 0)
                    {
                        noItemsM.setVisibility(RelativeLayout.VISIBLE);
                        Toast.makeText(wbf_stock_cat_m.this, "No Items, please add stock", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //If there is items to show(data retrieved from Firebase), then load Adapter into Item View
                        wbf_stock_adapter stockAdapter = new wbf_stock_adapter(wbf_stock_cat_m.this, ItemDescM, ItemNameM, ImgURLM, PriceM, DateCreatedM, AmountM, CatIDM);
                        ItemsViewM.setAdapter(stockAdapter);
                        ItemsViewM.setLayoutManager(new LinearLayoutManager(wbf_stock_cat_m.this));
                    }
                }
                catch (Exception e) {
                    Toast.makeText(wbf_stock_cat_m.this, "No Items", Toast.LENGTH_SHORT).show();
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


