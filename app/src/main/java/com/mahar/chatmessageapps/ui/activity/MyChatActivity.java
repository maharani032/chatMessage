package com.mahar.chatmessageapps.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahar.chatmessageapps.R;

public class MyChatActivity extends AppCompatActivity {
    private ImageView imageViewBack;
    private TextView textViewChat;
    private EditText inputMessage;
    private FloatingActionButton fab;
    private RecyclerView rvChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chat);

        imageViewBack=findViewById(R.id.imageViewBack);
        textViewChat=findViewById(R.id.textViewChat);
        inputMessage=findViewById(R.id.inputMessage);
        fab=findViewById(R.id.fab);
        rvChat=findViewById(R.id.rvChat);
    }
}