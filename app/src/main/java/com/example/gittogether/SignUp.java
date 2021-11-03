package com.example.gittogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextFirstName,  editTextLastName,  editTextEmail,  editTextPassword, editTextHobby1, editTextHobby2
            , editTextHobby3, editTextAddress;
    private Button registerUser, backLogin;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        registerUser=(Button)findViewById(R.id.bt_register);
        registerUser.setOnClickListener(this);

        backLogin=(Button)findViewById(R.id.b_Login);
        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class)); }
            }
        );

        editTextEmail=(EditText) findViewById(R.id.et_email);
        editTextEmail.setOnClickListener(this);

        editTextFirstName=(EditText) findViewById(R.id.et_first_name);
        editTextFirstName.setOnClickListener(this);

        editTextLastName=(EditText) findViewById(R.id.et_last_name);
        editTextLastName.setOnClickListener(this);

        editTextPassword=(EditText) findViewById(R.id.et_password);
        editTextPassword.setOnClickListener(this);

        editTextHobby1=(EditText) findViewById(R.id.et_hobby_1);
        editTextHobby1.setOnClickListener(this);

        editTextHobby2=(EditText) findViewById(R.id.et_hobby_2);
        editTextHobby2.setOnClickListener(this);

        editTextHobby3=(EditText) findViewById(R.id.et_hobby_3);
        editTextHobby3.setOnClickListener(this);

        editTextAddress=(EditText) findViewById(R.id.et_address);
        editTextAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bt_register:
                registerUser();
                break;
        }
    }

    private void registerUser(){

        String email=editTextEmail.getText().toString().trim();
        String firstName=editTextFirstName.getText().toString().trim();
        String lastName=editTextLastName.getText().toString().trim();
        String hobby1=editTextHobby1.getText().toString().trim();
        String hobby2=editTextHobby2.getText().toString().trim();
        String hobby3=editTextHobby3.getText().toString().trim();
        String address=editTextAddress.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();


        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),SignUp.class));
        }

        if(firstName.isEmpty()){
            editTextFirstName.setError("Please enter your First name.");
            editTextFirstName.requestFocus();
            return;
        }
        if(lastName.isEmpty()){
            editTextLastName.setError("Please enter your Last name.");
            editTextLastName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Please enter your Email.");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Your email does not match.");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Please enter a Password.");
            editTextPassword.requestFocus();
            return;
        }

        if(hobby1.isEmpty()){
            editTextHobby1.setError("Please enter a Hobby.");
            editTextHobby1.requestFocus();
            return;
        }

        if(hobby2.isEmpty()){
            editTextHobby2.setError("Please enter a Hobby.");
            editTextHobby2.requestFocus();
            return;
        }

        if(hobby3.isEmpty()){
            editTextHobby3.setError("Please enter a Hobby.");
            editTextHobby3.requestFocus();
            return;
        }

        if(address.isEmpty()){
            editTextAddress.setError("Please enter your City.");
            editTextAddress.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user=new User(firstName, lastName, email, hobby1,hobby2, hobby3, address );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUp.this, "User Registered", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                    }
                                    else if(!task.isSuccessful()){
                                        //startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                        Toast.makeText(SignUp.this, "User Not Registered", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                    }
                                }
                            });
                        }else{
                            startActivity(new Intent(getApplicationContext(),SignUp.class));
                        }
                    }
                });


    }

}