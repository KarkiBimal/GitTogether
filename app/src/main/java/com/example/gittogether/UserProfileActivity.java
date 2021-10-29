package com.example.gittogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    private TextView changePic, uploadPic;
    DrawerLayout drawerLayout;


    TextView userEmail, name, lastname, address, hobby1, hobby2, hobby3;
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

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userEmail.setText(extras.getString("email"));
            name.setText(extras.getString("firstName"));
            lastname.setText(extras.getString("lastName"));
            address.setText(extras.getString("city"));
            hobby1.setText(extras.getString("hobby1"));
            hobby2.setText(extras.getString("hobby2"));
            hobby3.setText(extras.getString("hobby3"));
        }
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
