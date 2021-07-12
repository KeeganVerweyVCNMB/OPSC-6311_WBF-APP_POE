package com.example.opsctask2app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

//Extending wbf_user_guide class to TutorialActivity to be able to set on-boarding process
public class wbf_user_guide extends TutorialActivity {

    //github.com. 2021. How to create a tutorials view in Android Studio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting fragment data and building screen for welcome user
        addFragment(new Step.Builder().setTitle("Welcome to WBF")
                .setContent("We would like to welcome you aboard WBF (Willies Bait & Fishing Tackle) store and stock " +
                        "managing application. This application will assist in keeping track of stock items, stock categories," +
                        "as well as allow you to create your own stock categories and add items to them. We also have a built in " +
                        "feature where you can scan your stock QR or bar-codes.")
                .setBackgroundColor(Color.parseColor("#eda03b"))
                .setDrawable(R.drawable.welcome_onboarding)
                .setSummary("Welcome aboard!")
                .build());

        //Setting fragment data and building screen for app explanations screen 1
        addFragment(new Step.Builder().setTitle("Freely browse the inventory")
                .setContent("No need to sign up, you can still see all inventory items as well as all the categories " +
                        "that they are associated with. This no sign-up feature is set in place to skip all sign-up processes " +
                        "and be able to quickly access items and see in store availability.")
                .setBackgroundColor(Color.parseColor("#eda03b"))
                .setDrawable(R.drawable.browse_onboarding)
                .setSummary("No need to sign up")
                .build());

        //Setting fragment data and building screen for app explanations screen 2
        addFragment(new Step.Builder().setTitle("Sign up")
                .setContent("By signing up and becoming a member/employee of WBF (Willies Bait & Fishing Tackle) you open a new world " +
                        "and never before seen side of this application. Of course you will need WBF permissions and acceptability before " +
                        "getting your hand on this in house stock taking application. With signing up to WBF you get all " +
                        "the premium and advance features of this application. Add categories, add items, manage stock, scan products, view graphs " +
                        "and many more. Sign up now and get a better experience!")
                .setBackgroundColor(Color.parseColor("#eda03b"))
                .setDrawable(R.drawable.signup_onboarding)
                .setSummary("Sign up and get premium features")
                .build());

        //Setting fragment data and building screen for app explanations screen 3
        addFragment(new Step.Builder().setTitle("Add categories")
                .setContent("WBF (Willies Bait & Fishing Tackle) allows it's users to add and manage categories. Simply navigate " +
                        "to the user friendly dashboard, click on 'Add Category' and give your category a name and add a goal. " +
                        "See the magic happen right before your eyes.")
                .setBackgroundColor(Color.parseColor("#eda03b"))
                .setDrawable(R.drawable.cat_onboarding)
                .setSummary("Add more and more...")
                .build());

        //Setting fragment data and building screen for app explanations screen 4
        addFragment(new Step.Builder().setTitle("Add items")
                .setContent("WBF (Willies Bait & Fishing Tackle) allows it's users to add and manage items within an assigned category. Simply navigate " +
                        "to the user friendly dashboard, click on 'Add Item' and follow the easy flow of adding your new item/product. " +
                        "Another great feature of this is the ability to add a local file or take your own photo without leaving the app. " +
                        "This is simply some of the best you will get. Try this out now.")
                .setBackgroundColor(Color.parseColor("#eda03b"))
                .setDrawable(R.drawable.add_onboarding)
                .setSummary("Extend your inventory")
                .build());

        //Setting fragment data and building screen for app explanations screen 5
        addFragment(new Step.Builder().setTitle("View realtime calculated graphs")
                .setContent("No need to worry about having to do calculations and try and figure out your value for your money. " +
                        "WBF (Willies Bait & Fishing Tackle) is directly linked with Firebase Realtime Cloud Databases. " +
                        "This allows to do some advanced calculations and show them to you in the best possible graph format available. " +
                        "Add new items and categories and see the graphs calculate itself.")
                .setBackgroundColor(Color.parseColor("#eda03b"))
                .setDrawable(R.drawable.graphs_onboarding)
                .setSummary("View awesome stock graphs")
                .build());

        //Setting fragment data and building screen for app explanations screen 5
        addFragment(new Step.Builder().setTitle("In app product scanning")
                .setContent("WBF (Willies Bait & Fishing Tackle) really went the extra mile and added there own in-app barcode as well as QR " +
                        "code scanner. Simply navigate to our user friendly dashboard, click on 'Scan Product' and hover the device over either " +
                        "a QR code or barcode. This will scan the code directly onto your device for item or product referencing. The best part " +
                        "is that you will have it by hand when ever needed the most.")
                .setBackgroundColor(Color.parseColor("#eda03b"))
                .setDrawable(R.drawable.scan_onboarding)
                .setSummary("Scan your products now!")
                .build());
    }

    //Start new activity after ending user on-boarding
    @Override
    public void finishTutorial() {
        startActivity(new Intent(this, wbf_main.class));
    }

    @Override
    public void currentFragmentPosition(int position) {

    }
}
