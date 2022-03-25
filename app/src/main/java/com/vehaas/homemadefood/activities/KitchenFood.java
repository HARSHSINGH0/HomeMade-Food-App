package com.vehaas.homemadefood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vehaas.homemadefood.adapter.AdapterFoodSeller;
import com.vehaas.homemadefood.Constants;
import com.vehaas.homemadefood.adapter.AdapterOrderKitchen;
import com.vehaas.homemadefood.model.ModelFood;
import com.vehaas.homemadefood.R;
import com.vehaas.homemadefood.model.ModelOrderKitchen;

import java.util.ArrayList;

public class KitchenFood extends AppCompatActivity {
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
    private RelativeLayout orderTabRL;

    //order
    private TextView filteredOrderTv;
    private ImageButton filterOrderBtn;
    private RecyclerView ordersRv;
    private ArrayList<ModelOrderKitchen> orderKitchenArrayList;
    private AdapterOrderKitchen adapterOrderKitchen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_food);
        tabFood=findViewById(R.id.tabFood);
        tabOrder=findViewById(R.id.tabOrder);
        foodTabRL=findViewById(R.id.foodTabRL);
        orderTabRL=findViewById(R.id.orderTabRL);

        searchFoodEt=findViewById(R.id.searchFoodEt);
        filterFoodBtn=findViewById(R.id.filterFoodBtn);
        filterFoodTv=findViewById(R.id.filterFoodTv);
        foodRv=findViewById(R.id.foodRv);
        //order
        filteredOrderTv=findViewById(R.id.filteredOrderTv);
        filterOrderBtn=findViewById(R.id.filterOrderBtn);
        ordersRv=findViewById(R.id.ordersRv);
        fAuth=FirebaseAuth.getInstance();
        loadAllFood();
        loadAllOrders();
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
                AlertDialog.Builder builder=new AlertDialog.Builder(KitchenFood.this);
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
        filterOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //options to display in dialog
                String[] options={"ALL","In Progress","Completed","Cancelled"};
                //dialog
                AlertDialog.Builder builder=new AlertDialog.Builder(KitchenFood.this);
                builder.setTitle("Filter Orders:")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //handle item clicks
                                if(i==0){
                                    //All clicked
                                    filteredOrderTv.setText("Showing All Orders");
                                    adapterOrderKitchen.getFilter().filter("");//show all orders
                                }
                                else{
                                    String optionClicked=options[i];
                                    filteredOrderTv.setText("Showing "+optionClicked+" Orders");//eg.showing completed orders
                                    adapterOrderKitchen.getFilter().filter(optionClicked);
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void loadAllOrders() {
        //init array list
        orderKitchenArrayList=new ArrayList<>();

        //load orders of shop
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("kitchen");
        ref.child(fAuth.getUid()).child("orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //clear list before adding new data in it
                        orderKitchenArrayList.clear();
                        for (DataSnapshot ds:snapshot.getChildren()){
                            ModelOrderKitchen modelOrderKitchen=ds.getValue(ModelOrderKitchen.class);
                            //add to list
                            orderKitchenArrayList.add(modelOrderKitchen);

                        }
                        //setup adapter
                        adapterOrderKitchen=new AdapterOrderKitchen(KitchenFood.this,orderKitchenArrayList);
                        //set adapter to recyclerview
                        ordersRv.setAdapter(adapterOrderKitchen);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    //now create a filter class, we already have one,lets copy paste and change according to our adapter


    private void loadFilteredFood(String selected) {
        foodList=new ArrayList<>();
        //get all products
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("kitchen");
        reference.child(fAuth.getUid()).child("foods")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //before getting reset list
                        for (DataSnapshot ds:dataSnapshot.getChildren()){
                            String foodTiming=""+ds.child("timing").getValue();
                            if(selected.equals(foodTiming)){
                                ModelFood modelFood=ds.getValue(ModelFood.class);
                                foodList.add(modelFood);
                            }


                        }
                        //setup adapter
                        adapterFoodSeller=new AdapterFoodSeller(KitchenFood.this,foodList);
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
                        adapterFoodSeller=new AdapterFoodSeller(KitchenFood.this,foodList);
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
        foodTabRL.setVisibility(View.VISIBLE);
        orderTabRL.setVisibility(View.GONE);
        tabFood.setTextColor(getResources().getColor(R.color.black));
        tabFood.setBackgroundResource(R.drawable.shape_rect01);
        tabOrder.setTextColor(getResources().getColor(R.color.white));
        tabOrder.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
    private void showOrderTabUI() {
        //show order ui and hide food ui
        foodTabRL.setVisibility(View.GONE);
        orderTabRL.setVisibility(View.VISIBLE);
        tabFood.setTextColor(getResources().getColor(R.color.white));
        tabFood.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        tabOrder.setTextColor(getResources().getColor(R.color.black));
        tabOrder.setBackgroundResource(R.drawable.shape_rect01);
    }

    public void add_food_button(View view) {
        startActivity(new Intent(getApplicationContext(), KitchenOrders.class));
    }

    public void add_food_button_back(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}