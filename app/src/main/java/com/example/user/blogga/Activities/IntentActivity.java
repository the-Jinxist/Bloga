package com.example.user.blogga.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.blogga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class IntentActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private TextView intent_act_post_user_name, intent_act_post_user_post_date, intent_act_post_title,
            intent_act_post_desc, intent_act_post_location, intent_act_post_time;
    private ImageView intent_act_post_image;
    private CircleImageView intent_act_post_user_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

        ref = FirebaseDatabase.getInstance().getReference("Events").child("FUTA");

        intent_act_post_user_name = findViewById(R.id.intent_act_poster_username);
        intent_act_post_user_post_date = findViewById(R.id.intent_act_poster_time);
        intent_act_post_title = findViewById(R.id.intent_act_post_title);
        intent_act_post_desc = findViewById(R.id.intent_act_post_desc);
        intent_act_post_location = findViewById(R.id.intent_act_post_location);
        intent_act_post_time = findViewById(R.id.intent_act_post_time);
        intent_act_post_image = findViewById(R.id.intent_act_post_image);
        intent_act_post_user_image = findViewById(R.id.intent_act_poster_image);


        Window win = getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if(getIntent() != null){
            Intent  intent = getIntent();
            if(intent.getStringExtra("key") != null){
                String key = intent.getStringExtra("key");

                ref.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("event_title").exists() && dataSnapshot.child("event_location").exists() && dataSnapshot.child("event_date").exists()
                                && dataSnapshot.child("u_id").exists() && dataSnapshot.child("event_image").exists()
                                ){

                            final String event_title = dataSnapshot.child("event_title").getValue().toString();
                            final String event_location = dataSnapshot.child("event_location").getValue().toString();
                            final String event_date = dataSnapshot.child("event_date").getValue().toString();
                            final String u_id = dataSnapshot.child("u_id").getValue().toString();
                            final String event_desc = dataSnapshot.child("event_desc").getValue().toString();
                            final String post_key = dataSnapshot.getKey();
                            final String post_time = dataSnapshot.child("timestamp").getValue().toString();
                            final String event_image = dataSnapshot.child("event_image").getValue().toString();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                            ref.child(u_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String user_name = dataSnapshot.child("Display Name").getValue().toString();
                                    String user_image = dataSnapshot.child("Thumbnails").getValue().toString();

                                    intent_act_post_user_name.setText(user_name);
                                    intent_act_post_title.setText(event_title);
                                    intent_act_post_user_post_date.setText(event_date);
                                    intent_act_post_location.setText(event_location);
                                    intent_act_post_desc.setText(event_desc);
                                    intent_act_post_time.setText(post_time);

                                    Picasso.get()
                                            .load(event_image)
                                            .placeholder(R.drawable.placeholderblogimage)
                                            .into(intent_act_post_image);

                                    Picasso.get()
                                            .load(user_image)
                                            .placeholder(R.drawable.placeholder)
                                            .into(intent_act_post_user_image);



                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }



        }
    }
}
