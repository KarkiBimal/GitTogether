package com.example.gittogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private Button button, signUp, logIn;
    private TextView forgotPassword;
    private FirebaseAuth auth;
    private EditText inputEmail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        inputEmail = (EditText) findViewById(R.id.et_email);
        inputPassword = (EditText) findViewById(R.id.et_password);

        auth = FirebaseAuth.getInstance();



        signUp = (Button) findViewById(R.id.bt_signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        }
        );



        logIn = (Button) findViewById(R.id.bt_login);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

                if(email.isEmpty()){
                    inputEmail.setError("Please enter an email.");
                    inputEmail.requestFocus();
                    return;
                }

                if(password.isEmpty()){
                    inputPassword.setError("Please enter a password.");
                    inputPassword.requestFocus();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Invalid email and password combination.", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.TOP, 0, 0);
                                    toast.show();
                                } else {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Failed to Login" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }

                        });
            }
        });




        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialogue = new AlertDialog.Builder(view.getContext());
                passwordResetDialogue.setTitle("Reset Password?");
                passwordResetDialogue.setMessage("Enter Your Email To Receive Reset Link.");
                passwordResetDialogue.setView(resetMail);

                passwordResetDialogue.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail= resetMail.getText().toString();
                    auth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(LoginActivity.this, "Rest Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this,"Error! Reset link is not sent."+e.getMessage(),Toast.LENGTH_SHORT);
                        }
                    });
                    }
                });
                passwordResetDialogue.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                passwordResetDialogue.create().show();
            }
        });




    }
}
