package com.example.reiten;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Customer extends AppCompatActivity {

    private static final int REQUEST_WRITE_PERMISSION = 786;
    ImageButton pickimage;
    ImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        imageview = findViewById(R.id.imageview);
        pickimage = findViewById(R.id.pickimage);
        pickimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IF THE ANDROID SDK UP TO MARSMALLOW BUILD NUMBER
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //START REQUEST PERMISSION
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
                } else {
                    //ELSE BELOW START OPEN PICKER
                    CropImage.startPickImageActivity(Customer.this);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //THIS IS HAPPEN WHEN USER CLICK ALLOW ON PERMISSION
            //START PICK IMAGE ACTIVITY
            CropImage.startPickImageActivity(Customer.this);
        }
    }


    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            Log.i("RESPONSE getPath", imageUri.getPath());
            Log.i("RESPONSE getScheme", imageUri.getScheme());
            Log.i("RESPONSE PathSegments", imageUri.getPathSegments().toString());

            //NOW CROP IMAGE URI
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(true)
                    //REQUEST COMPRESS SIZE
                    .setRequestedSize(800, 800)
                    //ASPECT RATIO, DELETE IF YOU NEED CROP ANY SIZE
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Log.i("RESPONSE getUri", result.getUri().toString());

                //GET CROPPED IMAGE URI AND PASS TO IMAGEVIEW
                imageview.setImageURI(result.getUri());
            }
        }
    }
}