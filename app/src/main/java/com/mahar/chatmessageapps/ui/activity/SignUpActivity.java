package com.mahar.chatmessageapps.ui.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mahar.chatmessageapps.R;
import com.squareup.picasso.Picasso;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class SignUpActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLauncherForUploadImage;
    private CircleImageView profileImage;
    private TextInputEditText inputEmail,inputPassword,inputUsername;
    private Button signupButton;
    boolean imageControl=false;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        RegisterActivityForUploadImage();

        profileImage=findViewById(R.id.profile_username);
        inputEmail=findViewById(R.id.inputEmailSignUp);
        inputPassword=findViewById(R.id.inputPasswordSignUp);
        inputUsername=findViewById(R.id.inputUsernameSignUp);
        signupButton=findViewById(R.id.SignUpButton);

        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=inputEmail.getText().toString();
                String password=inputPassword.getText().toString();
                String username=inputUsername.getText().toString();

                if(email.isEmpty()||password.isEmpty()||username.isEmpty()) return;
                else if(password.length()<=5)
                    Toast.makeText(SignUpActivity.this, "minimum length of password is 6", Toast.LENGTH_SHORT).show();
                else signup(email,password,username);

            }
        });
    }

    public void signup(String email,String password, String username){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.i("reference",reference.toString());
                            reference.child("Users").child(auth.getUid()).child("userName").setValue(username);
                            if(imageControl){
                                Log.i("imageControl","diawal imageControl");
                                UUID randomId=UUID.randomUUID();
                                String imageName = "images/"+ randomId + " - "+username+".jpg";
                                storageReference.child(imageName).putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Log.i("imageControl","put file success");
                                        StorageReference myStorageRef=firebaseStorage.getReference(imageName);
                                        myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Log.i("imageControl","download file success");
                                                String imagePath=uri.toString();
                                                reference.child("Users").child(auth.getUid()).child("image").setValue(imagePath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(SignUpActivity.this,"Image send to database successful",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }else{
                                Log.i("imageControl","no imageControl");
                                reference.child("Users").child(auth.getUid()).child("image").setValue("null");

                            }
                            Intent i= new Intent(SignUpActivity.this, HomePageActivity.class);
                            i.putExtra("userName",username);
                            startActivity(i);
                            finish();
                        }else Toast.makeText(SignUpActivity.this, "cant sign up", Toast.LENGTH_SHORT).show();
                    }
        });
    }

    public void choosePhoto() {
        if (ContextCompat.checkSelfPermission(SignUpActivity.this
                , Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SignUpActivity.this
                    ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{

            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherForUploadImage.launch(i);
        }
    }

    public void RegisterActivityForUploadImage(){
        activityResultLauncherForUploadImage= registerForActivityResult(new ActivityResultContracts
                .StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                int resultCode=result.getResultCode();
                Intent data=result.getData();

                if (resultCode==RESULT_OK && data!=null){
                    selectedImage=data.getData();
                    Picasso.get().load(selectedImage).into(profileImage);
                    imageControl=true;
                }else{
                    imageControl=false;
                }

            }
        });
    }

}