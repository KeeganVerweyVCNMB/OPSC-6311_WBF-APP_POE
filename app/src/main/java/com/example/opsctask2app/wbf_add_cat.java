package com.example.opsctask2app;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class wbf_add_cat extends AppCompatActivity {

    //Setting Variables
    DatabaseReference dbRef;
    EditText addCategory, addGoal;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_add_cat);

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF Add Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Calling UI into variables
        addCategory = findViewById(R.id.txtCatName);
        addGoal = findViewById(R.id.txtAddGoal);

        //Only display numeric keyboard for user input
        //stackoverflow.com. 2021. How do you set the edit text keyboard to only consist of number on android: Android Tutorial.
        addGoal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        addGoal.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        //Add Category button click
        final Button btnAddCatFile = (Button) findViewById(R.id.btnAddNewCat);
        btnAddCatFile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String category = addCategory.getText().toString();
                String goal = addGoal.getText().toString();

                //Generating random number to match categories to items(CatID)
                int max = 1000000;
                int min = 0;

                Random randomNum = new Random();
                int generatedID = min + randomNum.nextInt(max);

                //Validation
                if(TextUtils.isEmpty(category)) {
                    addCategory.setError("Please add a category");
                    return;
                }
                else if(TextUtils.isEmpty(goal)) {
                    addGoal.setError("Please add a goal");
                    return;
                }
                else {
                    //If validation succeeds, push data to Firebase (Blog.mindorks.com. 2021.)
                    try
                    {
                        dbRef = FirebaseDatabase.getInstance().getReference("Categories");
                        dbRef.child(category).setValue(category);

                        dbRef = FirebaseDatabase.getInstance().getReference("Categories");
                        dbRef.child(category).child("CatDesc").setValue(category);

                        dbRef = FirebaseDatabase.getInstance().getReference("Categories");
                        dbRef.child(category).child("CatID").setValue(generatedID);

                        dbRef = FirebaseDatabase.getInstance().getReference("Categories");
                        dbRef.child(category).child("Goal").setValue(goal);

                        dbRef = FirebaseDatabase.getInstance().getReference("Categories");
                        dbRef.child(category).child("IsCategory").setValue("True");


                        Toast.makeText(wbf_add_cat.this, "New Category Added", Toast.LENGTH_SHORT).show();
                        addCategory.setText("");
                        addGoal.setText("");
                    }
                    catch (Exception e){
                        addCategory.setError("Could not add category");
                    }
                }

            }
        });
    }

    //Numeric keyboard class
    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }
}


