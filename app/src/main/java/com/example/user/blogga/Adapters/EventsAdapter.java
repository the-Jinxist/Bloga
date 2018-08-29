package com.example.user.blogga.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.blogga.Activities.ChatActivity;
import com.example.user.blogga.Activities.UserProfile;
import com.example.user.blogga.Models.EventsModel;
import com.example.user.blogga.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Big-Nosed Developer on the Edge of Infinity.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    Context context;
    List<EventsModel> model_list;
    Activity activty;

    private static final String BASE_URL = "http://www.neolution.bloga.com/";
    private static final String GOOGLE_MAPS_BASE_URL = "geo:0,0?q=";
    public EventsAdapter(Context context, List<EventsModel> model_list, Activity activty) {
        this.context = context;
        this.model_list = model_list;
        this.activty = activty;
    }

    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventsAdapter.ViewHolder holder, final int position) {

        RelativeLayout event_image_poster_layout = holder.view.findViewById(R.id.event_image_poster_layout);
        RelativeLayout error_event_image_layout = holder.view.findViewById(R.id.events_image_error_layout);

        holder.set_event_image(model_list.get(position).getEvent_thumb(),model_list.get(position).getEvent_image(), error_event_image_layout);
        holder.set_event_location(model_list.get(position).getEvent_location());
        holder.set_event_name(model_list.get(position).getEvent_name());
        holder.set_event_time(model_list.get(position).getEvent_time());
        holder.set_user_image(model_list.get(position).getUser_image());
        holder.set_user_name(model_list.get(position).getUser_name());
        holder.set_post_time(model_list.get(position).getPost_time());
        holder.set_event_desc(model_list.get(position).getEvent_desc());



        ImageButton share_btn = holder.view.findViewById(R.id.generate_share_link);
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View share_view = LayoutInflater.from(context).inflate(R.layout.share_event, null);



               AlertDialog.Builder builder = new AlertDialog.Builder(context);
               builder.setCancelable(true);
               builder.setView(share_view);

               final AlertDialog dialog = builder.create();
                TextView copy_to_clip = share_view.findViewById(R.id.copy_to_clipboard);
                TextView share_to = share_view.findViewById(R.id.share_to);

                copy_to_clip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager)
                                context.getSystemService(Context.CLIPBOARD_SERVICE);

                        ClipData clip = ClipData.newPlainText("post_id", BASE_URL+model_list.get(position).getPost_key());
                        if(clipboard != null){
                            clipboard.setPrimaryClip(clip);
                        }

                        Toast.makeText(context, "Post link copied to clipboard", Toast.LENGTH_LONG)
                                .show();

                        dialog.dismiss();

                    }
                });

                share_to.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent share_intent = new Intent();
                        share_intent.setAction(Intent.ACTION_SEND);
                        share_intent.putExtra(Intent.EXTRA_TEXT, BASE_URL+model_list.get(position).getPost_key());
                        share_intent.setType("text/plain");
                        context.startActivity(Intent.createChooser(share_intent, "S"));
                    }
                });

                dialog.show();

            }
        });


        final ImageView event_image = holder.view.findViewById(R.id.event_image_poster);
        event_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(context).inflate(R.layout.picture_layout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setView(view);

                final AlertDialog dialog = builder.create();
                dialog.show();
                ImageView pic = view.findViewById(R.id.event_full_pic);

                TextView full_pic_event_title = view.findViewById(R.id.full_pic_event_title);
                TextView full_event_event_time = view.findViewById(R.id.full_pic_event_time);
                ImageButton share_event_fll_pic = view.findViewById(R.id.full_pic_share_event);


                share_event_fll_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        View share_view = LayoutInflater.from(context).inflate(R.layout.share_event, null);



                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(true);
                        builder.setView(share_view);

                        final AlertDialog dialog = builder.create();
                        TextView copy_to_clip = share_view.findViewById(R.id.copy_to_clipboard);
                        TextView share_to = share_view.findViewById(R.id.share_to);
                        ImageButton close_dialog = share_view.findViewById(R.id.close_dialog);

                        close_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        copy_to_clip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ClipboardManager clipboard = (ClipboardManager)
                                        context.getSystemService(Context.CLIPBOARD_SERVICE);

                                ClipData clip = ClipData.newPlainText("post_id", BASE_URL+model_list.get(position).getPost_key());
                                if(clipboard != null){
                                    clipboard.setPrimaryClip(clip);
                                }

                                Toast.makeText(context, "Post link copied to clipboard", Toast.LENGTH_LONG)
                                        .show();

                                dialog.dismiss();

                            }
                        });

                        share_to.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent share_intent = new Intent();
                                share_intent.setAction(Intent.ACTION_SEND);
                                share_intent.putExtra(Intent.EXTRA_TEXT, BASE_URL+model_list.get(position).getPost_key());
                                share_intent.setType("text/plain");
                                context.startActivity(Intent.createChooser(share_intent, "S"));
                            }
                        });

                        dialog.show();

                    }
                });

                full_pic_event_title.setText(model_list.get(position).getEvent_name());


                full_event_event_time.setText(model_list.get(position).getEvent_time());

                Picasso.get()
                        .load(model_list.get(position).getEvent_image())
                        .placeholder(R.drawable.placeholder)
                        .into(pic);


                ImageButton cancel_dialog = view.findViewById(R.id.cancel_event_full_pic);
                cancel_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        final TextView location_text = holder.view.findViewById(R.id.event_location);
        location_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "The location is "+ location_text.getText().toString(), Toast.LENGTH_SHORT).show();
                String location_search = location_text.getText().toString();

               String search_query = location_search.replace(" " , "+");


               Uri intent_place = Uri.parse(GOOGLE_MAPS_BASE_URL+search_query);
                Intent map_intent = new Intent(Intent.ACTION_VIEW, intent_place);
                map_intent.setPackage("com.google.android.apps.maps");
                context.startActivity(map_intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return model_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
        }

        public void set_event_name(String _event_name){
            TextView event_name = view.findViewById(R.id.event_name);
            event_name.setText(_event_name);
        }

        public void set_event_time(String _event_time){
            TextView event_time = view.findViewById(R.id.event_time);
            event_time.setText(_event_time);
        }

        public void set_event_location(String _location){
            TextView event_location = view.findViewById(R.id.event_location);
            event_location.setText(_location);
        }

        public void set_event_image(final String _event_thumb, final String _event_image, final RelativeLayout error_layout){
            final ImageView event_image = view.findViewById(R.id.event_image_poster);
            Picasso.get()
                    .load(_event_image)
                    .into(event_image, new Callback(){

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(_event_thumb)
                                    .into(event_image, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            error_layout.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    });
        }

        public void set_user_image(String _user_image){
            CircleImageView event_image  = view.findViewById(R.id.event_user_image);
            Picasso.get()
                    .load(_user_image)
                    .placeholder(R.drawable.placeholder)
                    .into(event_image);
        }

        public void set_user_name(String _user_name){
            TextView user_name = view.findViewById(R.id.event_user_name);
            user_name.setText(_user_name);
        }

        public void set_post_time(String _time_stamp){
            TextView post_date = view.findViewById(R.id.post_time);
            Long time_stamp = Long.parseLong(_time_stamp);
            Calendar cal = Calendar.getInstance();

            cal.setTimeInMillis(time_stamp);

            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("hh:mm, dd MM yyyy");
            String date_finale = format.format(cal.getTime());
            post_date.setText(date_finale);
        }

        public void  set_event_desc(String _event_desc){
            TextView event_desc = view.findViewById(R.id.event_desc);
            event_desc.setText(_event_desc);
        }
    }


}
