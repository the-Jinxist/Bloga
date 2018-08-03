package com.example.user.blogga.Fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.blogga.Activities.UsersActivity;
import com.example.user.blogga.Adapters.FriendsAdapter;
import com.example.user.blogga.Models.FriendsModel;
import com.example.user.blogga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class FriendsFragment extends Fragment {

    public RecyclerView friends_list;
    public FriendsAdapter adapter;
    public List<FriendsModel> model_list;
    private DatabaseReference user_ref;
    private FirebaseUser current_user;
    private FirebaseAuth mAuth;
    private String current_user_uid;
    public FloatingActionButton users_fab;
    private TextView other_stuff;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        model_list = new ArrayList<>();
        other_stuff = view.findViewById(R.id.friends_hint);
        mAuth = FirebaseAuth.getInstance();
        friends_list = view.findViewById(R.id.friends_recycler);
        adapter = new FriendsAdapter(model_list);
        user_ref = FirebaseDatabase.getInstance().getReference().child("Friends");

        current_user = mAuth.getCurrentUser();
        setup_adapter();
        current_user_uid = current_user.getUid();
        friends_list.setLayoutManager(new LinearLayoutManager(getContext()));
        friends_list.setHasFixedSize(true);
        users_fab = view.findViewById(R.id.request_add);
        FirebaseDatabase.getInstance().getReference().child("Friends").keepSynced(true);


        users_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UsersActivity.class));
            }
        });
           return view;
    }

    public void setup_adapter(){
//       user_ref.addValueEventListener(new ValueEventListener() {
//           @Override
//           public void onDataChange(DataSnapshot dataSnapshot) {
//               for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                   String u_id = dataSnapshot1.getKey();
//                   Toast.makeText(getContext(), u_id, Toast.LENGTH_SHORT).show();
//               }
//           }
//
//           @Override
//           public void onCancelled(DatabaseError databaseError) {
//
//           }
//       });

        final DatabaseReference current_user_friend_database = user_ref.child(current_user.getUid());
        current_user_friend_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    final String friends_uid = dataSnapshot1.getKey();
                    DatabaseReference friends_user_info = FirebaseDatabase.getInstance().getReference().child("Users").child(friends_uid);
                    friends_user_info.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot shot) {
                            final String name= shot.child("Display Name").getValue().toString();
                            final String status = shot.child("Status").getValue().toString();
                            final String thumb = shot.child("Thumbnails").getValue().toString();

                            current_user_friend_database.child(friends_uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    model_list.clear();
                                    String date = dataSnapshot.getValue().toString();
                                    FriendsModel model = new FriendsModel(name, status, thumb, date, friends_uid);
                                    other_stuff.setVisibility(View.INVISIBLE);
                                    model_list.add(model);

//                                    Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();
                                    FriendsAdapter adapter = new FriendsAdapter(model_list);
                                    friends_list.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

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
