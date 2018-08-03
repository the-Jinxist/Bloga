package com.example.user.blogga.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.user.blogga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private RelativeLayout error_layout, loading_layout;
    private NestedScrollView post_view;

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


        ref = FirebaseDatabase.getInstance().getReference("Events").child("FUTA");

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        final String key = appLinkData.getLastPathSegment();

        Toast.makeText(this, key, Toast.LENGTH_LONG).show();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(key).exists()){
                    //TODO ADD LOADING IMAGES AND STUFF!
                    error_layout.setVisibility(View.GONE);
                    loading_layout.setVisibility(View.GONE);
                    post_view.setVisibility(View.VISIBLE);

                }else{
                    error_layout.setVisibility(View.VISIBLE);
                    loading_layout.setVisibility(View.GONE);
                    post_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
