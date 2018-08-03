package com.example.user.blogga.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.blogga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private TextInputLayout  email_layout, password_layout, conf_password_layout;
    private EditText email, password, conf_password;
    private Button sign_up;
    private TextView signup_bloga_logo;
    private ProgressBar signup_progress;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        signup_progress = findViewById(R.id.signup_progress);

        email_layout = findViewById(R.id.signup_email_layout);
        password_layout = findViewById(R.id.signup_password_layout);
        conf_password_layout = findViewById(R.id.signup_confpassword_layout);


        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        conf_password = findViewById(R.id.signup_confpassword);


        sign_up = findViewById(R.id.signup_button2);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate_email() && validate_password() && validate_password_emptiness() ){
                    signup_progress.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), conf_password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        signup_progress.setVisibility(View.INVISIBLE);
                                        String uid = mAuth.getCurrentUser().getUid();
                                        DatabaseReference infos = mDatabase.child(uid);
                                        infos.child("Display Name").setValue("No Name");
                                        infos.child("Status").setValue("Hi, This app rocks!");
                                        infos.child("Thumbnails").setValue("Default");
                                        infos.child("Profile Image").setValue("Default").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                startActivity(new Intent(SignupActivity.this, OtherDetails.class));
                                            }
                                        });


                                    }else{
                                       signup_progress.setVisibility(View.INVISIBLE);
                                        Toast.makeText(SignupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }



    private boolean validate_email(){
        if(TextUtils.isEmpty(email.getText().toString())){
            email_layout.setError("Please input an email");
            return  false;
        }else if(!email.getText().toString().contains("@")){
            email_layout.setError("Please input a valid email");
            return false;
        }else{
            email_layout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validate_password() {
        if (TextUtils.isEmpty(password.getText().toString()) && TextUtils.isEmpty(conf_password.getText().toString())) {
            password_layout.setError("Please input a password");
            conf_password_layout.setError("Please input a password");
            return false;
        }
        else if(TextUtils.isEmpty(password.getText().toString())){
            password_layout.setError("Please input a password");
            return false;
        }
        else if(TextUtils.isEmpty(conf_password.getText().toString())){
            conf_password_layout.setError("Please input a password");
            return false;
        }
        else{
            password_layout.setErrorEnabled(false);
            conf_password_layout.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validate_password_emptiness(){
        if(!password.getText().toString().equals(conf_password.getText().toString())){
            password_layout.setError("These passwords differ");
            conf_password_layout.setError("These passwords differ");
            return false;
        }
        else{
            password_layout.setErrorEnabled(false);
            conf_password_layout.setErrorEnabled(false);
            return true;
        }
    }


}
