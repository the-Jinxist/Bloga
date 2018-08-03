package com.example.user.blogga.Activities;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.blogga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText email_address, password;
    private TextInputLayout email_layout, password_layout;
    private Button login;
    private TextView signup, bloga_logo;
    private FirebaseAuth mAuth;
    private ProgressDialog login_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        login_dialog = new ProgressDialog(LoginActivity.this);
        email_address = findViewById(R.id.email_address);
        password = findViewById(R.id.password);

        email_layout = findViewById(R.id.email_address_layout);
        password_layout = findViewById(R.id.password_layout);

        signup = findViewById(R.id.signup_button);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate_email();
                validate_password();

                if(validate_email() && validate_password()){
                    login_dialog.setTitle("Validating Information");
                    login_dialog.setMessage("Please wait while we validate your info");
                    login_dialog.show();
                    mAuth.signInWithEmailAndPassword(email_address.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        login_dialog.dismiss();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    }
                                    else{

                                        login_dialog.dismiss();
                                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            });
                }
            }
        });
    }
    private boolean validate_email(){
        if(TextUtils.isEmpty(email_address.getText())){
            email_layout.setError("Please type in an email");
            return false;
        }else if(!email_address.getText().toString().contains("@")){
            email_layout.setError("Please type in a valid email");
            return  false;
        }else{
            email_layout.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validate_password(){
        if(TextUtils.isEmpty(password.getText().toString())){
            password_layout.setError("Please type in an password");
            return false;
        }else{
            password_layout.setErrorEnabled(false);
            return true;
        }

    }
}
