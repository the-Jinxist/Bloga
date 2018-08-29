package com.example.user.blogga.Services;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.user.blogga.Activities.IntentActivity;
import com.example.user.blogga.Activities.MainActivity;
import com.example.user.blogga.Activities.PostActivity;
import com.example.user.blogga.Models.NotificationModel;
import com.example.user.blogga.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class Notification extends Service {
    DatabaseReference ref;
    Bitmap notification_display;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(23)
    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        final List<NotificationModel> modelList = new ArrayList<>();



        ref = FirebaseDatabase.getInstance().getReference("Events").child("FUTA");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.child("event_title").exists() && dataSnapshot.child("event_date").exists() && dataSnapshot.child("event_image_thumb").exists()){
                    String event_name = dataSnapshot.child("event_title").getValue().toString();
                    String event_desc = dataSnapshot.child("event_desc").getValue().toString();
                    String event_key = dataSnapshot.getKey();

                    Intent start_post_activity = new Intent(getApplicationContext(), IntentActivity.class);
                    start_post_activity.putExtra("key", event_key);

                    final PendingIntent intent_ = PendingIntent.getActivity(getApplicationContext(), 123, start_post_activity
                            , PendingIntent.FLAG_CANCEL_CURRENT);

                    //Might use it one day.. Maybe loading the event pic into the big picture notification style
                    //    :)
                    String event_image_thumb = dataSnapshot.child("event_image_thumb").getValue().toString();

                    NotificationModel model = new NotificationModel(event_name, event_desc);

                    if(!modelList.contains(model)){

                        NotificationCompat.Builder notifiction = new NotificationCompat.Builder(getApplicationContext());
                        notifiction.setContentTitle(model.getNotification_title());
                        notifiction.setContentText(model.getNotification_desc());
                        notifiction.setDefaults(android.app.Notification.DEFAULT_ALL);
                        notifiction.setPriority(android.app.Notification.PRIORITY_DEFAULT);
                        notifiction.setSmallIcon(R.drawable.ic_support);
                        notifiction.setContentIntent(intent_);


                        NotificationManager manager = getApplicationContext().getSystemService(NotificationManager.class);
                        manager.notify(123, notifiction.build());

                        modelList.add(model);
                    }

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





        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
