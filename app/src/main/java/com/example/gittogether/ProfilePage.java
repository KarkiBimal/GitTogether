package com.example.gittogether;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;

public class ProfilePage extends AppCompatActivity {
    private Button updatePic;
    private TextView changePic, uploadPic;
    private DatabaseReference userRef;
    private StorageReference storageReference;
    private StorageReference photoReference;
    private FirebaseDatabase database;
    private ImageView profilePicture;
    DrawerLayout drawerLayout;


    TextView userEmail, name, lastname, address, hobby1, hobby2, hobby3;
    private static final String USERS="Users";
    String email;
    FirebaseUser cUser;
    String uId;
    Bitmap bmImg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        //assign variable
        drawerLayout = findViewById(R.id.drawer_layout);

        userEmail=(TextView) findViewById(R.id.email);
        name=(TextView) findViewById(R.id.name);
        address=(TextView) findViewById(R.id.address_1);
        hobby1=(TextView) findViewById(R.id.hobby1);
        hobby2=(TextView) findViewById(R.id.hobby2);
        hobby3=(TextView) findViewById(R.id.hobby3);
        cUser= FirebaseAuth.getInstance().getCurrentUser();
        uId=cUser.getUid();
        database= FirebaseDatabase.getInstance();
        userRef=database.getReference(USERS);
        profilePicture = findViewById(R.id.profile_pic);
        updatePic = (Button) findViewById(R.id.updatePicButton);
        storageReference = FirebaseStorage.getInstance().getReference();
        photoReference= storageReference.child("users/" + uId + "/profile.jpg");

//        profileImage = findViewById(R.id.profile_pic);
//        changePic = (TextView) findViewById(R.id.change_pic);
//        uploadPic = (TextView) findViewById(R.id.upload_pic);

        userEmail=(TextView) findViewById(R.id.email);
        name=(TextView) findViewById(R.id.name);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePicture.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child(uId).child("firstName").getValue(String.class));
                userEmail.setText(dataSnapshot.child(uId).child("email").getValue(String.class));
                address.setText(dataSnapshot.child(uId).child("address").getValue(String.class));
                hobby1.setText(dataSnapshot.child(uId).child("hobby1").getValue(String.class));
                hobby2.setText(dataSnapshot.child(uId).child("hobby2").getValue(String.class));
                hobby3.setText(dataSnapshot.child(uId).child("hobby3").getValue(String.class));
                Log.i(TAG, "onDataChange:"+ dataSnapshot.child(uId).child("email").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updatePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open Gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //profilePicture.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to Firebase Storage
        final StorageReference fileRef = storageReference.child("users/" + uId +"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final long ONE_MEGABYTE = 1024 * 1024;
                photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        profilePicture.setImageBitmap(bmp);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                    }
                });
                Toast.makeText(ProfilePage.this, "Image Uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilePage.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ClickMenu(View view){
        //open drawer
        Navigation.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        //close drawer
        Navigation.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        //Redirect activity to home
        Navigation.redirectActivity(this, MainActivity.class);
    }

    public void ClickPost(View view){
        //Redirect activity to home
        Navigation.redirectActivity(this, PostActivity.class);
    }

    public void ClickMessage(View view){
        //Redirect activity to Messages
        Navigation.redirectActivity(this, MessageActivity.class);
    }

    public void ClickProfile(View view){
        //recreate activity
        recreate();
    }

    public void ClickLogout(View view){
        //close app
        Navigation.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        Navigation.closeDrawer(drawerLayout);
    }
}
