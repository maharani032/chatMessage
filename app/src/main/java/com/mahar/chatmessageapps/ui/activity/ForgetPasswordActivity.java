package com.mahar.chatmessageapps.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.mahar.chatmessageapps.R;

public class ForgetPasswordActivity extends AppCompatActivity {
    private TextInputEditText inputEmail;
    private Button resetButton;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        inputEmail=findViewById(R.id.inputEmailReset);
        resetButton=findViewById(R.id.resetButton);

        auth=FirebaseAuth.getInstance();
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=inputEmail.getText().toString();

                if(!email.isEmpty()) passwordReset(email);
            }
        });
    }
    public void passwordReset(String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetPasswordActivity.this,"Please check ur mail"
                            ,Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(ForgetPasswordActivity.this,"Your email not register"
                            ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}