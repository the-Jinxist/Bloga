package com.example.user.blogga.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.blogga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class PostBlogActivity extends AppCompatActivity {

    private Toolbar bar;
    private DatabaseReference root_dbase, blog_dbse;
    private FirebaseAuth auth;
    private TextInputLayout blog_text_layout;
    private EditText blog_text;
    private Button add_post;
    private ImageView post_image;
    private Uri photo_uri = null;
    private String display_name, thumb;
    private ProgressDialog dialog;
    private StorageReference blog_storage_ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_blog);

        bar = findViewById(R.id.blog_post_toolbar);
        setSupportActionBar(bar);


        add_post = findViewById(R.id.add_blog_post_btn);
        blog_text_layout = findViewById(R.id.blog_post_text_layout);
        blog_text = findViewById(R.id.blog_post_edittext);
        post_image = findViewById(R.id.blog_post_picture);
        blog_storage_ref = FirebaseStorage.getInstance().getReference("Blog posts");
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(PostBlogActivity.this);

        root_dbase = FirebaseDatabase.getInstance().getReference();
        blog_dbse = root_dbase.child("Blog Posts");
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        root_dbase.child("Users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                display_name = dataSnapshot.child("Display Name").getValue().toString();
                thumb = dataSnapshot.child("Thumbnails").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(PostBlogActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                    } else {
                        Intent gallery_intent = new Intent();
                        gallery_intent.setType("image/*");
                        gallery_intent.setAction(Intent.ACTION_GET_CONTENT);

                        startActivityForResult(gallery_intent, 256);
                    }

                } else {
                    Intent gallery_intent = new Intent();
                    gallery_intent.setType("image/*");
                    gallery_intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(gallery_intent, 256);
                }
            }
        });

        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                if(validate_blog_text()){
//                    dialog.setMessage("Posting to the blog!");
//                    dialog.setCanceledOnTouchOutside(false);
//                    dialog.show();

                    if(photo_uri == null){

//                        Toast.makeText(PostBlogActivity.this, display_name + " ", Toast.LENGTH_LONG)
//                                .show();

                        String blog_type = "text";
                        String blog_photo = "default";

                        DatabaseReference post_ref = blog_dbse.push();

                        post_ref.child("Blog_text").setValue(blog_text.getText().toString());
                        post_ref.child("current_user_uid").setValue(auth.getCurrentUser().getUid());
                        post_ref.child("Blog_photo").setValue(blog_photo);
                        post_ref.child("Blog_type").setValue(blog_type);
                        post_ref.child("Blog_username").setValue(display_name);
                        post_ref.child("Blog_userthumb").setValue(thumb).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Snackbar.make(v, "Blog post added!", Snackbar.LENGTH_LONG)
                                            .show();
                                    startActivity(new Intent(PostBlogActivity.this, MainActivity.class));
                                    dialog.dismiss();
                                }
                            }
                        });

                    }else{
                        if(validate_blog_text()){

                            final DatabaseReference post_ref = blog_dbse.push();
                            final String blog_type = "image_included";

                            String key = post_ref.getKey();

                            StorageReference blog_pic_storage_ref = blog_storage_ref.child(key);
                            blog_pic_storage_ref.putFile(photo_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    String blog_photo = task.getResult().getDownloadUrl().toString();

                                    post_ref.child("Blog_text").setValue(blog_text.getText().toString());
                                    post_ref.child("current_user_uid").setValue(auth.getCurrentUser().getUid());
                                    post_ref.child("Blog_photo").setValue(blog_photo);
                                    post_ref.child("Blog_type").setValue(blog_type);
                                    post_ref.child("Blog_username").setValue(display_name);
                                    post_ref.child("Blog_userthumb").setValue(thumb).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Snackbar.make(v, "Blog post added!", Snackbar.LENGTH_LONG)
                                                        .show();
                                                startActivity(new Intent(PostBlogActivity.this, MainActivity.class));

                                                dialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            });
                        }

                    }
                }
            }
        });

    }

    public boolean validate_blog_text(){
        if(TextUtils.isEmpty(blog_text.getText())){
            blog_text_layout.setError("Please type in an email");
            return false;
        } else{
            blog_text_layout.setErrorEnabled(false);
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 123){
            Intent gallery_intent = new Intent();
            gallery_intent.setType("image/*");
            gallery_intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(gallery_intent, 256);

        }else{
            Toast.makeText(PostBlogActivity.this, "Permission Denied", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 256 && resultCode == RESULT_OK) {
            Uri first_Uri = data.getData();
            CropImage.activity(first_Uri)
                    .setAspectRatio(1, 1)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(PostBlogActivity.this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                photo_uri = result.getUri();
                Picasso.get().load(photo_uri)
                        .into(post_image);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
