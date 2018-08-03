package com.example.user.blogga.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.blogga.Activities.PostBlogActivity;
import com.example.user.blogga.Activities.UsersActivity;
import com.example.user.blogga.Adapters.BlogAdapter;
import com.example.user.blogga.Adapters.FriendsAdapter;
import com.example.user.blogga.Models.BlogInfo;
import com.example.user.blogga.Models.FriendsModel;
import com.example.user.blogga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class BlogFragment extends Fragment {
    private FloatingActionButton fab;
    private RecyclerView blog_recycler;
    private BlogAdapter adapter;
    private List<BlogInfo> info_list;
    private DatabaseReference root_ref;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        fab = view.findViewById(R.id.blog_fragment_fab);
        blog_recycler = view.findViewById(R.id.blog_recycler);


        info_list = new ArrayList<>();

        root_ref = FirebaseDatabase.getInstance().getReference();

        DatabaseReference blog_ref = root_ref.child("Blog Posts");
        DatabaseReference user_ref = root_ref.child("Users");
        adapter = new BlogAdapter(getContext(), info_list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        DividerItemDecoration decor = new DividerItemDecoration(getContext(), manager.getOrientation());

        blog_recycler.setLayoutManager(manager);
//        blog_recycler.addItemDecoration(decor);

        blog_recycler.setHasFixedSize(true);
        blog_recycler.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PostBlogActivity.class));
            }
        });

        blog_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                info_list.clear();

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    if(snap.child("Blog_text").exists() && snap.child("current_user_uid").exists() && snap.child("Blog_photo").exists() && snap.child("Blog_type").exists() &&
                    snap.child("Blog_username").exists() && snap.child("Blog_userthumb").exists()){
                        String blog_text = snap.child("Blog_text").getValue().toString();
                        String user_id = snap.child("current_user_uid").getValue().toString();
                        String blog_photo = snap.child("Blog_photo").getValue().toString();
                        String post_type = snap.child("Blog_type").getValue().toString();
                        String user_displayname = snap.child("Blog_username").getValue().toString();
                        String user_thumb = snap.child("Blog_userthumb").getValue().toString();
                        String post_key = snap.getKey();

                        BlogInfo new_info = new BlogInfo(user_displayname, user_thumb, blog_photo, blog_text, user_id, post_type, post_key);
                        info_list.add(new_info);

                        adapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

           return view;


    }

}
