package com.example.user.blogga.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class OtherDetails extends AppCompatActivity {
    private ProgressDialog dialog;
    private DatabaseReference dbase_ref;
    private FirebaseAuth mAuth;
    private StorageReference mReference;

    private TextInputLayout displayname_layout, status_layout;
    private EditText displayname, status;
    private Button save_button;
    private CircleImageView profile_image;
    private String u_id;
    private Uri resultUri = null;
    private ProgressBar profile_progress;
    private String photo_download_uri;

    private Bitmap thumb_bitmap;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.user.blogga.R.layout.activity_other_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile Settings");

        mDialog = new ProgressDialog(OtherDetails.this);
        dbase_ref = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseStorage.getInstance().getReference();
        dialog = new ProgressDialog(OtherDetails.this);

        displayname = findViewById(R.id.displayname);
        profile_progress = findViewById(R.id.profile_progress);
        displayname_layout = findViewById(R.id.diplayname_layout);
        status_layout = findViewById(R.id.status_layout);
        status = findViewById(R.id.status);
        save_button = findViewById(R.id.save_button);
        profile_image = findViewById(R.id.profile_image);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(OtherDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE)
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


        load_user_details();

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate_email() && validate_staus() && resultUri != null) {
                    save_button.setEnabled(false);
                   ProgressDialog dialog = new ProgressDialog(OtherDetails.this);
                   profile_progress.setVisibility(View.VISIBLE);
                    dialog.setTitle("Saving your details");
                    dialog.setMessage("Please wait while your details are being saved..");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    profile_image.setVisibility(View.VISIBLE);
                    u_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference user_info_ref = dbase_ref.child(u_id);
                    user_info_ref.child("Display Name").setValue(displayname.getText().toString());
                    user_info_ref.child("Status").setValue(status.getText().toString());

                    save_photo_thumb(resultUri);
                    dialog.dismiss();

                }else if(validate_email() && validate_staus() && resultUri == null){
                    profile_progress.setVisibility(View.VISIBLE);

                    profile_image.setVisibility(View.VISIBLE);
                    DatabaseReference user_info_ref = dbase_ref.child(u_id);
                    user_info_ref.child("Display Name").setValue(displayname.getText().toString());
                    user_info_ref.child("Status").setValue(status.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(OtherDetails.this, "Details Saved", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(OtherDetails.this, MainActivity.class));
                            }else{
                                Toast.makeText(OtherDetails.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }

    public boolean validate_email(){
        if(TextUtils.isEmpty(displayname.getText().toString())){
            displayname_layout.setError("Name cannot be blank");
            return false;

        }else{
            displayname_layout.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validate_staus(){
        if(TextUtils.isEmpty(status.getText().toString())){
            status_layout.setError("Status cannot be blank");
            return false;

        }else{
            status_layout.setErrorEnabled(false);
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
            Toast.makeText(OtherDetails.this, "Permission Denied", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 256 && resultCode == RESULT_OK) {
            Uri first_Uri = data.getData();
            CropImage.activity(first_Uri)
                    .setAspectRatio(1, 1)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(OtherDetails.this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Picasso.get().load(resultUri)
                        .into(profile_image);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return false;
    }

    public void save_photo_thumb(final Uri resultUri){

        final DatabaseReference user_info_ref = dbase_ref.child(u_id);
        final File thumb_path = new File(resultUri.getPath());



        try {
            thumb_bitmap = new Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(75)
                    .compressToBitmap(thumb_path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] thumb_byte = baos.toByteArray();

        StorageReference photo_storage_path = mReference.child("profile_images").child(u_id);
        final StorageReference thumb_storage_path = mReference.child("profile_images").child("thumbnails").child(u_id);



        photo_storage_path.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    final String photo_download_uri_fresh = task.getResult().getDownloadUrl().toString();
                    UploadTask uploadTask = thumb_storage_path.putBytes(thumb_byte);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                            String thumb_url = thumb_task.getResult().getDownloadUrl().toString();
                            if(thumb_task.isSuccessful()){
                                Map picture_map = new HashMap();
                                picture_map.put("Profile Image", photo_download_uri_fresh);
                                picture_map.put("Thumbnails", thumb_url);

                                user_info_ref.updateChildren(picture_map ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Picasso.get().load(resultUri)
                                                    .into(profile_image);
                                            mDialog.dismiss();
                                            Toast.makeText(OtherDetails.this, "Details saved!", Toast.LENGTH_LONG)
                                                    .show();
                                            startActivity(new Intent(OtherDetails.this, MainActivity.class));
                                        }
                                    }
                                });
                            }
                        }
                    });


                } else {
                    Toast.makeText(OtherDetails.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void load_user_details(){
        u_id = mAuth.getCurrentUser().getUid();
        final StorageReference file_path = mReference.child("profile_images").child(u_id);
        DatabaseReference user_info_ref = dbase_ref.child(u_id);


            file_path.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
//                    Toast.makeText(OtherDetails.this, "Loading Profile picture..", Toast.LENGTH_LONG).show();
                         Picasso.get().load(task.getResult())
                                .placeholder(R.drawable.placeholder)
                                .into(profile_image);
                    }
                }
            });




        user_info_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String default_name = dataSnapshot.child("Display Name").getValue().toString();
                String default_status = dataSnapshot.child("Status").getValue().toString();


                displayname.setText(default_name);
                status.setText(default_status);


                    if(dataSnapshot.child("Profile Image").getValue() == null){
                        Picasso.get().load(R.drawable.placeholder)
                                .into(profile_image);
                    }else if(dataSnapshot.child("Profile Image").getValue().equals("Default")){
                        Picasso.get().load(R.drawable.placeholder)
                                .into(profile_image);
                    }else {
                        Picasso.get().load( file_path.getDownloadUrl().toString()).placeholder(R.drawable.placeholder)
                                .into(profile_image);
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OtherDetails.this, "You cancelled the event. Try again..", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
