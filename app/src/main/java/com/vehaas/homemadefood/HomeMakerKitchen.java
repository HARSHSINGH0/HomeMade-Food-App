package com.vehaas.homemadefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class HomeMakerKitchen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth fAuth;
        fAuth = FirebaseAuth.getInstance();
        String user_id = fAuth.getCurrentUser().getUid();
        setContentView(R.layout.activity_home_maker_kitchen);
        FirebaseDatabase rootNode;
        DatabaseReference reference;
        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference("kitchen");
        Query checkuser=reference.orderByChild("kitchen_userid").equalTo(user_id);
        ProgressBar pgb=findViewById(R.id.progressBar2);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                startActivity(new Intent(getApplicationContext(),KitchenFood.class));
                pgb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void save_button_kitchen_desc(View view) {
        EditText kitchen_name=findViewById(R.id.edt_kitchenName);
        EditText desc=findViewById(R.id.edt_kitchenDesc);
        EditText food_style=findViewById(R.id.edt_DishStyle);
        EditText kitchen_address=findViewById(R.id.edt_address);

        FirebaseAuth fAuth;
        fAuth = FirebaseAuth.getInstance();
        String user_id = fAuth.getCurrentUser().getUid();
        String Kitchen_name =kitchen_name.getText().toString().trim();
        String Kitchen_desc =desc.getText().toString().trim();
        String Kitchen_style =food_style.getText().toString().trim();
        String Kitchen_address =food_style.getText().toString().trim();
        FirebaseDatabase rootNode;
        DatabaseReference reference;
        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference("kitchen");
        Kitchen_Newid_helper helperClass=new Kitchen_Newid_helper(Kitchen_name,Kitchen_desc,Kitchen_style,Kitchen_address,fAuth.getCurrentUser().getUid());
        reference.child(user_id).setValue(helperClass);
        startActivity(new Intent(getApplicationContext(),KitchenFood.class));
    }
}