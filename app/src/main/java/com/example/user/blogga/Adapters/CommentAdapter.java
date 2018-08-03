package com.example.user.blogga.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.blogga.Models.CommentModel;
import com.example.user.blogga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context context;
    List<CommentModel> comment_info;
    private FirebaseAuth suth;

    public CommentAdapter(Context context, List<CommentModel> comment_info) {
        this.context = context;
        this.comment_info = comment_info;

        suth = FirebaseAuth.getInstance();
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        holder.set_comment(comment_info.get(position).getComment());

        if(comment_info.get(position).getUser_id().equals(suth.getCurrentUser().getUid())){
            holder.set_user_displayname("You");
        }else{
            holder.set_user_displayname(comment_info.get(position).getDisplay_name());
        }

        holder.set_user_thumb(comment_info.get(position).getThumb_url());
    }

    @Override
    public int getItemCount() {
        return comment_info.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View m_view;
        public ViewHolder(View itemView) {
            super(itemView);

            m_view = itemView;
        }

        public void set_comment(String comment){
            TextView comment_view = m_view.findViewById(R.id.comment_text);
            comment_view.setText(comment);
        }

        public void set_user_displayname(String display_name){
            TextView display_name_view = m_view.findViewById(R.id.comment_user_displayname);
            display_name_view.setText(display_name);
        }

        public void set_user_thumb(String thumb_url){
            CircleImageView comment_image = m_view.findViewById(R.id.comment_user_image);
            Picasso.get()
                    .load(thumb_url)
                    .placeholder(R.drawable.placeholder)
                    .into(comment_image);
        }
    }
}
