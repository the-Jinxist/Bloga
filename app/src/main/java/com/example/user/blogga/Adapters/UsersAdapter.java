package com.example.user.blogga.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.blogga.Activities.UserProfile;
import com.example.user.blogga.Models.UserInfo;
import com.example.user.blogga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.Viewholder> {

    private Context context;
    private List<UserInfo> user_info;

    public UsersAdapter(Context context, List<UserInfo> user_info) {
        this.context = context;
        this.user_info = user_info;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false);
        return new Viewholder(view);
    }



    @Override
    public void onBindViewHolder(final UsersAdapter.Viewholder holder, final int position) {
      holder.setStatus(user_info.get(position).getStatus());
      holder.setName(user_info.get(position).getUsername());
      holder.setThumb_nails(user_info.get(position).getThumb_nails());

      holder.mView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent profile_intent =  new Intent(context, UserProfile.class);
              profile_intent.putExtra("u_id", user_info.get(position).getU_id());
              context.startActivity(profile_intent);
          }
      });
    }


    @Override
    public int getItemCount() {
        return user_info.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder {
        View mView;
        public Viewholder(View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setName(String displayname){
            TextView displayname_view = mView.findViewById(R.id.user_displayname);
            displayname_view.setText(displayname);
        }

        public void setStatus(String status){
            TextView staus_view = mView.findViewById(R.id.user_status);
            staus_view.setText(status);
        }

        public void setThumb_nails(String thumb){
            CircleImageView user_image = mView.findViewById(R.id.user_image);
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
