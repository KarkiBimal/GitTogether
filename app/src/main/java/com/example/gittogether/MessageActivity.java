package com.example.gittogether;

import static com.example.gittogether.R.*;
import static com.example.gittogether.R.layout.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class MessageActivity extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;

    public class ChatMessage {

        private String messageText;
        private String messageUser;
        private long messageTime;

        public ChatMessage(String messageText, String messageUser) {
            this.messageText = messageText;
            this.messageUser = messageUser;

            // Initialize to current time
            messageTime = new Date().getTime();
        }

        public ChatMessage(){

        }

        public String getMessageText() {
            return messageText;
        }

        public void setMessageText(String messageText) {
            this.messageText = messageText;
        }

        public String getMessageUser() {
            return messageUser;
        }

        public void setMessageUser(String messageUser) {
            this.messageUser = messageUser;
        }

        public long getMessageTime() {
            return messageTime;
        }

        public void setMessageTime(long messageTime) {
            this.messageTime = messageTime;
        }
        private void displayChatMessages() {
            ListView listOfMessages = (ListView)findViewById(id.list_of_messages);

            adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                    activity_message,FirebaseDatabase.getInstance().getReference() ) {
                @Override
                protected void populateView(View v, ChatMessage model, int position) {
                    // Get references to the views of message.xml
                    TextView messageText = (TextView)v.findViewById(id.message_text);
                    TextView messageUser = (TextView)v.findViewById(id.message_user);
                    TextView messageTime = (TextView)v.findViewById(id.message_time);

                    // Set their text
                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());

                    // Format the date before showing it
                    messageTime.setText((CharSequence) DateFormat.getDateTimeInstance());
                }
            };

            listOfMessages.setAdapter(adapter);

        }

    }



    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_message);


        FloatingActionButton fab =
                (FloatingActionButton)findViewById(id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );

                // Clear the input
                input.setText("");
            }
        });

        drawerLayout = findViewById(id.drawer_layout);
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
        Navigation.redirectActivity(this, PostActivity.class);
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