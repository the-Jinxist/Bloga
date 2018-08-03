package com.example.user.blogga.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.blogga.Activities.ChatActivity;
import com.example.user.blogga.Activities.UserProfile;
import com.example.user.blogga.Models.FriendsModel;
import com.example.user.blogga.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewholder> {

    private List<FriendsModel> model_list;

    public FriendsAdapter(List<FriendsModel> model_list) {
        this.model_list = model_list;
    }

    @Override
    public FriendsViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_layout, null, false);
        return new FriendsViewholder(view);
    }

    @Override
    public void onBindViewHolder(final FriendsViewholder holder, final int position) {
        holder.set_Friends_status(model_list.get(position).getFriend_status());
        holder.set_Friends_date(model_list.get(position).getFriend_date());
        holder.set_Friends_image(model_list.get(position).getFriend_image());
        holder.set_Friends_name(model_list.get(position).getFriend_name());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                  "Check Profile",
                  "Send Message"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.mView.getContext());
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            Intent profile_intent = new Intent(holder.mView.getContext(), UserProfile.class);
                            profile_intent.putExtra("u_id", model_list.get(position).getU_id());
                            holder.mView.getContext().startActivity(profile_intent);

                        }else if(which == 1){
                            Intent profile_intent = new Intent(holder.mView.getContext(), ChatActivity.class);
                            profile_intent.putExtra("u_id", model_list.get(position).getU_id());
                            holder.mView.getContext().startActivity(profile_intent);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return model_list.size();
    }

    public class FriendsViewholder extends RecyclerView.ViewHolder {
        View mView;
        public FriendsViewholder(View itemView) {
            super(itemView);

           mView = itemView;

        }

        public void set_Friends_name(String name){
            TextView friend_name = mView.findViewById(R.id.friend_user_displayname);
            friend_name.setText(name);
        }

        public void set_Friends_status(String status){
            TextView friend_status = mView.findViewById(R.id.friends_user_status);
            friend_status.setText(status);
        }

        public void set_Friends_image(String image_url){
            ImageView friend_image = mView.findViewById(R.id.friend_user_image);
            Picasso.get().load(image_url).placeholder(R.drawable.placeholder)
                    .into(friend_image);
        }

        public void set_Friends_date (String date){
            TextView friend_date = mView.findViewById(R.id.friends_date);
            friend_date.setText(date);
        }
    }
}
