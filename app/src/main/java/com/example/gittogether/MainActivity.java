package com.example.gittogether;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView changePic, uploadPic;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    private StorageReference storageProfilePicsRef;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener mAuthListner;

    TextView userEmail, name, lastname;
    private static final String USERS="users";
    String email;
    FirebaseUser cUser;
    String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=getIntent();
        email=intent.getStringExtra("email");
        userEmail=(TextView) findViewById(R.id.email);
        name=(TextView) findViewById(R.id.name);
        cUser=FirebaseAuth.getInstance().getCurrentUser();
        uId=cUser.getUid();
        database= FirebaseDatabase.getInstance();
        userRef=database.getReference(USERS).child(uId);

   mAuthListner=new FirebaseAuth.AuthStateListener() {
       @Override
       public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
           FirebaseUser cUser=firebaseAuth.getCurrentUser();
           if(cUser != null){
               Log.d(TAG, "onAuthStateChanged: "+ cUser.getUid());
           }else{
               Log.d(TAG, "onAuthStateChanged:Signed out ");
           }
       }
   };


//        profileImage = findViewById(R.id.profile_pic);
//        changePic = (TextView) findViewById(R.id.change_pic);
//        uploadPic = (TextView) findViewById(R.id.upload_pic);
//

        //lastname=(TextView) findViewById(R.id.lastName);





        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.i(TAG, "onDataChange:"+ dataSnapshot.child("bob").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//
//        changePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                  chooseImage();
//
//            }
//        });

//       uploadPic.setOnClickListener(new View.OnClickListener() {
//          @Override
//           public void onClick(View v)
//           {
//                uploadImage();
//           }
//        });
    }

//
//    private void chooseImage() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
//                && data != null && data.getData() != null) {
//            filePath = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                profileImage.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    private void uploadImage() {
//        if (filePath != null) {
//
//
//
//            // Defining the child of storageReference
//            DatabaseReference ref
//                    =userRef
//                    .child(
//                            "images/"
//                                    + UUID.randomUUID().toString());
//
//            // adding listeners on upload
//            // or failure of image
//            ref.putFile(filePath)
//                    .addOnSuccessListener(
//                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//                                @Override
//                                public void onSuccess(
//                                        UploadTask.TaskSnapshot taskSnapshot) {
//
//                                    // Image uploaded successfully
//                                    // Dismiss dialog
//
//                                    Toast
//                                            .makeText(MainActivity.this,
//                                                    "Image Uploaded!!",
//                                                    Toast.LENGTH_SHORT)
//                                            .show();
//                                }
//                            })
//
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//
//                            Toast
//                                    .makeText(MainActivity.this,
//                                            "Failed " + e.getMessage(),
//                                            Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    })
//                    .addOnProgressListener(
//                            new OnProgressListener<UploadTask.TaskSnapshot>() {
//
//                                // Progress Listener for loading
//                                // percentage on the dialog box
//                                @Override
//                                public void onProgress(
//                                        UploadTask.TaskSnapshot taskSnapshot) {
//                                    double progress
//                                            = (100.0
//                                            * taskSnapshot.getBytesTransferred()
//                                            / taskSnapshot.getTotalByteCount());
//
//                                }
//                            });
//        }
//    }

}

