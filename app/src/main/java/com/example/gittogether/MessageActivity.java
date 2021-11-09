package com.example.gittogether;

import static com.example.gittogether.R.*;
import static com.example.gittogether.R.layout.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

        LinearLayout layout;
        ImageView sendButton;
        EditText messageArea;
        ScrollView scrollView;
        DrawerLayout drawerLayout;
        DatabaseReference database;
        FirebaseUser cUser;
        String uId;
        String userName;
        String currentU;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(activity_message);
            database = FirebaseDatabase.getInstance().getReference("Users");
            cUser = FirebaseAuth.getInstance().getCurrentUser();
            currentU = cUser.getDisplayName();
            //Log.d("tag", currentU);
            uId = cUser.getUid();

            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {

                    currentU = snapshot.child(uId).child("firstName").getValue(String.class);
                    currentU = currentU + "_" + snapshot.child(uId).child("lastName").getValue(String.class);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Bundle extras = getIntent().getExtras();

            userName = extras.getString("userName");

            layout = (LinearLayout)findViewById(R.id.layout1);
            sendButton = (ImageView)findViewById(R.id.sendButton);
            messageArea = (EditText)findViewById(R.id.messageArea);
            scrollView = (ScrollView)findViewById(R.id.scrollView);
            Firebase reference1;
            Firebase reference2;
            Firebase.setAndroidContext(this);
            reference1 = new Firebase("https://gittogether-13f65-default-rtdb.firebaseio.com/Message/" + currentU +  "-" + userName);
            reference2 = new Firebase("https://gittogether-13f65-default-rtdb.firebaseio.com/Message/" + userName + "-" + currentU);


            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String messageText = messageArea.getText().toString();

                    if(!messageText.equals("")){
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("message", messageText);
                        map.put("user", currentU);
                        reference1.push().setValue(map);
                        reference2.push().setValue(map);
                    }
                }
            });

            reference1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map map = dataSnapshot.getValue(Map.class);
                    String message = map.get("message").toString();
                    String userName = map.get("user").toString();

                    if(userName.equals(User.class.getName())){
                        addMessageBox("You:\n" + message, 1);
                    }
                    else{
                        addMessageBox(/*User.class.chatWith + */ ":-\n" + message, 2);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        public void addMessageBox(String message, int type){
            TextView textView = new TextView(MessageActivity.this);
            textView.setText(message);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 10);
            textView.setLayoutParams(lp);

            if(type == 1) {
                textView.setBackgroundResource(R.drawable.rounded_corner1);
            }
            else{
                textView.setBackgroundResource(R.drawable.rounded_corner2);
            }

            layout.addView(textView);
            scrollView.fullScroll(View.FOCUS_DOWN);
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
        //Redirect activity to Post
        Navigation.redirectActivity(this, PostViewActivity.class);
    }
    public void ClickMessage(View view){
        //Recreate activity
        recreate();
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