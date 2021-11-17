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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostViewActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    RecyclerView recyclerView;
    private PostAdapter.OnItemListener onItemListener;
    DatabaseReference database, userDatabase;
    PostAdapter mainAdapter;
    ArrayList<Post> PostList;
    ArrayList<User> UserList, users;
    FirebaseUser cUser;
    String uId;
    String title;
    String message;
    ArrayList<String> hobbies = new ArrayList<String>();
    String mTitle;
    String mMessage;
    User tmpUsr;
    StorageReference storageReference;
    StorageReference photoReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        drawerLayout = findViewById(R.id.drawer_layout);

        // Assign variable
        recyclerView = findViewById(R.id.PostRecyclerView);
        database = FirebaseDatabase.getInstance().getReference("Post");
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        cUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = cUser.getUid();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        storageReference = FirebaseStorage.getInstance().getReference();
        photoReference = storageReference.child("users/" + uId + "/profile.jpg");

        PostList = new ArrayList<>();
        UserList = new ArrayList<>();
        users = new ArrayList<>();

        onItemListener = new PostAdapter.OnItemListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                intent.putExtra("firstName", UserList.get(position).getFirstName());
                intent.putExtra("lastName", UserList.get(position).getLastName());
                intent.putExtra("email", UserList.get(position).getEmail());
                intent.putExtra("city", UserList.get(position).getAddress());
                intent.putExtra("hobby1", UserList.get(position).getHobby1());
                intent.putExtra("hobby2", UserList.get(position).getHobby2());
                intent.putExtra("hobby3", UserList.get(position).getHobby3());
                intent.putExtra("userID", UserList.get(position).getuserID());
//                intent.putExtra("profile_pic", )
//                intent.putExtra("uId", UserList.get(position).getuserID());
                startActivity(intent);
//                Navigation.redirectActivity(PostViewActivity.this, UserProfileActivity.class);
            }
        };

        mainAdapter = new PostAdapter(this, PostList, onItemListener);
        recyclerView.setAdapter(mainAdapter);

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);

                    if (user.getFirstName() != null) {
                        users.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                title = snapshot.child(uId).child("title").getValue(String.class);
                message = snapshot.child(uId).child("message").getValue(String.class);

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Post post = dataSnapshot.getValue(Post.class);

                    for (User usr : users) {
                        if(usr.userID.equals(dataSnapshot.getKey())){
                            tmpUsr = new User(usr.firstName, usr.lastName, usr.email, usr.hobby1, usr.hobby2, usr.hobby3, usr.address);
                        }
                    }

                    mTitle = post.getTitle();
//                    if(mTitle != null && mTitle.equals(title))
//                        continue;
                    mMessage = post.getMessage();

                    if( mTitle != null && mMessage != null ) {
                        PostList.add(post);
                        UserList.add(tmpUsr);
                    }
                }

                mainAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void goToPost(View view) {
        Navigation.redirectActivity(PostViewActivity.this, PostActivity.class);
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