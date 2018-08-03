package com.example.user.blogga.Activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.blogga.Adapters.MessageAdapter;
import com.example.user.blogga.Models.MessageModel;
import com.example.user.blogga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private ImageButton back_button, send_message_buttton;
    private EditText message_edit_text;
    private Toolbar main_toolbar;
    private DatabaseReference dbase_ref;
    private FirebaseAuth m_auth;
    private String other_user_u_id, final_date;
    private TextView chat_user_displayname, chat_user_online;
    private CircleImageView chat_user_thumb;
    private DatabaseReference chat_dbase_ref;
    private RecyclerView message_list_view;

    public List<MessageModel> model_list;
    private MessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        main_toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);

        dbase_ref = FirebaseDatabase.getInstance().getReference("Users");
        m_auth = FirebaseAuth.getInstance();
        other_user_u_id = getIntent().getStringExtra("u_id");
        message_edit_text = findViewById(R.id.message_edit_text);
        model_list = new ArrayList<>();
        message_list_view = findViewById(R.id.message_recycler);
        message_list_view.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        message_list_view.setHasFixedSize(true);

        adapter = new MessageAdapter(ChatActivity.this, model_list);
        message_list_view.setAdapter(adapter);

        chat_user_displayname = findViewById(R.id.user_chat_name);
        chat_user_online = findViewById(R.id.user_chat_status);
        chat_user_thumb = findViewById(R.id.user_chat_image);
        back_button = findViewById(R.id.back_to_main);
        chat_dbase_ref = FirebaseDatabase.getInstance().getReference("Chats");
        send_message_buttton = findViewById(R.id.send_message_button);


        chat_dbase_ref.child(m_auth.getCurrentUser().getUid()).child(other_user_u_id).keepSynced(true);
        chat_dbase_ref.keepSynced(true);
        chat_dbase_ref.child(m_auth.getCurrentUser().getUid()).keepSynced(true);

        setup_chat_room();
        stupid_shit();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, MainActivity.class));
            }
        });

        final DatabaseReference user_database = dbase_ref.child(m_auth.getCurrentUser().getUid());
        user_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    user_database.child("online").onDisconnect().setValue("Offline");
                    user_database.child("online").setValue("Online");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



      send_message_buttton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             String message = message_edit_text.getText().toString();
              if(!TextUtils.isEmpty(message)){
                  send_message(message);
              }
          }
      });

    }

    public void send_message(final String message){

        message_edit_text.setText("");

        long date1 = System.currentTimeMillis();
        SimpleDateFormat asdf = new SimpleDateFormat("hh:mm:ss");
        final String final_date = asdf.format(date1);

        if(final_date != null){
            final DatabaseReference chat_ref = chat_dbase_ref.child(m_auth.getCurrentUser().getUid()).child(other_user_u_id).push();
            chat_ref.child("Message").setValue(message);
            chat_ref.child("current_user_uid").setValue(m_auth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        chat_ref.child("time_stamp").setValue(final_date).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    DatabaseReference other_user_chat_ref = chat_dbase_ref.child(other_user_u_id).child(m_auth.getCurrentUser().getUid()).push();
                                    other_user_chat_ref.child("Message").setValue(message);
                                    other_user_chat_ref.child("current_user_uid").setValue(m_auth.getCurrentUser().getUid());
                                    other_user_chat_ref.child("time_stamp").setValue(final_date).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                                stupid_shit();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }else{
                        Toast.makeText(ChatActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }else{
            Toast.makeText(this, "Chairman, time stamp is null", Toast.LENGTH_SHORT).show();   
        }
    }

    public void setup_chat_room(){
        DatabaseReference other_user_dbase_ref = dbase_ref.child(other_user_u_id);
        other_user_dbase_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.child("Display Name").getValue().toString();
                String user_status = dataSnapshot.child("online").getValue().toString();
                String user_thumb = dataSnapshot.child("Thumbnails").getValue().toString();

                chat_user_displayname.setText(user_name);
                chat_user_online.setText(user_status);
                Picasso.get()
                        .load(user_thumb)
                        .placeholder(R.drawable.placeholder)
                        .into(chat_user_thumb);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void stupid_shit(){
        DatabaseReference dbase_to_check = chat_dbase_ref.child(m_auth.getCurrentUser().getUid()).child(other_user_u_id);
        dbase_to_check.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                model_list.clear();

                if(dataSnapshot.child("Message").exists() && dataSnapshot.child("current_user_uid").exists() && dataSnapshot.child("time_stamp").exists()){

                    final String message = dataSnapshot.child("Message").getValue().toString();
                    final String current_user_uid = dataSnapshot.child("current_user_uid").getValue().toString();
                    final String current_time = dataSnapshot.child("time_stamp").getValue().toString();
//
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(current_user_uid).child("Thumbnails").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String thumb_nails = dataSnapshot.getValue().toString();

                            MessageModel new_model = new MessageModel(message, current_user_uid, current_time, thumb_nails);
                            model_list.add(new_model);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
