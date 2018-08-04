package com.example.user.blogga.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.blogga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private RelativeLayout error_layout, loading_layout;
    private NestedScrollView post_view;
    private TextView event_poster_name, event_desc_name, event_desc_time, event_desc_desc, event_desc_loacation;
    private ImageView event_desc_event_image;
    private CircleImageView event_desc_user_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Post");

        error_layout = findViewById(R.id.error_layout);
        post_view = findViewById(R.id.post_nested_view);
        loading_layout = findViewById(R.id.loading_layout);

        event_poster_name = findViewById(R.id.event_poster_name);
        event_desc_name = findViewById(R.id.event_desc_name);
        event_desc_time = findViewById(R.id.event_desc_time);
        event_desc_desc = findViewById(R.id.event_desc_desc);
        event_desc_loacation = findViewById(R.id.event_desc_location);
        event_desc_event_image = findViewById(R.id.event_desc_event_image);
        event_desc_user_image = findViewById(R.id.event_desc_user_image);

        ref = FirebaseDatabase.getInstance().getReference("Events").child("FUTA");

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        if(appLinkData != null){
            final String key = appLinkData.getLastPathSegment();

            if(key != null){
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
                           final String post_key = dataSnapshot.getKey();
                           final String post_time = dataSnapshot.child("timestamp").getValue().toString();
                           final String event_image = dataSnapshot.child("event_image").getValue().toString();

                           DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                           ref.child(u_id).addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   String user_name = dataSnapshot.child("Display Name").getValue().toString();
                                   String user_image = dataSnapshot.child("Thumbnails").getValue().toString();

                                   event_poster_name.setText(user_name);
                                   event_desc_name.setText(event_title);
                                   event_desc_time.setText(event_date);
                                   event_desc_loacation.setText(event_location);

                                   Picasso.get()
                                           .load(event_image)
                                           .placeholder(R.drawable.placeholderblogimage)
                                           .into(event_desc_event_image);

                                   Picasso.get()
                                           .load(user_image)
                                           .placeholder(R.drawable.placeholder)
                                           .into(event_desc_user_image);



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
