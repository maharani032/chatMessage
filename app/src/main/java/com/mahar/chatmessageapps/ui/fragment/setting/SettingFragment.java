package com.mahar.chatmessageapps.ui.fragment.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahar.chatmessageapps.databinding.FragmentSettingBinding;
import com.mahar.chatmessageapps.ui.activity.ProfileUpdateActivity;
import com.mahar.chatmessageapps.ui.activity.SignInActivity;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseReference ref=database.getReference().child("Users").child(auth.getUid());
        Log.i("Database",ref.toString());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName= snapshot.child("userName").getValue().toString();
                String imageUri=snapshot.child("image").getValue().toString();
                if(!imageUri.equals("null")){
                    Uri image=Uri.parse(imageUri);
                    Picasso.get().load(image).into(binding.profileUsername);
                }
                binding.usernameID.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView usernameID = binding.usernameID;
        final CircleImageView profileImage= binding.profileUsername;
        final LinearLayout signoutLayout= binding.SignOutLayout;
        final LinearLayout profileLayout= binding.ProfileLayout;


        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), ProfileUpdateActivity.class);
                startActivity(i);
            }
        });
        signoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i= new Intent(getActivity(), SignInActivity.class);
                startActivity(i);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}