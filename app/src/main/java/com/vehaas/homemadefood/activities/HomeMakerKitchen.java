package com.vehaas.homemadefood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vehaas.homemadefood.Kitchen_Newid_helper;
import com.vehaas.homemadefood.R;

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

        ProgressBar pgb=findViewById(R.id.progressBar2);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(fAuth.getUid().toString())) {
                    startActivity(new Intent(getApplicationContext(),KitchenFood.class));
                    // run some code
                }
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
        EditText phone_number=findViewById(R.id.edt_phoneNumber);

//        ImageView img_kitchen=findViewById(R.id.image_kitchen);

        FirebaseAuth fAuth;
        fAuth = FirebaseAuth.getInstance();
        String user_id = fAuth.getCurrentUser().getUid();
        String Kitchen_name =kitchen_name.getText().toString().trim();
        String Kitchen_desc =desc.getText().toString().trim();
        String Kitchen_style =food_style.getText().toString().trim();
        String Kitchen_address =kitchen_address.getText().toString().trim();
        String kitchen_phone =phone_number.getText().toString().trim();

        FirebaseDatabase rootNode;
        DatabaseReference reference;
        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference("kitchen");
        Kitchen_Newid_helper helperClass=new Kitchen_Newid_helper(Kitchen_name,Kitchen_desc,Kitchen_style,Kitchen_address,kitchen_phone,fAuth.getCurrentUser().getUid());
        reference.child(user_id).setValue(helperClass);

        startActivity(new Intent(getApplicationContext(),KitchenFood.class));
    }
}