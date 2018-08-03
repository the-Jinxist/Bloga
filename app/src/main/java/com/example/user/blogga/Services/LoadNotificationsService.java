package com.example.user.blogga.Services;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.user.blogga.R;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class LoadNotificationsService extends JobService {
    DatabaseReference reference;
    private final static String CHANNEL_ID = "notify_shit";
    @Override
    public boolean onStartJob(JobParameters job) {

        reference = FirebaseDatabase.getInstance().getReference("Events");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    if(snap.child("event_name").exists() && snap.child("event_date").exists()){
                        String event_name = snap.child("event_name").getValue().toString();
                        String event_date = snap.child("event_date").getValue().toString();

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_support)
                                .setContentTitle("New Event Posted!")
                                .setContentText(event_name)
                                .setContentText(event_date)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        notificationManager.notify(123, mBuilder.build());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
