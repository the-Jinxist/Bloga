package com.example.user.blogga.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.blogga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class AddEventsActivity extends AppCompatActivity {
    private EditText event_title, event_location, event_date;
    private TextInputLayout event_title_layout, event_location_layout, event_date_layout;
    private Uri result_uri;
    private Button add_event_btn;
    private DatabaseReference events_ref;
    private FloatingActionButton add_event_image_btn;
    private StorageReference storageReference;
    private Bitmap thumb_bitmap;
    private ImageView add_events_image;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        events_ref = FirebaseDatabase.getInstance().getReference("Events");
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("event_pics");

        event_title_layout = findViewById(R.id.add_event_edit_text_layout);
        event_location_layout = findViewById(R.id.add_event_location_layout);
        event_date_layout = findViewById(R.id.add_event_date_layout);

        event_title = findViewById(R.id.add_event_title);
        event_location = findViewById(R.id.add_event_location);
        event_date = findViewById(R.id.add_event_date);
        add_event_image_btn = findViewById(R.id.add_event_image_btn);
        add_events_image = findViewById(R.id.add_events_imageview);

        add_event_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(AddEventsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
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

        add_event_btn = findViewById(R.id.activity_add_event_btn);
        add_event_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(validate_inputs(event_title, event_title_layout, "Please type your event's title")
                        && validate_inputs(event_location, event_location_layout, "Please specify your event's location")
                        && validate_inputs(event_date, event_date_layout, "Please specify your event's layout")
                        && result_uri != null
                        ){

                    //TODO ADD EVENT DESCRIPTION AND HOOK STUFF UP

                    ProgressDialog dialog = new ProgressDialog(AddEventsActivity.this);
                    dialog.setMessage("Adding your event!");
                    dialog.show();

                    String event_title_text = event_title.getText().toString();
                    String event_location_text = event_location.getText().toString();
                    String event_date_text = event_date.getText().toString();

                    DatabaseReference add_event_ref = events_ref.child("FUTA").push();
                    store_cake_info(add_event_ref, event_title_text, event_location_text, event_date_text, dialog, result_uri);

                }
            }
        });
    }

    public boolean validate_inputs(TextView text_input, TextInputLayout text_layout, String error_text){
        if(TextUtils.isEmpty(text_input.getText().toString())){
            text_layout.setErrorEnabled(true);
            text_layout.setError(error_text);

            return false;
        }else{

            text_layout.setErrorEnabled(false);
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
            Toast.makeText(AddEventsActivity.this, "Permission Denied", Toast.LENGTH_LONG)
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
                    .start(AddEventsActivity.this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                result_uri = result.getUri();
                Picasso.get().load(result_uri)
                        .into(add_events_image);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void save_thumb_and_image(final DatabaseReference _ref_to_use, Uri _result_uri, final ProgressDialog _dialog){

        String key = _ref_to_use.getKey();
        StorageReference reference = storageReference.child("food_pics").child(key);

        final File thumb_path = new File(_result_uri.getPath());

        try {
            thumb_bitmap = new Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(50)
                    .compressToBitmap(thumb_path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        final byte[] thumb_byte = baos.toByteArray();

        StorageReference photo_path = reference.child("photo");
        final StorageReference thumb_storage_path = reference.child("thumb");

        photo_path.putFile(result_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> _photo_task) {
                if(_photo_task.isSuccessful()){
                    thumb_storage_path.putBytes(thumb_byte).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                            if(thumb_task.isSuccessful()){

                                String photo_url = _photo_task.getResult().getDownloadUrl().toString();
                                final String thumb_url = thumb_task.getResult().getDownloadUrl().toString();

                                _ref_to_use.child("event_image").setValue(photo_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            _ref_to_use.child("event_image_thumb").setValue(thumb_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        _dialog.dismiss();
//                                                        startActivity(new Intent(AddFoodActivity.this, CategoryCakes.class));
                                                        Toast.makeText(AddEventsActivity.this, "Events added!", Toast.LENGTH_SHORT)
                                                                .show();

                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                            }
                        }
                    });
                }else{
                    Toast.makeText(AddEventsActivity.this, "An error occurred. Please check your internet connection!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    public void store_cake_info(final DatabaseReference _ref_to_use, final String event_title,
                                String event_location, String event_date,
                                final ProgressDialog _dialog, final Uri _result_uri){


        _ref_to_use.child("event_title").setValue(event_title);
        _ref_to_use.child("event_location").setValue(event_location);
        _ref_to_use.child("u_id").setValue(auth.getCurrentUser().getUid());
        _ref_to_use.child("timestamp").setValue(ServerValue.TIMESTAMP);
        _ref_to_use.child("event_date").setValue(event_date).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    save_thumb_and_image(_ref_to_use, _result_uri, _dialog);
                }
            }
        });
    }


}
