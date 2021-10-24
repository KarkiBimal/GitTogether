package com.example.gittogether;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import android.widget.TextView;
//import com.google.firebase.auth.AuthResult;
//import android.util.Log;
//import static android.content.ContentValues.TAG;

public class PostFeed extends AppCompatActivity implements View.OnClickListener {
//public class PostFeed extends AppCompatActivity {

    private EditText editTextTitle, editTextMessage;
    private Button postToFeed;
    private FirebaseUser currentUser;
    private DatabaseReference userRef;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
//    private FirebaseAuth mAuth;

    private static final String POSTS="Post";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_layout_test);

//        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        postToFeed = (Button) findViewById(R.id.post_button);
        postToFeed.setOnClickListener(this);

        editTextTitle = (EditText) findViewById(R.id.post_title_text);
        editTextTitle.setOnClickListener(this);

        editTextMessage = (EditText) findViewById(R.id.post_message_text);
        editTextMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        switch(view.getId()){
//            case R.id.button_post:
//                postToFeed();
//                break;
//        }
    }

    private void postToFeed(){
        String title=editTextTitle.getText().toString().trim();
        String message=editTextMessage.getText().toString().trim();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(POSTS);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Post post = new Post(currentUser.getUid(), title, message);

                FirebaseDatabase.getInstance().getReference(POSTS)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PostFeed.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),ProfilePage.class));
                        }
                        else{
                            Toast.makeText(PostFeed.this, "Post Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}
