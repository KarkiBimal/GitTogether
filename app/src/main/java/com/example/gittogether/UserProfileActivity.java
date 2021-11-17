package com.example.gittogether;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserProfileActivity extends AppCompatActivity {

    private TextView changePic, uploadPic;
    DrawerLayout drawerLayout;
    private StorageReference storageReference;
    private StorageReference photoReference;
    private ImageView profilePicture;


    TextView userEmail, name, lastname, address, hobby1, hobby2, hobby3;
    String uId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        drawerLayout = findViewById(R.id.drawer_layout);

        userEmail=(TextView) findViewById(R.id.email);
        name=(TextView) findViewById(R.id.name);
        lastname=(TextView) findViewById(R.id.lastName);
        address=(TextView) findViewById(R.id.address_1);
        hobby1=(TextView) findViewById(R.id.hobby1);
        hobby2=(TextView) findViewById(R.id.hobby2);
        hobby3=(TextView) findViewById(R.id.hobby3);
        storageReference = FirebaseStorage.getInstance().getReference();
        profilePicture = findViewById(R.id.profile_pic);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userEmail.setText(extras.getString("email"));
            name.setText(extras.getString("firstName"));
            lastname.setText(extras.getString("lastName"));
            address.setText(extras.getString("city"));
            hobby1.setText(extras.getString("hobby1"));
            hobby2.setText(extras.getString("hobby2"));
            hobby3.setText(extras.getString("hobby3"));
            uId = extras.getString("userID");
        }

        photoReference= storageReference.child("users/" + uId + "/profile.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(2*ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePicture.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });

        Button btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("userName", extras.getString("firstName") + "_" + extras.getString("lastName"));

                startActivity(intent);
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
