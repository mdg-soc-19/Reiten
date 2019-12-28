package com.example.reiten;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reiten.Common.Common;
import com.example.reiten.Model.Rider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class Customer extends AppCompatActivity {
    public static final String TAG = "TAG";
    private static final int REQUEST_WRITE_PERMISSION = 786;
    ImageButton pickimage;
    ImageView imageview;
    String userID;
    static int PReqCode = 1;
    static int RequesCode = 1;
    FirebaseDatabase db;
    DatabaseReference users;
    Uri pickedImgUri;
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    private EditText mname, mbhawan;
    private ProgressBar loadingProgress;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        imageview = findViewById(R.id.imageview);
        pickimage = findViewById(R.id.pickimage);
        fStore = FirebaseFirestore.getInstance();
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
        mname = findViewById(R.id.editText12);
        mbhawan = findViewById(R.id.editText33);
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Common.user_rider_tbl);
        loadingProgress = findViewById(R.id.progressBar2);
        submit = findViewById(R.id.button37);

        loadingProgress.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    loadingProgress.setVisibility(View.VISIBLE);
                    final String name = mname.getText().toString();
                    final String bhawan = mbhawan.getText().toString();
                    final String ig = pickedImgUri.toString();


                    if (TextUtils.isEmpty(name)) {
                        mname.setError("Name is Required.");
                        return;
                    }

                    if (TextUtils.isEmpty(bhawan)) {
                        mbhawan.setError("Bhawan name is Required.");
                        return;
                    }

                    userID = mAuth.getCurrentUser().getUid();
                    final FirebaseUser currentUser = mAuth.getCurrentUser();
                    StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("customer_photos_"+name);
                    final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name)
                                            .setPhotoUri(uri).build();

                                    currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                            }


                                        }
                                    });

                                }
                            });
                        }
                    });
                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", name);
                    user.put("Bhawan",bhawan);
                    user.put("Imageuri",ig);
                    Rider rider = new Rider();
                    rider.setName(name);
                    users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .updateChildren(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                    DocumentReference documentReference = fStore.collection("Customers").document(name+userID);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                            startActivity(new Intent(Customer.this, Home.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                            loadingProgress.setVisibility(View.GONE);
                        }
                    });

                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
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
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Log.i("RESPONSE getUri", result.getUri().toString());

                //GET CROPPED IMAGE URI AND PASS TO IMAGEVIEW
                pickedImgUri=result.getUri();
                if (pickedImgUri!=null)
                imageview.setImageURI(pickedImgUri);
            }
        }
    }
}