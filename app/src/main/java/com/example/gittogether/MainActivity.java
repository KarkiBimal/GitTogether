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

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private MainAdapter.OnItemListener onItemListener;
    DatabaseReference database;
    MainAdapter mainAdapter;
    ArrayList<User> list;
    FirebaseUser cUser;
    String uId;
    String cUserCity;
    String hobby1;
    String hobby2;
    String hobby3;
    String email;
    ArrayList<String> hobbies = new ArrayList<String>();
    DrawerLayout drawerLayout;
    String mCity;
    String mHobby1;
    String mHobby2;
    String mHobby3;
    String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.MainRecyclerView);
        database = FirebaseDatabase.getInstance().getReference("Users");
        cUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = cUser.getUid();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database.child(uId).child("userID").setValue(uId);

        list = new ArrayList<>();
        onItemListener = new MainAdapter.OnItemListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                intent.putExtra("firstName", list.get(position).getFirstName());
                intent.putExtra("lastName", list.get(position).getLastName());
                intent.putExtra("email", list.get(position).getEmail());
                intent.putExtra("city", list.get(position).getAddress());
                intent.putExtra("hobby1", list.get(position).getHobby1());
                intent.putExtra("hobby2", list.get(position).getHobby2());
                intent.putExtra("hobby3", list.get(position).getHobby3());
                intent.putExtra("userID",list.get(position).getuserID());
                startActivity(intent);
            }
        };
        mainAdapter = new MainAdapter(this, list, onItemListener);
        recyclerView.setAdapter(mainAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cUserCity = snapshot.child(uId).child("address").getValue(String.class);
                //Log.d("MSG", cUserCity);
                email = snapshot.child(uId).child("email").getValue(String.class);
                //Log.d("MSG", cUserCity);
                hobby1 = snapshot.child(uId).child("hobby1").getValue(String.class);
                //Log.d("MSG", hobby1);
                hobby2 = snapshot.child(uId).child("hobby2").getValue(String.class);
                //Log.d("MSG", hobby2);
                hobby3 = snapshot.child(uId).child("hobby3").getValue(String.class);
                //Log.d("MSG", hobby3);

                hobbies.add(hobby1);
                hobbies.add(hobby2);
                hobbies.add(hobby3);

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    User user = dataSnapshot.getValue(User.class);
                    mEmail = user.getEmail();
                    if(mEmail != null && mEmail.equals(email))
                        continue;
                    mCity = user.getAddress();
                    if(mCity != null){
                        Log.d("MSG", mCity );
                    }
                    mHobby1 = user.getHobby1();
                    mHobby2 = user.getHobby2();
                    mHobby3 = user.getHobby3();

                    if( mCity != null && mCity.equalsIgnoreCase(cUserCity) && ( ( mHobby1 != null && hobbies.contains(mHobby1) ) || ( mHobby2 != null && hobbies.contains(mHobby2) ) || ( mHobby3 != null && hobbies.contains(mHobby3) ) ) ) {
                        list.add(user);
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
        //Recreate activity
        recreate();
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