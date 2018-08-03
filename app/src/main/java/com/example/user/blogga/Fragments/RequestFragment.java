package com.example.user.blogga.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.blogga.Adapters.RequestAdapter;
import com.example.user.blogga.Models.RequestModel;
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

public class RequestFragment extends Fragment {
    public RecyclerView request_recycler;
    public List<RequestModel> request_list;
    public RequestAdapter adapter;
    public DatabaseReference ref;
    public FirebaseAuth request_auth;
    public TextView request_hint;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        request_recycler = view.findViewById(R.id.request_recycler);
        request_list = new ArrayList<>();
        request_auth = FirebaseAuth.getInstance();
        request_hint = view.findViewById(R.id.request_hint);

        adapter = new RequestAdapter(getContext(), request_list);

        request_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        request_recycler.setHasFixedSize(true);
        request_recycler.setAdapter(adapter);
        ref = FirebaseDatabase.getInstance().getReference("Friend Requests");
        ref.child(request_auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot shot : dataSnapshot.getChildren()){
                    final String u_id = shot.getKey();
                     final String request_type = shot.child("Request Type").getValue().toString();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(u_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            request_list.clear();

                            String requester_name = dataSnapshot.child("Display Name").getValue().toString();
                            String requester_thumb = dataSnapshot.child("Thumbnails").getValue().toString();

                            RequestModel model = new RequestModel(requester_name, requester_thumb, u_id, request_type);
                            request_list.add(model);
                            adapter.notifyDataSetChanged();

                            request_hint.setVisibility(View.INVISIBLE);
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


        return view;
    }


}
