package com.example.gittogether;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class EmailResetActivity extends AppCompatActivity {

    private TextView forgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_reset);


        forgotPassword=(TextView) findViewById(R.id.forgot_password);

    }
}