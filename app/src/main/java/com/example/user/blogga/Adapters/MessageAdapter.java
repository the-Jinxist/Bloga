package com.example.user.blogga.Adapters;

import android.content.Context;
import android.provider.DocumentsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.blogga.Models.MessageModel;
import com.example.user.blogga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewholder> {

    public Context context;
    public List<MessageModel> messageModelList;
    private FirebaseAuth mAuth;

    public MessageAdapter(Context context, List<MessageModel> messageModelList) {
        this.context = context;
        this.messageModelList = messageModelList;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public MessageAdapter.MessageViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_layout, parent, false);
        return new MessageViewholder(view);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.MessageViewholder holder, int position) {

        holder.set_Image(messageModelList.get(position).getImage_u_id());
        holder.set_Message(messageModelList.get(position).getMessage());
        holder.setGravity(messageModelList.get(position).getU_id());
        holder.set_timestamp(messageModelList.get(position).getTime_stamp());

        holder.setIsRecyclable(false);
          }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MessageViewholder extends RecyclerView.ViewHolder {

        View m_view;
        public MessageViewholder(View itemView) {
            super(itemView);

            m_view = itemView;
        }

        public void set_Message(String message){
            TextView message_text_view = m_view.findViewById(R.id.message_text_view);
            message_text_view.setText(message);
        }

        public void set_Image(String image_uri){
            ImageView user_image = m_view.findViewById(R.id.message_user_image);
            Picasso.get()
                    .load(image_uri)
                    .placeholder(R.drawable.placeholder)
                    .into(user_image);
        }

        public void setGravity(String current_uid){
            RelativeLayout root = m_view.findViewById(R.id.root_message_layout);
            if(current_uid.equals(mAuth.getCurrentUser().getUid())){
                root.setGravity(View.FOCUS_RIGHT);
                root.setHorizontalGravity(View.FOCUS_RIGHT);
            }else{
                root.setGravity(View.FOCUS_LEFT);
                root.setHorizontalGravity(View.FOCUS_LEFT);
            }
        }

        public void set_timestamp(String time){
            TextView time_stamp = m_view.findViewById(R.id.message_timestamp);
            time_stamp.setText(time);
        }

    }
}
