package com.example.user.blogga.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.blogga.Adapters.SectionsPagerAdapter;
import com.example.user.blogga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class blog_nav_fragment extends Fragment {
    public TabLayout tabss;
    private ViewPager mainactivity_viewpager;
    public SectionsPagerAdapter pagerAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference dbase_ref;
    private List<Fragment> mFragmets;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blog_fragment, container, false);

        mFragmets = new ArrayList<>();
        mFragmets.add(new RequestFragment());
        mFragmets.add(new HomeFragment());
//        mFragmets.add(new BlogFragment());
        mFragmets.add(new FriendsFragment());

        tabss = view.findViewById(R.id.tabss);
        mainactivity_viewpager = view.findViewById(R.id.mainactivity_viewpager);

        pagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager() ,mFragmets );

        mainactivity_viewpager.setAdapter(pagerAdapter);
        tabss.setupWithViewPager(mainactivity_viewpager);


        tabss.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_person_add_black_24dp));
        tabss.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ic_people_outline_black_24dp));
        tabss.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_chat_bubble_outline_black_24dp));
//        tabss.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.google_blog));
        dbase_ref = FirebaseDatabase.getInstance().getReference("Events");




        return view;
    }
}
