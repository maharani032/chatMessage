package com.mahar.chatmessageapps.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mahar.chatmessageapps.MainActivity;
import com.mahar.chatmessageapps.R;


public class SignInActivity extends AppCompatActivity {
    private TextInputEditText inputEmail,inputPassword;
    private Button buttonSignIn,navSignUp;
    private TextView forgetId;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        inputEmail=findViewById(R.id.inputEmail);
        inputPassword=findViewById(R.id.inputPassword);
        buttonSignIn=findViewById(R.id.SignInButton);
        navSignUp=findViewById(R.id.navSignUp);
        forgetId=findViewById(R.id.forgetId);

        auth=FirebaseAuth.getInstance();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=inputEmail.getText().toString();
                String password=inputPassword.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()) signIn(email,password);
                else if(password.length()<=5) Toast.makeText(SignInActivity.this,"Minimum length of password",Toast.LENGTH_SHORT).show();
                else Toast.makeText(SignInActivity.this,"Please Input Your Email and Password",Toast.LENGTH_SHORT).show();

            }
        });
        navSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navSignUp= new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(navSignUp);
            }
        });
        forgetId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navForget= new Intent(SignInActivity.this,ForgetPasswordActivity.class);
                startActivity(navForget);
            }
        });
    }

    public void signIn(String email,String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Intent intent= new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(SignInActivity.this,"Sign In not Successful",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}