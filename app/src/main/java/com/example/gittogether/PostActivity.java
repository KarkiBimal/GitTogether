package com.example.gittogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.text.TextUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post);
//
//        drawerLayout = findViewById(R.id.drawer_layout);
//    }

    private EditText editTextTitle, editTextMessage;
    private Button postToFeedbtn;
    private FirebaseUser currentUser;
    private DatabaseReference userRef;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;

    private static final String POSTS="Post";

    Post post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        databaseReference = FirebaseDatabase.getInstance().getReference("Post");

        editTextTitle = (EditText) findViewById(R.id.edit_post_title);
        editTextMessage = (EditText) findViewById(R.id.edit_post_message);
        postToFeedbtn = (Button) findViewById(R.id.button_post);

        post = new Post();

        postToFeedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting text from our edittext fields.
                String title = editTextTitle.getText().toString();
                String message = editTextMessage.getText().toString();

                // below line is for checking weather the
                // edittext fields are empty or not.
                if (TextUtils.isEmpty(title) && TextUtils.isEmpty(message)) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(PostActivity.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                } else {
                    // else call the method to add
                    // data to our database.
                    addPostToFirebase(title, message);
                }
            }
        });
    }

    private void addPostToFirebase(String title, String message) {
        // below 3 lines of code is used to set
        // data in our object class.
        post.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
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

//    private void postToFeed(){
//        String title=editTextTitle.getText().toString().trim();
//        String message=editTextMessage.getText().toString().trim();
//
//        if(FirebaseAuth.getInstance().getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//        }
//
//        database = FirebaseDatabase.getInstance();
//        userRef = database.getReference(POSTS);
//
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                Post post = new Post(currentUser.getUid(), title, message);
//
//                FirebaseDatabase.getInstance().getReference(POSTS)
//                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(PostActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(),ProfilePage.class));
//                        }
//                        else{
//                            Toast.makeText(PostActivity.this, "Post Unsuccessful", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        });
//    }

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