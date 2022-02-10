

package com.vehaas.homemadefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KitchenFoodTest extends AppCompatActivity {
    private EditText searchFoodEt;
    private ImageButton filterFoodBtn;
    private TextView filterFoodTv;
    private RecyclerView foodRv;
    private ArrayList<ModelFood> foodList;
    private AdapterFoodSeller adapterFoodSeller;

    private TextView tabFood,tabOrder;
    private FirebaseAuth fAuth;
    private ProgressDialog progressDialog;
    private RelativeLayout foodTabRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_food_test);
        tabFood=findViewById(R.id.tabFood);
        tabOrder=findViewById(R.id.tabOrder);
        foodTabRL=findViewById(R.id.foodTabRL);

        searchFoodEt=findViewById(R.id.searchFoodEt);
        filterFoodBtn=findViewById(R.id.filterFoodBtn);
        filterFoodTv=findViewById(R.id.filterFoodTv);
        foodRv=findViewById(R.id.foodRv);


        fAuth=FirebaseAuth.getInstance();

        loadAllFood();
        Log.d("TAG", "onCreate: before showFoodTabUI button it is working");
        showFoodTabUI();
        searchFoodEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    adapterFoodSeller.getFilter().filter(charSequence);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tabFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //load products
                showFoodTabUI();
            }
        });
        tabOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //load orders
                showOrderTabUI();
            }
        });
        filterFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(KitchenFoodTest.this);
                builder.setTitle("Choose Timing:")
                        .setItems(Constants.foodTiming, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String selected=Constants.foodTiming1[i];
                                filterFoodTv.setText(selected);
                                if(selected.equals("ALL")){
                                    //load all
                                    loadAllFood();
                                }
                                else{
                                    loadFilteredFood(selected);
                                }
                            }
                        }).show();
            }
        });
    }
    private void loadFilteredFood(String selected) {
        foodList=new ArrayList<>();
        //get all products
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("kitchen");
        reference.child(fAuth.getUid()).child("foods")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //before getting reset list
                        for (DataSnapshot ds:dataSnapshot.getChildren()){
                            String foodTiming=""+ds.child("foodTiming").getValue();
                            if(selected.equals(foodTiming)){
                                ModelFood modelFood=ds.getValue(ModelFood.class);
                                foodList.add(modelFood);
                            }


                        }
                        //setup adapter
                        adapterFoodSeller=new AdapterFoodSeller(KitchenFoodTest.this,foodList);
                        //set adapter
                        foodRv.setAdapter(adapterFoodSeller);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllFood() {
        foodList=new ArrayList<>();
        //get all products
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("kitchen");
        reference.child(fAuth.getUid()).child("foods")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //before getting reset list
                        for (DataSnapshot ds:dataSnapshot.getChildren()){
                            ModelFood modelFood=ds.getValue(ModelFood.class);
                            foodList.add(modelFood);

                        }
                        //setup adapter
                        adapterFoodSeller=new AdapterFoodSeller(KitchenFoodTest.this,foodList);
                        //set adapter
                        foodRv.setAdapter(adapterFoodSeller);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void showFoodTabUI() {

        //show products ui and hide orders ui
//        foodTabRL.setVisibility(View.VISIBLE);
        Log.d("TAG", "showFoodTabUI: this is working");
//        ordersTabRL.setVisibility(View.GONE);
        tabFood.setTextColor(getResources().getColor(R.color.black));
        tabFood.setBackgroundResource(R.drawable.shape_rect01);
        tabOrder.setTextColor(getResources().getColor(R.color.white));
        tabOrder.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
    private void showOrderTabUI() {
        //show order ui and hide food ui
//        foodTabRL.setVisibility(View.GONE);
//        ordersTabRL.setVisibility(View.VISIBLE);
        tabFood.setTextColor(getResources().getColor(R.color.white));
        tabFood.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        tabOrder.setTextColor(getResources().getColor(R.color.black));
        tabOrder.setBackgroundResource(R.drawable.shape_rect01);
    }

    public void add_food_button(View view) {
        startActivity(new Intent(getApplicationContext(),KitchenOrders.class));
    }

    public void add_food_button_back(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}