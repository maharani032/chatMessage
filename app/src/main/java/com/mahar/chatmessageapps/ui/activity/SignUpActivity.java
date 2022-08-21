package com.mahar.chatmessageapps.ui.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.mahar.chatmessageapps.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class SignUpActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLauncherForUploadImage;

    private CircleImageView profileImage;
    private TextInputEditText inputEmail,inputPassword,inputUsername;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        profileImage=findViewById(R.id.profile_image);
        inputEmail=findViewById(R.id.inputEmailSignUp);
        inputPassword=findViewById(R.id.inputPasswordSignUp);
        inputUsername=findViewById(R.id.inputUsernameSignUp);
        signupButton=findViewById(R.id.SignUpButton);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public void choosePhoto() {
        Intent intent=new Intent();
        intent.setType("images/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,1);
        activityResultLauncherForUploadImage.launch(intent);
//        CHECK PERMISION
//        if (ContextCompat.checkSelfPermission(SignUpActivity.this
//                , Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(SignUpActivity.this
//                    ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
//        }else{
//            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            activityResultLauncherForUploadImage.launch(i);
//        }
    }
    public void RegisterActivityForUploadImage(){
        activityResultLauncherForUploadImage= registerForActivityResult(new ActivityResultContracts
                .StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                int resultCode=result.getResultCode();
                Intent data=result.getData();

                if (resultCode==RESULT_OK && data!=null){

//                    selectedImage = data.getData();
//                    uploadPhoto();
//                    Picasso.get().load(selectedImage).into(binding.photoProfil);
//                    uploadFile(selectedImage);
                }
                else{
//                    binding.photoProfil.setClickable(true);
//                    binding.spinnerPhoto.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}