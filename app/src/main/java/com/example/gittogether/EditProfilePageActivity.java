package com.example.gittogether;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditProfilePageActivity extends AppCompatActivity {
    EditText etFName, etLName, etAddress, etEmail, etHobby1, etHobby2, etHobby3;
    Button buttonPic;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef;
    private StorageReference storageReference;
    private StorageReference photoReference;
    private static final String USERS="Users";
    FirebaseUser cUser;
    String uId, fName, lName, address, email, hobby1, hobby2, hobby3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        //Assigning variables to corresponding layout elements
        etFName = (EditText) findViewById(R.id.et_first_name_up);
        etLName = (EditText) findViewById(R.id.et_last_name_up);
        etAddress = (EditText) findViewById(R.id.et_address_up);
        etEmail = (EditText) findViewById(R.id.et_email_up);
        etHobby1 = (EditText) findViewById(R.id.et_hobby_1_up);
        etHobby2 = (EditText) findViewById(R.id.et_hobby_2_up);
        etHobby3 = (EditText) findViewById(R.id.et_hobby_3_up);
        buttonPic = (Button) findViewById(R.id.btn_pic);
        cUser= FirebaseAuth.getInstance().getCurrentUser();
        uId=cUser.getUid();
        database= FirebaseDatabase.getInstance();
        userRef=database.getReference(USERS);
        storageReference = FirebaseStorage.getInstance().getReference(); //reference to Firebase storage as a whole
        photoReference= storageReference.child("users/" + uId + "/profile.jpg"); //path to profile picture

        //Filling in Text Fields with current User Data
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                etFName.setText(dataSnapshot.child(uId).child("firstName").getValue(String.class));
                etLName.setText(dataSnapshot.child(uId).child("lastName").getValue(String.class));
                etEmail.setText(dataSnapshot.child(uId).child("email").getValue(String.class));
                etAddress.setText(dataSnapshot.child(uId).child("address").getValue(String.class));
                etHobby1.setText(dataSnapshot.child(uId).child("hobby1").getValue(String.class));
                etHobby2.setText(dataSnapshot.child(uId).child("hobby2").getValue(String.class));
                etHobby3.setText(dataSnapshot.child(uId).child("hobby3").getValue(String.class));
                Log.i(TAG, "onDataChange:"+ dataSnapshot.child(uId).child("email").getValue(String.class));

                //Saving starting value of text fields for later comparison
                fName = etFName.getText().toString();
                lName = etLName.getText().toString();
                address = etAddress.getText().toString();
                email = etEmail.getText().toString();
                hobby1 = etHobby1.getText().toString();
                hobby2 = etHobby2.getText().toString();
                hobby3 = etHobby3.getText().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //When user clicks Upload Picture button
        buttonPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open Gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == RESULT_OK && data != null){
            //Save URI of chosen image
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to Firebase Storage
        final StorageReference fileRef = storageReference.child("users/" + uId +"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditProfilePageActivity.this, "Image Uploaded.", Toast.LENGTH_SHORT).show();
                //Go back to profile page when uploading finished
                Navigation.redirectActivity(EditProfilePageActivity.this, ProfilePage.class);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfilePageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void update(View view){
        //if any of the fields have been changed
        if(isFNameChanged() || isLNameChanged() || isAddressChanged() || isHobby1Changed() || isHobby2Changed() || isHobby3Changed()){
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Data is the same and can not be updated", Toast.LENGTH_LONG).show();
        }
        Navigation.redirectActivity(EditProfilePageActivity.this, ProfilePage.class);
    }

    private boolean isFNameChanged() {
        if(!fName.equals(etFName.getText().toString())){
            //setting new value in database
            userRef.child(uId).child("firstName").setValue(etFName.getText().toString());
            fName = etFName.getText().toString();
            //still checking other fields if FName did have changes
            isLNameChanged();
            isAddressChanged();
            isHobby1Changed();
            isHobby2Changed();
            isHobby3Changed();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isLNameChanged(){
        if(!lName.equals(etLName.getText().toString())){
            userRef.child(uId).child("lastName").setValue(etLName.getText().toString());
            lName = etLName.getText().toString();
            isAddressChanged();
            isHobby1Changed();
            isHobby2Changed();
            isHobby3Changed();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isAddressChanged(){
        if(!address.equals(etAddress.getText().toString())){
            userRef.child(uId).child("address").setValue(etAddress.getText().toString());
            address = etAddress.getText().toString();
            isHobby1Changed();
            isHobby2Changed();
            isHobby3Changed();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isHobby1Changed(){
        if(!hobby1.equals(etHobby1.getText().toString())){
            userRef.child(uId).child("hobby1").setValue(etHobby1.getText().toString());
            hobby1 = etHobby1.getText().toString();
            isHobby2Changed();
            isHobby3Changed();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isHobby2Changed(){
        if(!hobby2.equals(etHobby2.getText().toString())){
            userRef.child(uId).child("hobby2").setValue(etHobby2.getText().toString());
            hobby2 = etHobby2.getText().toString();
            isHobby3Changed();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isHobby3Changed(){
        if(!hobby3.equals(etHobby3.getText().toString())){
            userRef.child(uId).child("hobby3").setValue(etHobby3.getText().toString());
            hobby3 = etHobby3.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }
}