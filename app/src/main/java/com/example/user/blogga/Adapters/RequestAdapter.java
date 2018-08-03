package com.example.user.blogga.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.blogga.Activities.ChatActivity;
import com.example.user.blogga.Activities.UserProfile;
import com.example.user.blogga.Models.RequestModel;
import com.example.user.blogga.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.HomeViewholder> {
    private Context context;
    private List<RequestModel> request_model_list;

    public RequestAdapter(Context context, List<RequestModel> request_model_list) {
        this.context = context;
        this.request_model_list = request_model_list;
    }

    @Override
    public RequestAdapter.HomeViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_layout, parent, false);
        return new HomeViewholder(view);
    }

    @Override
    public void onBindViewHolder(final HomeViewholder holder, final int position) {
        holder.set_Home_Name(request_model_list.get(position).getRequester_name());
        holder.set_Home_thumb(request_model_list.get(position).getRequester_thumb_nail());
        holder.m_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start_chat = new Intent(context, UserProfile.class);
                start_chat.putExtra("u_id", request_model_list.get(position).getRequester_uid());
                context.startActivity(start_chat);
            }
        });
        holder.set_UserStatus(request_model_list.get(position).getRequest_type());
    }

    @Override
    public int getItemCount() {
        return request_model_list.size();
    }

    public class HomeViewholder extends RecyclerView.ViewHolder {

        View m_view;
        public HomeViewholder(View itemView) {
            super(itemView);

            m_view = itemView;
        }

        public void set_Home_Name(String displayname){
            TextView displayname_view = m_view.findViewById(R.id.request_user_displayname);
            displayname_view.setText(displayname);
        }

        public void set_Home_thumb(String thumb){
            CircleImageView user_image = m_view.findViewById(R.id.request_user_image);
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

        public void set_UserStatus(String _user_status){
            TextView user_status = m_view.findViewById(R.id.home_user_status);
            if(_user_status.equals("Sent")){
                user_status.setText("You sent a friend request");
            }else if(_user_status.equals("Received")){
                user_status.setText("Sent you a friend request");
            }
        }
    }
}
