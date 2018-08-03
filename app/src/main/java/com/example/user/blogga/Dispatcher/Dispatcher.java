package com.example.user.blogga.Dispatcher;

import android.content.Context;

import com.example.user.blogga.Services.LoadNotificationsService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class Dispatcher {

    synchronized public static void start_notification_service(Context context){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job job =  dispatcher.newJobBuilder()
                   .setService(LoadNotificationsService.class)
                   .setRecurring(false)
                    .setTag("my_job")
                    .setLifetime(Lifetime.FOREVER)
                    .addConstraint(Constraint.ON_ANY_NETWORK)
                    .setReplaceCurrent(true)
                    .build();

        dispatcher.mustSchedule(job);
        dispatcher.schedule(job);
    }

}
