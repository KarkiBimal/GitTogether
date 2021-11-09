package com.example.gittogether;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostViewActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    RecyclerView recyclerView;
    private PostAdapter.OnItemListener onItemListener;
    DatabaseReference database;
    PostAdapter mainAdapter;
    ArrayList<Post> list;
    FirebaseUser cUser;
    String uId;
    String title;
    String message;
    ArrayList<String> hobbies = new ArrayList<String>();
    String mTitle;
    String mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        drawerLayout = findViewById(R.id.drawer_layout);

        // Assign variable
        recyclerView = findViewById(R.id.PostRecyclerView);
        database = FirebaseDatabase.getInstance().getReference("Post");
        cUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = cUser.getUid();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        onItemListener = new PostAdapter.OnItemListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), Post.class);
                intent.putExtra("title", list.get(position).getTitle());
                intent.putExtra("message", list.get(position).getMessage());
                startActivity(intent);
            }
        };
        mainAdapter = new PostAdapter(this, list, onItemListener);
        recyclerView.setAdapter(mainAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                title = snapshot.child(uId).child("title").getValue(String.class);
                message = snapshot.child(uId).child("message").getValue(String.class);

                System.out.println("Title of post: " + title);
                System.out.println("Message of post: " + message);


                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Post post = dataSnapshot.getValue(Post.class);
                    mTitle = post.getTitle();
                    if(mTitle != null && mTitle.equals(title))
                        continue;
                    mMessage = post.getMessage();
                    if(mMessage != null){
                        Log.d("MSG", mMessage );
                    }

                    if( mTitle != null && mMessage != null ) {
                        list.add(post);
                    }

                }

                mainAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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