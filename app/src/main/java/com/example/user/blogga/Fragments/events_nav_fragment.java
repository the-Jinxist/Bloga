package com.example.user.blogga.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.blogga.Activities.AddEventsActivity;
import com.example.user.blogga.Adapters.EventsAdapter;
import com.example.user.blogga.Models.EventsModel;
import com.example.user.blogga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class events_nav_fragment extends Fragment {

    private DatabaseReference ref, events_ref;
    private FirebaseAuth auth;
    private Button get_started_button;
    private RelativeLayout get_started_layout, events_layout, select_school_layout, events_loading_layout;
    private EventsAdapter adapter;
    private List<EventsModel> model_list;
    private RecyclerView events_recycler;
    private FloatingActionButton add_events_btn;
    private TextView futa_text;
    private String user_uid;
    private ImageButton close_school_layout;
    private SwipeRefreshLayout swipe_refresh;
    private ProgressBar bar;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_event_fragment, container, false);



        ref = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        events_ref = FirebaseDatabase.getInstance().getReference("Events");
        user_uid = auth.getCurrentUser().getUid();

        get_started_button = view.findViewById(R.id.get_started_button);
        bar = view.findViewById(R.id.progressBar2);
        bar.setVisibility(View.VISIBLE);
        get_started_layout = view.findViewById(R.id.events_getstarted_layout);
        events_layout = view.findViewById(R.id.events_recycler_layout);
        events_recycler = view.findViewById(R.id.events_recycler);
        add_events_btn = view.findViewById(R.id.add_events_btn);
        select_school_layout = view.findViewById(R.id.select_school_layout);
        futa_text = view.findViewById(R.id.futa_events_text);
        close_school_layout = view.findViewById(R.id.close_school_layout);
        swipe_refresh = view.findViewById(R.id.events_swipe_to_refresh);
        events_loading_layout = view.findViewById(R.id.events_loading_layout);

        events_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        events_recycler.setHasFixedSize(true);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                set_up(events_ref.child("FUTA"));
                swipe_refresh.setRefreshing(false);
            }
        });

        ref.child(user_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("school").exists()){
                    events_layout.setVisibility(View.VISIBLE);
                    get_started_layout.setVisibility(GONE);
                    events_loading_layout.setVisibility(GONE);

                    String school = dataSnapshot.child("school").getValue().toString();

                    set_up(events_ref.child(school));

                }else{
                    events_loading_layout.setVisibility(GONE);
                    events_layout.setVisibility(GONE);
                    get_started_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        close_school_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select_school_layout.getVisibility() == View.VISIBLE){
                    select_school_layout.setVisibility(View.GONE);
                }
            }
        });

        futa_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Setting up events feed");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                ref.child(auth.getCurrentUser().getUid()).child("school").setValue("FUTA").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            events_layout.setVisibility(View.VISIBLE);
                            get_started_layout.setVisibility(View.GONE);
                            events_loading_layout.setVisibility(GONE);
                            select_school_layout.setVisibility(View.GONE);

                            set_up(events_ref.child("FUTA"));

                            dialog.dismiss();

                        }else {
                            Toast.makeText(getContext(), "Network Error. Please check connectivity..", Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        add_events_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddEventsActivity.class));
            }
        });



        get_started_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_school_layout.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    public void set_up(DatabaseReference ref){



        model_list = new ArrayList<>();
        adapter = new EventsAdapter(getContext(), model_list, getActivity());

        Query query = ref.orderByChild("timestamp");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    bar.setVisibility(View.VISIBLE);

                    if(snap.child("event_title").exists() && snap.child("event_location").exists() && snap.child("event_date").exists()
                            && snap.child("u_id").exists() && snap.child("event_image").exists()
                            ){


                        final String event_title = snap.child("event_title").getValue().toString();
                        final String event_location = snap.child("event_location").getValue().toString();
                        final String event_date = snap.child("event_date").getValue().toString();
                        final String u_id = snap.child("u_id").getValue().toString();
                        final String post_key = snap.getKey();
                        final String post_time = snap.child("timestamp").getValue().toString();
                        final String event_image = snap.child("event_image").getValue().toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                        ref.child(u_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String user_name = dataSnapshot.child("Display Name").getValue().toString();
                                String user_image = dataSnapshot.child("Thumbnails").getValue().toString();

                                model_list.add(0 ,new EventsModel(event_title, event_date, event_location, u_id, event_image, user_image, user_name, post_time, post_key));

                                adapter.notifyDataSetChanged();
                                bar.setVisibility(View.GONE);

                                events_recycler.setAdapter(adapter);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
