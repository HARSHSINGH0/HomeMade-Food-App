package com.vehaas.homemadefood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vehaas.homemadefood.R;

public class LoginFragment extends AppCompatActivity {
    FirebaseAuth fAuth;
    Button loginButton1;
    EditText mEmail,mPassword;
    ProgressBar progressBar;
    TextView mRDsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        mRDsignup=findViewById(R.id.registerbuttonRD);
        fAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.edt_email);
        mPassword = findViewById(R.id.edt_passwd);
        loginButton1=findViewById(R.id.login_button1);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        loginButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mEmail.setError("Password is Required");
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password Must be greater than 5 Characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else {
                            Toast.makeText(LoginFragment.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }
    public void create_txt(View view) {
        startActivity(new Intent(getApplicationContext(),SignupFragment.class));

    }

    public void forgetPassword(View view) {
        if (TextUtils.isEmpty(mEmail.getText())) {
            mEmail.setError("Email is Required");
            return;
        }
        else{
            fAuth.sendPasswordResetEmail(mEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(LoginFragment.this,"We have sent reset password link",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

    }
}