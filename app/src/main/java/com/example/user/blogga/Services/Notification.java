package com.example.user.blogga.Services;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.user.blogga.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

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

//        new AsyncTask<String, Void, Bitmap>(){
//
//            @Override
//            protected Bitmap doInBackground(String... string) {
//
//            }
//        };


        ref = FirebaseDatabase.getInstance().getReference("Events").child("FUTA");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.child("event_title").exists() && dataSnapshot.child("event_date").exists() && dataSnapshot.child("event_image_thumb").exists()){
                    String event_name = dataSnapshot.child("event_title").getValue().toString();
                    String event_date = dataSnapshot.child("event_date").getValue().toString();
                    String event_image_thumb = dataSnapshot.child("event_image_thumb").getValue().toString();
//
//                    Uri uri = Uri.parse(event_image_thumb);
//                    File file = new File(uri.getPath());
//


//
//                     try {
//
//
//                          notification_display = new Compressor(getApplicationContext())
//                                .setMaxWidth(200)
//                                .setMaxHeight(200)
//                                .setQuality(75)
//                                .compressToBitmap(file);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }



                    NotificationCompat.Builder notifiction = new NotificationCompat.Builder(getApplicationContext());
                    notifiction.setContentTitle(event_name);
                    notifiction.setContentText(event_date);
                    notifiction.setDefaults(android.app.Notification.DEFAULT_ALL);
                   notifiction.setPriority(android.app.Notification.PRIORITY_DEFAULT);
                    notifiction.setSmallIcon(R.drawable.ic_support);

                    NotificationCompat.Style style = new NotificationCompat.BigPictureStyle(notifiction);
                    notifiction.setStyle(style);


                    NotificationManager manager = getApplicationContext().getSystemService(NotificationManager.class);
                    manager.notify(123, notifiction.build());
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
