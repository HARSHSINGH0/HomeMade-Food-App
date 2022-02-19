package com.vehaas.homemadefood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vehaas.homemadefood.Constants;
import com.vehaas.homemadefood.R;
import com.vehaas.homemadefood.adapter.AdapterFoodSeller;
import com.vehaas.homemadefood.adapter.AdapterFoodUser;
import com.vehaas.homemadefood.adapter.AdapterKitchen;
import com.vehaas.homemadefood.model.ModelFood;

import java.util.ArrayList;

public class KitchenDetailsActivity extends AppCompatActivity {
    //declare ui views
    private TextView kitchenNameTv,phoneTv,addressTv,filteredFoodTv,kitchenstyleTv,addToCartTv;
    private ImageButton backBtn,callBtn;
    private EditText searchFoodEt;
    private ImageButton filterFoodBtn;
    private RecyclerView foodsRv;

    private String kitchenUid;
    private String kitchenName,kitchenPhone,kitchenAddress,kitchenStyle;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelFood> foodsList;
    private AdapterFoodUser adapterFoodUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_details);
        //init ui views
        kitchenNameTv=findViewById(R.id.kitchenNameTv);
        phoneTv=findViewById(R.id.phoneTv);
        addressTv=findViewById(R.id.addressTv);
        kitchenstyleTv=findViewById(R.id.styleTv);
        backBtn=findViewById(R.id.backBtn);
        callBtn=findViewById(R.id.callBtn);
        searchFoodEt=findViewById(R.id.searchFoodEt);
        filterFoodBtn=findViewById(R.id.filterFoodBtn1);
        filteredFoodTv=findViewById(R.id.filteredFoodTv);
        foodsRv=findViewById(R.id.foodsRv);

        //get uid of the kitchen from intent
        kitchenUid=getIntent().getStringExtra("kitchenID");
        firebaseAuth=FirebaseAuth.getInstance();
        loadKitchenDetails();
        loadKitchenFoods();

        searchFoodEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    adapterFoodUser.getFilter().filter(charSequence);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to previous activity
                onBackPressed();
            }
        });
//        addToCartTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });S
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialPhone();
            }
        });
        filterFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(KitchenDetailsActivity.this);
                builder.setTitle("Choose Timing:")
                        .setItems(Constants.foodTiming, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String selected=Constants.foodTiming1[i];
                                filteredFoodTv.setText(selected);
                                if(selected.equals("ALL")){
                                    //load all
                                    loadKitchenFoods();
                                }
                                else{
                                    //load filtered
                                    adapterFoodUser.getFilter().filter(selected);
                                }
                            }
                        }).show();
            }
        });
    }

    private void dialPhone() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(kitchenPhone))));
        Toast.makeText(this,""+kitchenPhone,Toast.LENGTH_SHORT).show();
    }

    private void loadKitchenDetails() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("kitchen");
        ref.child(kitchenUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get kitchen data
                kitchenName=""+snapshot.child("kitchen_name").getValue();
                kitchenPhone=""+snapshot.child("kitchen_phone").getValue();
                kitchenAddress=""+snapshot.child("kitchen_address").getValue();

                kitchenStyle=""+snapshot.child("kitchen_style").getValue();
                //set kitchen data
                kitchenNameTv.setText(kitchenName);
                phoneTv.setText("Phone: ₹"+kitchenPhone);
                addressTv.setText("Address:\n"+kitchenAddress);
                //kitchenstyleTv.setText("Food Style:\n"+kitchenStyle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadKitchenFoods() {
        //init list
        foodsList=new ArrayList<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("kitchen");

        reference.child(kitchenUid).child("foods")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //clearing this list before adding item
                        foodsList.clear();

                        Log.d("TAG", "loadKitchenFoods: this is running");

                        for(DataSnapshot ds:snapshot.getChildren()){
                            ModelFood modelFood=ds.getValue(ModelFood.class);

                            foodsList.add(modelFood);

                        }
                        //setup adapter
                        adapterFoodUser=new AdapterFoodUser(KitchenDetailsActivity.this,foodsList);
                        //set adapter
                        foodsRv.setAdapter(adapterFoodUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}