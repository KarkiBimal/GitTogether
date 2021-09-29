package com.example.gittogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextFirstName,  editTextLastName,  editTextEmail,  editTextPassword, editTextRepeatPassword;
    private Button registerUser;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        registerUser=(Button)findViewById(R.id.bt_register);
        registerUser.setOnClickListener(this);

        editTextEmail=(EditText) findViewById(R.id.et_email);
        editTextEmail.setOnClickListener(this);

        editTextFirstName=(EditText) findViewById(R.id.et_first_name);
        editTextFirstName.setOnClickListener(this);

        editTextLastName=(EditText) findViewById(R.id.et_last_name);
        editTextLastName.setOnClickListener(this);

        editTextPassword=(EditText) findViewById(R.id.et_password);
        editTextPassword.setOnClickListener(this);

        editTextRepeatPassword=(EditText) findViewById(R.id.et_repeat_password);
        editTextRepeatPassword.setOnClickListener(this);
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
        String password=editTextPassword.getText().toString().trim();
        String repeatPassword=editTextRepeatPassword.getText().toString().trim();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        if(firstName.isEmpty()){
            editTextFirstName.setError("You Idiot....");
            editTextFirstName.requestFocus();
            return;
        }
        if(lastName.isEmpty()){
            editTextLastName.setError("You Idiot....");
            editTextLastName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("You Idiot....");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("You are Super Idiot....");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("You Idiot....");
            editTextPassword.requestFocus();
            return;
        }
        if(repeatPassword.isEmpty()){
            editTextRepeatPassword.setError("You Idiot....");
            editTextRepeatPassword.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user=new User(firstName, lastName, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUp.this, "User Registered", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    }
                                    else{
                                        Toast.makeText(SignUp.this, "User Not Registered", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });


    }

}