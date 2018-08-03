package com.example.user.blogga.Fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.blogga.Activities.MainActivity;
import com.example.user.blogga.Activities.UsersActivity;
import com.example.user.blogga.Adapters.HomeAdapter;
import com.example.user.blogga.Models.HomeModel;
import com.example.user.blogga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class HomeFragment extends Fragment {
    public RecyclerView home_list;
    public DatabaseReference chats_ref;
    public FirebaseAuth auth;
    public List<HomeModel> home_info;
    public HomeAdapter adapter;
    public FloatingActionButton chat_fab;
    private MainActivity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.fragment_home, container, false);
        home_list = mview.findViewById(R.id.home_recycler);
        chat_fab = mview.findViewById(R.id.chat_fab);
        activity = new MainActivity();

        chat_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UsersActivity.class));
            }
        });

        chats_ref = FirebaseDatabase.getInstance().getReference("Chats");
        home_info = new ArrayList<>();
        adapter = new HomeAdapter(getContext(), home_info);
        home_list.setAdapter(adapter);
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("Chats").keepSynced(true);

        home_list.setHasFixedSize(true);
        home_list.setLayoutManager(new LinearLayoutManager(getContext()));

        chats_ref.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                home_info.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    final String user_id = snap.getKey();
                    DatabaseReference user_dbase = FirebaseDatabase.getInstance().getReference("Users");
                    user_dbase.child(user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("Display Name").getValue().toString();
                            String thum = dataSnapshot.child("Thumbnails").getValue().toString();
//                            String status = dataSnapshot.child("Status").getValue().toString();
                            String status = "Chat started..";
                            HomeModel model = new HomeModel(name, thum, user_id, status);
                            home_info.add(model);
                            HomeAdapter fresh_adapter = new HomeAdapter(getContext(), home_info);
                            home_list.setAdapter(fresh_adapter);
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
        return mview;
    }
}
