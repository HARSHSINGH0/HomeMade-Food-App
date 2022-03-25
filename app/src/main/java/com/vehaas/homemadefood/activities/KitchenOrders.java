package com.vehaas.homemadefood.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vehaas.homemadefood.R;

import java.util.HashMap;

public class KitchenOrders extends AppCompatActivity{
    //ui views
    private ImageView img_fdimg;
    private EditText edt_fdname,edt_fdDesc,fdStyle,fdStock,fdPrice;
    private Spinner spinner_food_timing_activity;
    private Button add_food_button;

    //permission constants
    private static  final int  CAMERA_REQUEST_CODE=200;
    private static  final int  STORAGE_REQUEST_CODE=300;
    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE=400;
    private static final int IMAGE_PICK_CAMERA_CODE=500;
    //permission arrays
    private String[] cameraPermission;
    private String[] storagePermission;
    private Uri image_uri;
    private FirebaseAuth fAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_orders);
        //init ui views
        img_fdimg=findViewById(R.id.img_fdimg);
        edt_fdname=findViewById(R.id.edt_fdname);
        edt_fdDesc=findViewById(R.id.edt_fdDesc);
        fdStyle=findViewById(R.id.fdStyle);
        fdStock=findViewById(R.id.fdStock);
        fdPrice=findViewById(R.id.fdPrice);
        spinner_food_timing_activity=findViewById(R.id.spinner_food_timing_activity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.food_timing_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_food_timing_activity.setAdapter(adapter);
        add_food_button=findViewById(R.id.add_food_button);

        img_fdimg.setImageResource(R.drawable.ic_baseline_fastfood_24);

        fAuth=FirebaseAuth.getInstance();


        //setup progress dialog
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //init  permission arrays
        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //

        img_fdimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show dialog to pic image
                showImagePickDialog();
            }
        });
        add_food_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Flow:
                //1) Input data
                //2) validate data
                //3) Add data to db
                inputData();
            }
        });
        //will add filter feature afterwards --harsh

    }





    private String foodName,foodDesc,dishStyle,foodQuantity,originalPrice,timing;

    private void inputData() {
        //1) Input data
        foodName=edt_fdname.getText().toString().trim();
        foodDesc=edt_fdDesc.getText().toString().trim();
        dishStyle=fdStyle.getText().toString().trim();
        foodQuantity=fdStock.getText().toString().trim();
        originalPrice=fdPrice.getText().toString().trim();
        timing=spinner_food_timing_activity.getSelectedItem().toString().trim();
        //2)validate data
        if(TextUtils.isEmpty(foodName)){
            Toast.makeText(this,"Food name is required....",Toast.LENGTH_SHORT).show();
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(foodDesc)){
            Toast.makeText(this,"Food Description is required....",Toast.LENGTH_SHORT).show();
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(dishStyle)){
            Toast.makeText(this,"Dish style is required....",Toast.LENGTH_SHORT).show();
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(foodQuantity)){
            Toast.makeText(this,"Specify quantity....",Toast.LENGTH_SHORT).show();
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(originalPrice)){
            Toast.makeText(this,"Price is required....",Toast.LENGTH_SHORT).show();
            return;//don't proceed further
        }
        addFood();

    }

    private void addFood() {
        //3) Add data to db
        progressDialog.setMessage("Adding Product.....");
        progressDialog.show();
        final String timestamp=""+System.currentTimeMillis();

        if(image_uri==null){
            //upload without image

            //setup data to uploadz
            HashMap<String,Object>hashMap=new HashMap<>();
            hashMap.put("foodId",timestamp);
            hashMap.put("foodName",foodName);
            hashMap.put("foodDesc",foodDesc);
            hashMap.put("dishStyle",dishStyle);
            hashMap.put("foodQuantity",foodQuantity);
            hashMap.put("foodIcon","");//no image
            hashMap.put("originalPrice",originalPrice);
            hashMap.put("timing",timing);
            hashMap.put("timestamp",timestamp);
            hashMap.put("userID",fAuth.getUid());
            //add data to db
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("kitchen");
            reference.child(fAuth.getUid()).child("foods").child(timestamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //added to db
                            progressDialog.dismiss();
                            Toast.makeText(KitchenOrders.this,"Product added...",Toast.LENGTH_SHORT).show();
                            clearData();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //failed adding to db
                    progressDialog.dismiss();
                    Toast.makeText(KitchenOrders.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            //upload with image
            //first upload image to storage
            //name and path of image to be uploaded
            String filePathAndName="food_images/"+""+timestamp;
            StorageReference storageReference= FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image uploaded
                            //get url of uploaded image
                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadImageUri=uriTask.getResult();
                            if(uriTask.isSuccessful()){
                                //url of image received,upload to db
                                //setup data to upload
                                HashMap<String,Object> hashMap=new HashMap<>();
                                hashMap.put("foodId",""+timestamp);
                                hashMap.put("foodName",""+foodName);
                                hashMap.put("foodDesc",""+foodDesc);
                                hashMap.put("dishStyle",""+dishStyle);
                                hashMap.put("dishStyle",""+dishStyle);
                                hashMap.put("foodQuantity",""+foodQuantity);
                                hashMap.put("foodIcon",""+downloadImageUri);//no image, set empty
                                hashMap.put("originalPrice",""+originalPrice);
                                hashMap.put("timing",""+timing);
                                hashMap.put("timestamp",""+timestamp);
                                hashMap.put("userID",""+fAuth.getUid());
                                // add to db
                                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("kitchen");
                                reference.child(fAuth.getUid()).child("foods").child(timestamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //added to db
                                                progressDialog.dismiss();
                                                Toast.makeText(KitchenOrders.this,"Product added...",Toast.LENGTH_SHORT).show();
                                                clearData();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //failed adding to db
                                        progressDialog.dismiss();
                                        Toast.makeText(KitchenOrders.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    });

        }
    }
    private void clearData(){
        //clear data after uploading product
        edt_fdname.setText("");
        edt_fdDesc.setText("");
        fdStyle.setText("");
        fdStock.setText("");
        fdPrice.setText("");
        img_fdimg.setImageResource(R.drawable.ic_baseline_fastfood_24);
        image_uri=null;
    }

    private void showImagePickDialog() {
        //options to display in dialog
        String[] options={"Camera","Gallery"};
        //dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item clicks
                        if(which==0){
                            //camera clicked
                            if(checkCameraPermission()) {
                                //permission granted
                                pickFromCamera();
                            }
                            else{
                                //permission not granted,request
                                requestCameraPermission();
                            }
                        }
                        else{
                            //gallery clicked
                            if(checkStoragePermission()){
                                //permission granted
                                pickFromGallery();
                            }
                            else{
                                //permission not granted,request
                                requestStoragePermission();
                            }
                        }
                    }
                }).show();
    }
    private void pickFromGallery(){
        //intent to pick image from gallery
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }
    private void pickFromCamera(){
        //intent to pick image from camera

        //using media store to pick high/original quality image
        ContentValues contentValues=new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Temp_Image_Description");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);
    }
    private boolean checkStoragePermission(){
        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return result; //returns true or false
    }
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        boolean result=ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE);
    }
    //handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        //both permission granted
                        pickFromCamera();
                    }
                    else{
                        //both or one permission denied
                        Toast.makeText(this,"Storage permission is required....",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean storageAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        //permission granted
                        pickFromGallery();
                    }else {
                        //permission denied
                        Toast.makeText(this,"Storage permission is required....",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //handle image pick resuls;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode==RESULT_OK){
            if(resultCode==IMAGE_PICK_GALLERY_CODE){
                //image picked from gallery
                //save picked image uri
                image_uri=data.getData();
                //set image
                img_fdimg.setImageURI(image_uri);

            }
            else if(resultCode==IMAGE_PICK_CAMERA_CODE){
                //image picked from camera
                img_fdimg.setImageURI(image_uri);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void add_food_button(View view) {

    }

    public void backbutton_aftk(View view) {
        startActivity(new Intent(getApplicationContext(), KitchenFood.class));
    }

}