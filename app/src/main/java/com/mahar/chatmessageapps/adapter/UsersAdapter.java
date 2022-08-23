package com.mahar.chatmessageapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahar.chatmessageapps.R;
import com.mahar.chatmessageapps.ui.activity.MyChatActivity;
import com.squareup.picasso.Picasso;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    List<String> userList;
    String userName;
    Context mContext;

    FirebaseDatabase database;
    DatabaseReference reference;

    public UsersAdapter(List<String> userList, String userName, Context mContext) {
        this.userList = userList;
        this.userName = userName;
        this.mContext = mContext;

        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
    }

    public UsersAdapter(List<String> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        reference.child("Users").child(userList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String otherName=snapshot.child("userName").getValue().toString();
                String imageURL=snapshot.child("image").getValue().toString();

                holder.textViewUsers.setText(otherName);

                if(imageURL.equals("null")){
                    holder.imageViewUsers.setImageResource(R.drawable.ic_baseline_account_circle_24);
                }else{
                    Picasso.get().load(imageURL).into(holder.imageViewUsers);
                }

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(mContext, MyChatActivity.class);
                        i.putExtra("userName",userName);
                        i.putExtra("otherName",otherName);
                        mContext.startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewUsers;
        private CircleImageView imageViewUsers;
        private CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsers=itemView.findViewById(R.id.textViewUsers);
            imageViewUsers=itemView.findViewById(R.id.imageViewUsers);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }

}
