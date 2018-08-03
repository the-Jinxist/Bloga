package com.example.user.blogga.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.blogga.Adapters.CommentAdapter;
import com.example.user.blogga.Models.CommentModel;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogActivity extends AppCompatActivity {
    private DatabaseReference blog_ref;
    private CircleImageView user_image;
    private TextView display_name, like_count, comment_recycler_instructons;
    private ImageView blog_image_view;
    private ImageView like_picture;
    private FirebaseAuth auth;
    private RecyclerView comment_recycler;
    private List<CommentModel> model_list;
    private CommentAdapter adapter;
    private String post_key;
    private ImageView post_comment;
    private EditText comment_text;
    private RelativeLayout text_only_layout, image_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_post_info);

        post_key = getIntent().getStringExtra("post_key");
        auth = FirebaseAuth.getInstance();
        model_list = new ArrayList<>();
        adapter = new CommentAdapter(BlogActivity.this, model_list);


        image_layout = findViewById(R.id.blog_activity_image_layout);

        display_name = findViewById(R.id.blog_activity_user_displayname);
        user_image = findViewById(R.id.blog_activity_userimage);
        comment_recycler_instructons = findViewById(R.id.recycler_instructions);
        blog_image_view = findViewById(R.id.blog_activity_image);
        like_picture = findViewById(R.id.like_image);
        like_count = findViewById(R.id.blog_activity_like_count);
        comment_recycler = findViewById(R.id.comment_blog_activity_recycler);
        post_comment = findViewById(R.id.post_comment_image);
        comment_text = findViewById(R.id.comment_edit_text);

        blog_ref = FirebaseDatabase.getInstance().getReference("Blog Posts");

        comment_recycler.setLayoutManager(new LinearLayoutManager(BlogActivity.this));
        comment_recycler.setHasFixedSize(true);
        comment_recycler.setAdapter(adapter);

        set_up_activity();
        setup_comment();

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_blog();
            }
        });

        blog_ref.child(post_key).child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.hasChild(auth.getCurrentUser().getUid())){
                   Long count = dataSnapshot.getChildrenCount();
                   like_count.setText(String.valueOf(count));

                   like_picture.setImageResource(R.drawable.ic_action_liked);
                   like_picture.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(final View v) {
                           blog_ref.child(post_key).child("Likes")
                                   .child(auth.getCurrentUser().getUid())
                                   .removeValue()
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful()){
                                               Snackbar.make(v, "Post Unliked!", Snackbar.LENGTH_LONG)
                                                       .show();
                                               like_picture.setImageResource(R.drawable.ic_action_liked);

                                           }
                                       }
                                   });
                       }
                   });

               }else{
                   Long count = dataSnapshot.getChildrenCount();
                   like_count.setText(String.valueOf(count));

                   like_picture.setImageResource(R.drawable.ic_action_unliked);
                   like_picture.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(final View v) {
                           blog_ref.child(post_key).child("Likes")
                                   .child(auth.getCurrentUser().getUid())
                                   .setValue("Liked").addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       Snackbar.make(v, "Post liked!", Snackbar.LENGTH_LONG)
                                               .show();

                                    like_picture.setImageResource(R.drawable.ic_action_liked);
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
    }

    private void post_blog() {
        if(!TextUtils.isEmpty(comment_text.getText().toString())){
            comment_text.setText("");

            final DatabaseReference ref =  blog_ref.child(post_key).child("Comments").push();
            ref.child("Comment").setValue(comment_text.getText().toString());
            ref.child("u_id").setValue(auth.getCurrentUser().getUid());

            DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("Users");
            user_ref.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String thumb_url = dataSnapshot.child("Thumbnails").getValue().toString();
                    String display_name = dataSnapshot.child("Display Name").getValue().toString();

                    ref.child("user_thumb").setValue(thumb_url);
                    ref.child("display_name").setValue(display_name).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(BlogActivity.this, "Comment successfully added!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(BlogActivity.this, "Please type in a comemnt!", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private void setup_comment() {
        blog_ref.child(post_key).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                model_list.clear();

               for(DataSnapshot snap: dataSnapshot.getChildren()){
                    if(snap.child("Comment").exists() && snap.child("u_id").exists() && snap.child("user_thumb").exists() && snap.child("display_name").exists()){
                        comment_recycler_instructons.setVisibility(View.INVISIBLE);

                        String comment = snap.child("Comment").getValue().toString();
                        String u_id = snap.child("u_id").getValue().toString();
                        String thumb = snap.child("user_thumb").getValue().toString();
                        String display_name = snap.child("display_name").getValue().toString();

                        CommentModel model = new CommentModel(u_id, comment, display_name, thumb);
                        model_list.add(model);
                        adapter.notifyDataSetChanged();

                    }
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void set_up_activity(){
        blog_ref.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String blog_info = dataSnapshot.child("Blog_text").getValue().toString();
                String blog_image = dataSnapshot.child("Blog_photo").getValue().toString();
                String user_displayname = dataSnapshot.child("Blog_username").getValue().toString();
                String user_thumb = dataSnapshot.child("Blog_userthumb").getValue().toString();


                display_name.setText(user_displayname);

                if(blog_image.equals("default")){

                }

                Picasso.get()
                        .load(user_thumb)
                        .placeholder(R.drawable.placeholder)
                        .into(user_image);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
