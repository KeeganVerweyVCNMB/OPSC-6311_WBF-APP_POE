package com.example.opsctask2app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class wbf_add_item extends AppCompatActivity{
    //Setting Variables
    private Button uploadBtn, openCam;
    private ImageView uploadedImg;
    private ProgressBar progressBarAddItem;
    private Spinner spnCategories;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String Code[];
    String CatIdAdd;
    String ItemNameAdd;
    String ItemDescAdd;
    String StockAmountAdd;
    String PriceAdd;
    String DateFoundAdd;

    EditText ItemName, ItemDesc, StockAmount, Price, DateFound;

    DatabaseReference dbRef;
    DatabaseReference fRef;
    ArrayList<String> arrayLstAddItem = new ArrayList<>();

    //Adding Firebase instance into root variable
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Items");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbf_add_item);

        //Calling UI into variables
        StockAmount = findViewById(R.id.txtAmount);
        Price = findViewById(R.id.txtPrice);
        //Setting numeric input for users
        //stackoverflow.com. 2021. How do you set the edit text keyboard to only consist of number on android: Android Tutorial.
        StockAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        Price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        StockAmount.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        Price.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        openCam = findViewById(R.id.btnOpenCam);
        uploadBtn = findViewById(R.id.btnAddNewItem);
        progressBarAddItem = findViewById(R.id.progressBarAddItem);
        uploadedImg = findViewById(R.id.imgAddItem);

        progressBarAddItem.setVisibility(View.VISIBLE);
        //Calling action bar and setting page description
        getSupportActionBar().setTitle("WBF Add Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Loading firebase categories into array adapter then into spinner(dropdown)
        //stackoverflow.com. 2021. Set on click listener for spinner item
        ArrayAdapter<String> arrayAdptAddItem = new ArrayAdapter<String>(wbf_add_item.this, android.R.layout.simple_spinner_dropdown_item, arrayLstAddItem);
        spnCategories = (Spinner) findViewById(R.id.spnCategory);
        spnCategories.setAdapter(arrayAdptAddItem);

        spnCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                CatIdAdd = Code[position];
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        //Calling Firebase data for spinner(dropdown) (Blog.mindorks.com. 2021.)
        dbRef = FirebaseDatabase.getInstance().getReference();
        fRef = dbRef.child("Categories");
        Query query = fRef.orderByChild("IsCategory").equalTo("True");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Code = new String[(int)dataSnapshot.getChildrenCount()];
                int index = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map mapStock = new HashMap();
                    mapStock = (HashMap) ds.getValue();
                    Code[index] = mapStock.get("CatID").toString();
                    arrayLstAddItem.add(mapStock.get("CatDesc").toString());
                    index++;
                }
                arrayAdptAddItem.notifyDataSetChanged();
                progressBarAddItem.setVisibility(RelativeLayout.INVISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do Nothing
            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);


        //Getting Android system permission to access camera
        if(ContextCompat.checkSelfPermission(wbf_add_item.this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(wbf_add_item.this, new String[]{
                    Manifest.permission.CAMERA
            },100);
        }

        //Calling and setting android calendar to EditText
        //stackoverflow.com. 2021. How to popup datepicker when click on edit text: Android Tutorial.
        DateFound = findViewById(R.id.txtDate);
        DateFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        wbf_add_item.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        //Setting calendar output for Firebase DB storing
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                DateFound.setText(date);
            }
        };

        //Adding on click to open Android Camera
        openCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,100);
                }
                catch(Exception e) {
                    Toast.makeText(wbf_add_item.this, "Error opening camera", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Adding on click for URI file upload
        uploadedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting user input into variables
                ItemName = findViewById(R.id.txtItemName);
                ItemDesc = findViewById(R.id.txtItemDesc);
                StockAmount = findViewById(R.id.txtAmount);
                Price = findViewById(R.id.txtPrice);
                DateFound = findViewById(R.id.txtDate);

                ItemNameAdd = ItemName.getText().toString();
                ItemDescAdd = ItemDesc.getText().toString();
                StockAmountAdd = StockAmount.getText().toString();
                PriceAdd = Price.getText().toString();
                DateFoundAdd = DateFound.getText().toString();

                //Validation
                if(TextUtils.isEmpty(ItemNameAdd)) {
                    ItemName.setError("Item name required");
                    return;
                }
                if(TextUtils.isEmpty(ItemDescAdd)) {
                    ItemDesc.setError("Item description required");
                    return;
                }
                if(TextUtils.isEmpty(StockAmountAdd)) {
                    StockAmount.setError("Item stock amount required");
                    return;
                }
                if(TextUtils.isEmpty(PriceAdd)) {
                    Price.setError("Item price required");
                    return;
                }
                if(TextUtils.isEmpty(DateFoundAdd)) {
                    DateFound.setError("Item date found required");
                    return;
                }
                if (imageUri != null){
                    uploadToFirebase(imageUri);
                }
                if (imageUri == null){
                    Toast.makeText(wbf_add_item.this, "Please upload an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Calling numeric keyboard
    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }

    //Image URI and bitmap conversion for Bucket storage to Firebase
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //stackoverflow.com. 2021. Bitmap To URI: Android Tutorial.
        if(requestCode == 100) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),bitmap,"val",null);
            imageUri = Uri.parse(path);
            uploadedImg.setImageURI(imageUri);
        }

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            uploadedImg.setImageURI(imageUri);
        }
    }

    //Pushing items to Firebase Realtime DB (Blog.mindorks.com. 2021.)
    private void uploadToFirebase(Uri uri){

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        UploadModel model = new UploadModel(uri.toString());
                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(model);
                        root.child(modelId).child("CatID").setValue(Integer.parseInt(CatIdAdd));
                        root.child(modelId).child("Amount").setValue(Integer.parseInt(StockAmountAdd));
                        root.child(modelId).child("DateFound").setValue(DateFoundAdd);
                        root.child(modelId).child("ItemDesc").setValue(ItemDescAdd);
                        root.child(modelId).child("ItemName").setValue(ItemNameAdd);
                        root.child(modelId).child("Price").setValue(Integer.parseInt(PriceAdd));

                        ItemName = findViewById(R.id.txtItemName);
                        ItemDesc = findViewById(R.id.txtItemDesc);
                        StockAmount = findViewById(R.id.txtAmount);
                        Price = findViewById(R.id.txtPrice);
                        DateFound = findViewById(R.id.txtDate);

                        //Clearing user input if submission is successfull
                        ItemName.setText("");
                        ItemDesc.setText("");
                        StockAmount.setText("");
                        Price.setText("");
                        DateFound.setText("");
                        uploadedImg.setImageResource(R.drawable.upload_img);

                        //stackoverflow.com. 2021. Android progress bar not hiding: Android Tutorial.
                        progressBarAddItem.setVisibility(View.INVISIBLE);
                        Toast.makeText(wbf_add_item.this, "New Item Added", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBarAddItem.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBarAddItem.setVisibility(View.INVISIBLE);
                Toast.makeText(wbf_add_item.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //URI class for image URI file extension
    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
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
            startActivity(new Intent(wbf_add_item.this, wbf_dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }

}




