package com.example.user.blogga.Activities;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.blogga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

public class UserProfile extends AppCompatActivity {
    private DatabaseReference info, friend_req_database, friend_database, user_info;
    private FirebaseUser current_user;
    private StorageReference profile_image;
    private ImageView user_profile_picture;
    private String profile_user_uid;
    private TextView user_profile_displayname, user_profile_status;
    private Button request_btn, decline_btn;
    private String mCurrent_status;
    private ProgressDialog dialog;
//    private Toolbar user_profile_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        user_profile_picture = findViewById(R.id.user_profile_image);
        user_profile_displayname = findViewById(R.id.user_profile_displayname);
        user_profile_status = findViewById(R.id.user_profile_status);
        request_btn = findViewById(R.id.request_button);
        decline_btn = findViewById(R.id.decline_button);
        profile_image = FirebaseStorage.getInstance().getReference();
//        user_profile_bar = findViewById(R.id.user_profile_toolbar);

//        setSupportActionBar(user_profile_bar);
//
//        ActionBar bar = getSupportActionBar();
//        bar.setDisplayHomeAsUpEnabled(true);
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        dialog = new ProgressDialog(UserProfile.this);


        decline_btn.setEnabled(false);
        decline_btn.setVisibility(View.INVISIBLE);

        info = FirebaseDatabase.getInstance().getReference("Users");
        friend_req_database = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        profile_user_uid = getIntent().getStringExtra("u_id");
        friend_database = FirebaseDatabase.getInstance().getReference().child("Friends");


          update_user_details();
          update_current_status();

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void update_user_details(){
        dialog.setTitle("Initializing..");
        dialog.setMessage("Getting User details...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        user_info = info.child(profile_user_uid);

        if(profile_user_uid.equals(current_user.getUid())){
            user_info.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String status = dataSnapshot.child("Status").getValue().toString();
                    user_profile_displayname.setText("You");
                    user_profile_status.setText(status);

                    request_btn.setVisibility(View.INVISIBLE);
                    decline_btn.setVisibility(View.INVISIBLE);

                    if(dataSnapshot.child("Profile Image").getValue().toString().equals("default")) {
                        Picasso.get()
                                .load(R.drawable.placeholder)
                                .into(user_profile_picture);
                    }else{
                        StorageReference profile_image_path = profile_image.child("profile_images").child(profile_user_uid);
                        profile_image_path.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Picasso.get().load(task.getResult()).placeholder(R.drawable.placeholder).into(user_profile_picture);
                            }
                        });
                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{

            user_info.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    String user_display = dataSnapshot.child("Display Name").getValue().toString();
                    String user_status = dataSnapshot.child("Status").getValue().toString();

                    user_profile_displayname.setText(user_display);
                    user_profile_status.setText(user_status);



                    dialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    dialog.dismiss();
                    Toast.makeText(UserProfile.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }



    public void update_current_status(){
        mCurrent_status = "not_friends";
        dialog.setTitle("Checking connections");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("PLease wait while we verify friendship or any kind of relationship. We don't judge :)");
        dialog.show();
//        Toast.makeText(UserProfile.this, mCurrent_status, Toast.LENGTH_LONG).show();
        friend_req_database.child(current_user.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(profile_user_uid)){
                    String request_type = dataSnapshot.child(profile_user_uid).child("Request Type").getValue().toString();
//                    Toast.makeText(UserProfile.this, request_type, Toast.LENGTH_LONG).show();
                   if(request_type.equals("Sent")){
                        request_btn.setText("Cancel friend request");
                        mCurrent_status = "request sent";
                        request_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancel_friend_request();
                            }
                        });
                       decline_btn.setVisibility(View.INVISIBLE);
                        dialog.dismiss();


                    }else if(request_type.equals("Received")){

                        request_btn.setText("Accept friend request?");
                        request_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                accept_request();
                            }
                        });
                        decline_btn.setEnabled(true);
                        decline_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                decline_request();
                            }
                        });
                       decline_btn.setVisibility(View.VISIBLE);
                        mCurrent_status = "request received";
                        dialog.dismiss();
                    }
                    dialog.dismiss();

                }else{
                    friend_database.child(current_user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(profile_user_uid)){
                                mCurrent_status = "Friends";
                                request_btn.setText("UnFriend?");
                                request_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        unfriend();
                                    }
                                });
                                decline_btn.setVisibility(View.INVISIBLE);
                                dialog.dismiss();
                            }else{
                                mCurrent_status = "not_friends";
                                decline_btn.setVisibility(View.INVISIBLE);
                                request_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        send_request();
                                    }
                                });
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(UserProfile.this, databaseError.toString() , Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    });

                    dialog.dismiss();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserProfile.this, databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void accept_request(){
        DatabaseReference new_friend_ref = friend_database.child(current_user.getUid()).child(profile_user_uid);
        final String current_date = DateFormat.getDateTimeInstance().format(new Date());
        new_friend_ref.setValue(current_date).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    friend_database.child(profile_user_uid).child(current_user.getUid()).setValue(current_date).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                friend_req_database.child(current_user.getUid()).child(profile_user_uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            friend_req_database.child(profile_user_uid).child(current_user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    mCurrent_status = "Friends";
                                                    request_btn.setText("UnFriend");
                                                }
                                            });
                                        }
                                    }
                                });

                            }
                        }
                    });

                }
            }
        });
    }

    public void decline_request(){
                if(mCurrent_status.equals("request received")){
                    friend_req_database.child(current_user.getUid()).child(profile_user_uid).child("Request Type").removeValue().addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        friend_req_database.child(profile_user_uid).child(current_user.getUid()).child("Request Type").removeValue().addOnCompleteListener(
                                                new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            request_btn.setText("Send Friend Request");
                                                            request_btn.setEnabled(true);
                                                            mCurrent_status = "not_friends";
                                                            decline_btn.setEnabled(false);

                                                            Toast.makeText(UserProfile.this, "Friend Request successfully rescinded", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                }
                                        );
                                    }else{
                                        Toast.makeText(UserProfile.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                    );
         }

    }

    public void send_request(){
        friend_req_database.child(current_user.getUid()).child(profile_user_uid).child("Request Type").setValue("Sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    friend_req_database.child(profile_user_uid).child(current_user.getUid()).child("Request Type").setValue("Received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            request_btn.setEnabled(true);
                            request_btn.setText("Cancel friend request");
                            mCurrent_status = "request sent";
                            Toast.makeText(UserProfile.this, "Friend Request Sent Successfully!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(UserProfile.this, "Friend Request Failed. Please try again...", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    public void cancel_friend_request(){
        friend_req_database.child(current_user.getUid()).child(profile_user_uid).child("Request Type").removeValue().addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            friend_req_database.child(profile_user_uid).child(current_user.getUid()).child("Request Type").removeValue().addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                request_btn.setText("Send Friend Request");
                                                request_btn.setEnabled(true);
                                                mCurrent_status = "not_friends";

                                                Toast.makeText(UserProfile.this, "Friend Request successfully rescinded", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                            );
                        }else{
                            Toast.makeText(UserProfile.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
    }
    public void unfriend(){
        friend_database.child(current_user.getUid()).child(profile_user_uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                friend_database.child(profile_user_uid).child(current_user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mCurrent_status = "not_friends";
                            request_btn.setText("Send Friend Request");
                            decline_btn.setEnabled(false);
                        }
                    }
                })     ;
            }
        });
    }
}
