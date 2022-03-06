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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vehaas.homemadefood.R;

import java.util.HashMap;
import java.util.Map;


public class SignupFragment extends AppCompatActivity {
    EditText mName, mEmail, mPassword,mPhone;
    Button mSignupBtn;
    TextView mLoginBtn;
    public FirebaseAuth fAuth;
    ProgressBar progressBar;
    TextView login_textview;
    public FirebaseFirestore fStore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup);
        mName = findViewById(R.id.edt_name);
        mEmail = findViewById(R.id.edt_email);
        mPassword = findViewById(R.id.edt_passwd);
        mPhone=findViewById(R.id.edt_phone);
        mSignupBtn = findViewById(R.id.signup_button);
        mLoginBtn = findViewById(R.id.login_button1);
        fAuth = FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        if (fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        login_textview=findViewById(R.id.login_textview);
        login_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupFragment.this, LoginFragment.class));
            }
        });
        mSignupBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name=mName.getText().toString().trim();
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                String phone=mPhone.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    mName.setError("Name is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required");
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;

                }
                if(TextUtils.isEmpty(phone)){
                    mPhone.setError("Mobile number is Required");
                    return;
                }
                if(phone.length() < 9 | phone.length() >10){
                    mPhone.setError("Mobile Number must be 10 characters");
                    return;

                }

                progressBar.setVisibility(View.VISIBLE);

                //now we register the user in firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            userID=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=fStore.collection("users").document(userID);
                            Map<String,Object> user=new HashMap<>();
                            user.put("name",name);
                            user.put("email",email);
                            user.put("phone",phone);
                            documentReference.set(user);

                            //test
//                            FirebaseDatabase rootNode;
//                            DatabaseReference reference;
//                            rootNode=FirebaseDatabase.getInstance();
//                            reference=rootNode.getReference("kitchen");
//                            FirebaseAuth fAuth;
//                            fAuth = FirebaseAuth.getInstance();
//                            String user_id = fAuth.getCurrentUser().getUid();
//                            Newid_helper helperClass=new Newid_helper(name,email,phone,"0");//setting flag as 0 for kitchencheck
//                            reference.child(user_id).setValue(helperClass);
                            //test

                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("name",name);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(SignupFragment.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }


                    }
                });

            }
        });
    }


//    public void login_txt(View view) {
//        startActivity(new Intent(getApplicationContext(),LoginFragment.class));
//        finish();
//    }
}