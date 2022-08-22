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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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
import com.mahar.chatmessageapps.R;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUpdateActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLauncherForUploadImage;
    private CircleImageView updateProfileImage;
    private TextInputEditText inputUsername;
    private Button updateProfile;

    Uri selectedImage;
    boolean imageControl=false;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference reference;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        RegisterActivityForUploadImage();
        updateProfile=findViewById(R.id.action_update);
        inputUsername=findViewById(R.id.updateUsername);
        updateProfileImage=findViewById(R.id.update_profile_image);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        getUserInfo();

        updateProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ProfileUpdateActivity.this
                        , Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ProfileUpdateActivity.this
                            ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else{
                    choosePhoto();
                }
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

    }
    public void choosePhoto(){
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncherForUploadImage.launch(i);
    }

    public void getUserInfo(){
        reference.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName= snapshot.child("userName").getValue().toString();
                String imageUri=snapshot.child("image").getValue().toString();
                inputUsername.setText(userName);
                if(!imageUri.equals("null")){
                    Picasso.get().load(imageUri).into(updateProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    Picasso.get().load(selectedImage).into(updateProfileImage);
                    imageControl=true;
                }else{
                    imageControl=false;
                }
            }
        });
    }

    public void updateProfile(){
        String username=inputUsername.getText().toString();
        reference.child("Users").child(auth.getUid()).child("userName").setValue(username);
        if(imageControl){
            Log.i("imageControl","diawal imageControl");
            UUID randomId=UUID.randomUUID();
            String imageName = "images/"+ randomId + " - "+username+".jpg";
            Log.i("imageControl",selectedImage.toString());
            Log.i("imageControl",storageReference.toString());

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
                                    Toast.makeText(ProfileUpdateActivity.this,"Image send to database successful",Toast.LENGTH_LONG).show();
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
        Intent i= new Intent(ProfileUpdateActivity.this, HomePageActivity.class);
        i.putExtra("userName",username);
        startActivity(i);
        finish();
    }
}