package com.example.user.blogga.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.blogga.Activities.BlogActivity;
import com.example.user.blogga.Activities.UserProfile;
import com.example.user.blogga.Models.BlogInfo;
import com.example.user.blogga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewholder> {
    Context context;
    List<BlogInfo> blog_list;
    DatabaseReference ref;
    FirebaseAuth auth;

    public BlogAdapter(Context context, List<BlogInfo> blog_list) {
        this.context = context;
        this.blog_list = blog_list;

        ref = FirebaseDatabase.getInstance().getReference("Blog Posts");
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public BlogViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.blog_layout, parent, false);
        return new BlogViewholder(view);
    }

    @Override
    public void onBindViewHolder(final BlogViewholder holder, final int position) {
        holder.set_blog_image(blog_list.get(position).getBlog_image_url());
        holder.set_blog_text(blog_list.get(position).getBlog_text());
        holder.set_blog_user_diaplay_name(blog_list.get(position).getDisplay_name());
        holder.set_blog_user_image(blog_list.get(position).getUser_image_url());
        if(blog_list.get(position).post_type.equals("text")){
            ImageView view = holder.m_view.findViewById(R.id.blog_layout_image_post);
            view.setVisibility(View.GONE);
        }else if(!blog_list.get(position).post_type.equals("text")){
            ImageView view = holder.m_view.findViewById(R.id.blog_layout_image_post);
            view.setVisibility(View.VISIBLE);
        }
        TextView user_display_name = holder.m_view.findViewById(R.id.blog_layout_user_display_name);
        user_display_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfile.class);
                intent.putExtra("u_id", blog_list.get(position).getUser_uid());
                v.getContext().startActivity(intent);
            }
        });

        CircleImageView imageView = holder.m_view.findViewById(R.id.blog_layout_user_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfile.class);
                intent.putExtra("u_id", blog_list.get(position).getUser_uid());
                v.getContext().startActivity(intent);
            }
        });

        final TextView like_count = holder.m_view.findViewById(R.id.like_count);

        final ImageButton like_button = holder.m_view.findViewById(R.id.like_blog_image_button);
        final TextView comment_count = holder.m_view.findViewById(R.id.comment_count);
        ref.child(blog_list.get(position).getPost_key()).child("Likes").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(auth.getCurrentUser().getUid())){


                   Long count = dataSnapshot.getChildrenCount();
                   like_count.setText(String.valueOf(count));
                    like_button.setImageDrawable(context.getDrawable(R.drawable.ic_action_liked));
                    like_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           ref.child(blog_list.get(position).getPost_key()).child("Likes")
                                   .child(auth.getCurrentUser().getUid())
                                   .removeValue()
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful()){
                                               Snackbar.make(holder.m_view, "Post Unliked!", Snackbar.LENGTH_LONG)
                                                       .show();
                                               like_button.setImageDrawable(context.getDrawable(R.drawable.ic_action_unliked));
//                                               Long count = dataSnapshot.getChildrenCount();
//                                               like_count.setText(String.valueOf(count));

                                           }
                                       }
                                   });
                        }
                    });
                }else{
                    Long count = dataSnapshot.getChildrenCount();
                    like_count.setText(String.valueOf(count));

                    like_button.setImageDrawable(context.getDrawable(R.drawable.ic_action_unliked));
                    like_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            ref.child(blog_list.get(position).getPost_key()).child("Likes")
                                    .child(auth.getCurrentUser().getUid())
                                    .setValue("Liked").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Snackbar.make(holder.m_view, "Post liked!", Snackbar.LENGTH_LONG)
                                                .show();

                                        like_button.setImageDrawable(context.getDrawable(R.drawable.ic_action_liked));

                                    }
                                }
                            });
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ImageButton comment_button = holder.m_view.findViewById(R.id.comment_blog_image_button);
        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent start_post_activty = new Intent(context, BlogActivity.class);
                start_post_activty.putExtra("post_key", blog_list.get(position).getPost_key());
                context.startActivity(start_post_activty);

            }
        });

        ref.child(blog_list.get(position).getPost_key()).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long count = dataSnapshot.getChildrenCount();
                comment_count.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class BlogViewholder extends RecyclerView.ViewHolder {
        View m_view;

        public BlogViewholder(View itemView) {
            super(itemView);

            m_view = itemView;
        }

        public void set_blog_user_diaplay_name(String display_name){
            TextView blog_user_display_name = m_view.findViewById(R.id.blog_layout_user_display_name);
            blog_user_display_name.setText(display_name);
        }

        public void set_blog_user_image(String image_url){
            CircleImageView blog_user_image = m_view.findViewById(R.id.blog_layout_user_image);
            Picasso.get()
                    .load(image_url)
                    .placeholder(R.drawable.placeholder)
                    .into(blog_user_image);
        }

        public void set_blog_text(String blog_text){
            TextView blog_text_view = m_view.findViewById(R.id.blog_layout_text_post);
            blog_text_view.setText(blog_text);
        }

        public void set_blog_image(String blog_image){
            ImageView blog_image_post = m_view.findViewById(R.id.blog_layout_image_post);
            Picasso.get()
                    .load(blog_image)
                    .placeholder(R.drawable.placeholderblogimage)
                    .into(blog_image_post);
            if(blog_image.equals("default")){
                Picasso.get()
                        .load(R.drawable.placeholderblogimage)
                        .into(blog_image_post);
            }
        }
    }
}
