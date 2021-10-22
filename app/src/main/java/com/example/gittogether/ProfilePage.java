package com.example.gittogether;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class ProfilePage extends AppCompatActivity {
    private TextView changePic, uploadPic;
    private DatabaseReference userRef;
    private FirebaseDatabase database;


    TextView userEmail, name, lastname, address, hobby1, hobby2, hobby3;
    private static final String USERS="Users";
    String email;
    FirebaseUser cUser;
    String uId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

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

//        profileImage = findViewById(R.id.profile_pic);
//        changePic = (TextView) findViewById(R.id.change_pic);
//        uploadPic = (TextView) findViewById(R.id.upload_pic);

        userEmail=(TextView) findViewById(R.id.email);
        name=(TextView) findViewById(R.id.name);

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

    }

}
