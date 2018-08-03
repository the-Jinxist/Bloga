package com.example.user.blogga.Activities;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.user.blogga.Adapters.UsersAdapter;
import com.example.user.blogga.Models.UserInfo;
import com.example.user.blogga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView user_list;
    private Toolbar bar;
    private SwipeRefreshLayout user_swipe;
    private UsersAdapter adapter;
    private List<UserInfo> info_list;

    private Context context;
    private FirebaseAuth mAuth;

    private DatabaseReference ref, current_user_dbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        bar = findViewById(R.id.users_tool_bar);
        setSupportActionBar(bar);

        info_list = new ArrayList<>();
        context = UsersActivity.this;
        FirebaseDatabase.getInstance().getReference("Users").keepSynced(true);
        mAuth = FirebaseAuth.getInstance();

        adapter = new UsersAdapter(context, info_list);
        ref = FirebaseDatabase.getInstance().getReference("Users");
        current_user_dbase = ref.child(mAuth.getCurrentUser().getUid());

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Bloga Users");

        user_list = findViewById(R.id.user_list);
        user_swipe = findViewById(R.id.users_swipe);



        LinearLayoutManager manager = new LinearLayoutManager(this);
//        DividerItemDecoration decor = new DividerItemDecoration(user_list.getContext(), manager.getOrientation());

        user_list.setLayoutManager(manager);
        user_list.setHasFixedSize(true);
        user_list.setAdapter(adapter);
//        current_user_dbase.child("online").setValue("Online");
//        current_user_dbase.child("online").onDisconnect().setValue("Offline");
//        user_list.addItemDecoration(decor);
        setup_adapter(context);
    }

    public void setup_adapter( final Context contex){

        Query query = ref.orderByChild("Display Name");

       query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                info_list.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    if(snap.child("Display Name").exists() && snap.child("Status").exists()){
                        String name= snap.child("Display Name").getValue().toString();
                        String status = snap.child("Status").getValue().toString();
                        String u_id = snap.getRef().getKey();

                        String thumb_image = snap.child("Thumbnails").getValue().toString();
                        UserInfo info = new UserInfo(name, status, thumb_image, u_id);
                        info_list.add(info);
                        adapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        current_user_dbase.child("online").setValue("Online");
    }

    @Override
    protected void onStop() {
        super.onStop();

        current_user_dbase.child("online").setValue("Offline");
    }
}
