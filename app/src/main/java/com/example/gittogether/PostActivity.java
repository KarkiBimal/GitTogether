package com.example.gittogether;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class PostActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextMessage;
    private Button uploadImageBtn;
    private DatabaseReference databaseReference;
    DrawerLayout drawerLayout;

    private StorageReference storageReference;
    FirebaseUser cUser;
    String uId;

    private static final String POSTS="Post";

    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        drawerLayout = findViewById(R.id.drawer_layout);
        databaseReference = FirebaseDatabase.getInstance().getReference(POSTS);

        // upload image references
        storageReference = FirebaseStorage.getInstance().getReference();
        cUser= FirebaseAuth.getInstance().getCurrentUser();
        uId = cUser.getUid();

        editTextTitle = (EditText) findViewById(R.id.edit_post_title);
        editTextMessage = (EditText) findViewById(R.id.edit_post_message);
        uploadImageBtn = (Button) findViewById(R.id.upload_image_btn);

        post = new Post();

        //When user clicks Upload Picture button
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
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
                //Save URI of chosen image
                Uri imageUri = data.getData();
                uploadImageToFirebase(imageUri);
            }
        }
    }

    public void post(View view) {
        // getting text from our edittext fields.
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        // below line is for checking weather the
        // edittext fields are empty or not.
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(message)) {
            // if the text fields are empty
            // then show the below message.
            Toast.makeText(PostActivity.this, "Please add some data.", Toast.LENGTH_SHORT).show();
        } else {
            // else call the method to add
            // data to our database.
            addPostToFirebase(title, message);
            Navigation.redirectActivity(PostActivity.this, MainActivity.class);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to Firebase Storage
        final StorageReference fileRef = storageReference.child("users/" + uId +"/post.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PostActivity.this, "Image Uploaded.", Toast.LENGTH_SHORT).show();
                //Go back to profile page when uploading finished
//                Navigation.redirectActivity(PostActivity.this, ProfilePage.class);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPostToFirebase(String title, String message) {
        // below 3 lines of code is used to set
        // data in our object class.
        post.setTitle(title);
        post.setMessage(message);

        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
//                databaseReference.setValue(post);
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(post);

                // after adding this data we are showing toast message.
                Toast.makeText(PostActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(PostActivity.this, "Post Unsuccessful " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    ***************************************************************
    Drawer Menu
     **************************************************************
     */
    public void ClickMenu(View view){
        //open drawer
        Navigation.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        //close drawer
        Navigation.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        //Redirect activity to Home
        Navigation.redirectActivity(this, MainActivity.class);
    }

    public void ClickPost(View view){
        //Recreate activity
        recreate();
    }

    public void ClickMessage(View view){
        //Redirect activity to Messages
        Navigation.redirectActivity(this, MessageActivity.class);
    }

    public void ClickProfile(View view){
        //Redirect activity to Profile
        Navigation.redirectActivity(this, ProfilePage.class);
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