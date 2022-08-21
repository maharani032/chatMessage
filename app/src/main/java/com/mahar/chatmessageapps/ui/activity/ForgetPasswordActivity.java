package com.mahar.chatmessageapps.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.mahar.chatmessageapps.R;

public class ForgetPasswordActivity extends AppCompatActivity {
    private TextInputEditText inputEmail;
    private Button resetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        inputEmail=findViewById(R.id.inputEmailReset);
        resetButton=findViewById(R.id.resetButton);

    }
}