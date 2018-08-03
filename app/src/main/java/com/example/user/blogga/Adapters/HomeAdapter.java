package com.example.user.blogga.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.blogga.Activities.ChatActivity;
import com.example.user.blogga.Models.HomeModel;
import com.example.user.blogga.Models.UserInfo;
import com.example.user.blogga.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewholder> {
    private Context context;
    private List<HomeModel> home_info;

    public HomeAdapter(Context context, List<HomeModel> home_info) {
        this.context = context;
        this.home_info = home_info;
    }

    @Override
    public HomeAdapter.HomeViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_layout, parent, false);
        return new HomeViewholder(view);
    }

    @Override
    public void onBindViewHolder(final HomeViewholder holder, final int position) {
        holder.set_Home_Name(home_info.get(position).getChat_name());
        holder.set_Home_Status(home_info.get(position).getChat_status());
        holder.set_Home_thumb(home_info.get(position).getChat_image_u_id());

        holder.m_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start_chat = new Intent(context, ChatActivity.class);
                start_chat.putExtra("u_id", home_info.get(position).getChat_other_user_uid());
                context.startActivity(start_chat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return home_info.size();
    }

    public class HomeViewholder extends RecyclerView.ViewHolder {

        View m_view;
        public HomeViewholder(View itemView) {
            super(itemView);

            m_view = itemView;
        }

        public void set_Home_Name(String displayname){
            TextView displayname_view = m_view.findViewById(R.id.home_user_displayname);
            displayname_view.setText(displayname);
        }

        public void set_Home_Status(String status){
            TextView staus_view = m_view.findViewById(R.id.home_user_status);
            staus_view.setText(status);
        }

        public void set_Home_thumb(String thumb){
            CircleImageView user_image = m_view.findViewById(R.id.home_user_image);
            if(thumb != null){
                Picasso.get().load(thumb)
                        .placeholder(R.drawable.placeholder)
                        .into(user_image);
            }else if(thumb.equals("default")){
                Picasso.get().load(R.drawable.placeholder)
                        .into(user_image);
            }else{
                Picasso.get().load(R.drawable.placeholder)
                        .into(user_image);
            }

        }
    }
}
