package com.example.user.blogga.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.blogga.Adapters.SectionsPagerAdapter;
import com.example.user.blogga.Dispatcher.Dispatcher;
import com.example.user.blogga.Fragments.BlogFragment;
import com.example.user.blogga.Fragments.HomeFragment;
import com.example.user.blogga.Fragments.FriendsFragment;
import com.example.user.blogga.Fragments.blog_nav_fragment;
import com.example.user.blogga.Fragments.events_nav_fragment;
import com.example.user.blogga.R;
import com.example.user.blogga.Fragments.RequestFragment;
import com.example.user.blogga.Services.Notification;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private android.support.v7.widget.Toolbar home_toolbar;
    public TabLayout tabss;
    private ViewPager mainactivity_viewpager;
    public SectionsPagerAdapter pagerAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference dbase_ref;
    private CircleImageView user_image, nav_user_image;
    private TextView chat;
    private View header_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity);
        home_toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(home_toolbar);

        startService(new Intent(this, Notification.class));

        dbase_ref = FirebaseDatabase.getInstance().getReference("Users");


        Dispatcher.start_notification_service(this);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, home_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.main_activity_frame_layout, new events_nav_fragment())
                .commit();

        user_image = findViewById(R.id.main_activity_user_photo);
        chat = findViewById(R.id.mainactivity_chat);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/fredoka.ttf");
        chat.setTypeface(custom_font);

        header_view = navigationView.getHeaderView(0);
        CircleImageView nav_user_image = header_view.findViewById(R.id.nav_header_user_image);
        nav_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OtherDetails.class));
            }
        });


//        FirebaseDatabase.getInstance().getReference("Users").keepSynced(true);

        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OtherDetails.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
//        mFragmets = new ArrayList<>();
//        mFragmets.add(new RequestFragment());
//        mFragmets.add(new HomeFragment());
//        mFragmets.add(new BlogFragment());
//        mFragmets.add(new FriendsFragment());

//        tabss = findViewById(R.id.tabss);
//        mainactivity_viewpager = findViewById(R.id.mainactivity_viewpager);
//        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager() ,mFragmets );
//
//        mainactivity_viewpager.setAdapter(pagerAdapter);
//        tabss.setupWithViewPager(mainactivity_viewpager);
//
//
//
//        tabss.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.add_user_male));
//        tabss.getTabAt(3).setIcon(getResources().getDrawable(R.drawable.users_accounts));
//        tabss.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.bubble));
//        tabss.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.google_blog));


        dbase_ref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Thumbnails").exists() && dataSnapshot.child("Display Name").exists() && dataSnapshot.child("Status").exists()){
                    String thumb = dataSnapshot.child("Thumbnails").getValue().toString();
                    String user_name =  dataSnapshot.child("Display Name").getValue().toString();
                    String user_status = dataSnapshot.child("Status").getValue().toString();

                    Picasso.get()
                            .load(thumb)
                            .placeholder(R.drawable.placeholder)
                            .into(user_image);



                    TextView nav_user_name = header_view.findViewById(R.id.nav_header_user_name);
                    TextView nav_user_status = header_view.findViewById(R.id.nav_header_user_status);
                    CircleImageView nav_user_image = header_view.findViewById(R.id.nav_header_user_image);

                    nav_user_name.setText(user_name);
                    nav_user_status.setText(user_status);

                    Picasso.get()
                            .load(thumb)
                            .placeholder(R.drawable.placeholder)
                            .into(nav_user_image);



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (id == R.id.nav_community) {

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.main_activity_frame_layout, new blog_nav_fragment())
                    .commit();
            drawer.closeDrawer(GravityCompat.START);

            return true;
        }else if(id == R.id.nav_events){
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.main_activity_frame_layout, new events_nav_fragment())
                    .commit();
            drawer.closeDrawer(GravityCompat.START);

            return true;
        }

        else if(id == R.id.nav_signout){
            drawer.closeDrawer(GravityCompat.START);

            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();

        //No time!
        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
