package com.vehaas.homemadefood.activities;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vehaas.homemadefood.Constants;
import com.vehaas.homemadefood.R;
import com.vehaas.homemadefood.adapter.AdapterCartItem;
import com.vehaas.homemadefood.adapter.AdapterFoodUser;
import com.vehaas.homemadefood.model.ModelCartItem;
import com.vehaas.homemadefood.model.ModelFood;

import java.util.ArrayList;
import java.util.HashMap;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class KitchenDetailsActivity extends AppCompatActivity {


    //declare ui views
    private TextView kitchenNameTv,phoneTv,addressTv,filteredFoodTv,styleTv;
    private ImageButton backBtn,callBtn,cartBtn;
    private EditText searchFoodEt;
    private ImageButton filterFoodBtn;
    private RecyclerView foodsRv;

    private String kitchenUid;
    private String kitchenName,kitchenPhone,kitchenAddress,kitchenStyle;
    private FirebaseAuth firebaseAuth;
    //progress dialog
    private ProgressDialog progressDialog;
    private ArrayList<ModelFood> foodsList;
    private AdapterFoodUser adapterFoodUser;

    //cart
    private ArrayList<ModelCartItem> cartItemList;
    private AdapterCartItem adapterCartItem;

    @MainThread
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeFragment.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_details);
        //init ui views
        kitchenNameTv=findViewById(R.id.kitchenNameTv);
        phoneTv=findViewById(R.id.phoneTv);
        addressTv=findViewById(R.id.addressTv);
        styleTv=findViewById(R.id.styleTv);
        backBtn=findViewById(R.id.backBtn);
        callBtn=findViewById(R.id.callBtn);
        searchFoodEt=findViewById(R.id.searchFoodEt);
        filterFoodBtn=findViewById(R.id.filterFoodBtn1);
        filteredFoodTv=findViewById(R.id.filteredFoodTv);
        foodsRv=findViewById(R.id.foodsRv);
        cartBtn=findViewById(R.id.cartBtn);

        //init progress dialog
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);


        //get uid of the kitchen from intent
        kitchenUid=getIntent().getStringExtra("kitchenID");
        firebaseAuth=FirebaseAuth.getInstance();
        loadKitchenDetails();
        loadKitchenFoods();

        //each shop has its own foods and orders so if user add items to cart and go back and open cart in different shop then cart should be different
        //so delete cart data whenever user open this activity
        deleteCartData();

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
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show cart
                showCartDialog();
            }
        });


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

    private void deleteCartData() {
        EasyDB easyDB=EasyDB.init(this,"ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id",new String[]{"text","unique"}))
                .addColumn(new Column("Item_PID",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Name",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price_Each",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Quantity",new String[]{"text","not null"}))
                .doneTableColumn();
        easyDB.deleteAllDataFromTable();//delete all records from cart
    }

    public double allTotalPrice=0.00;
    //need to access these views in adapter so making public
    public TextView sTotalTv;

    private void showCartDialog() {
        //init list
        cartItemList=new ArrayList<>();

        //inflate cart layout
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_cart,null);
        Log.d("TAG", "showCartDialog: 1");
        //init views
        TextView kitchenNameTv=view.findViewById(R.id.kitchenNameTv);
        Log.d("TAG", "showCartDialog: 2");

        RecyclerView cartItemRv=view.findViewById(R.id.cartItemRv);
        Log.d("TAG", "showCartDialog: 3");

        sTotalTv=view.findViewById(R.id.sTotalTv);
        Log.d("TAG", "showCartDialog: 4");

        Button checkOutBtn=view.findViewById(R.id.checkOutBtn);
        Log.d("TAG", "showCartDialog: 5");


        //dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        Log.d("TAG", "showCartDialog: 6");

        //set view to dialog
        builder.setView(view);
        Log.d("TAG", "showCartDialog: 7");

        kitchenNameTv.setText(kitchenName);
        Log.d("TAG", "showCartDialog: 8");

        EasyDB easyDB=EasyDB.init(this,"ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id",new String[]{"text","unique"}))
                .addColumn(new Column("Item_PID",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Name",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price_Each",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Quantity",new String[]{"text","not null"}))
                .doneTableColumn();
        Log.d("TAG", "showCartDialog: 9");

        //get all the records from db
        Cursor res=easyDB.getAllData();
        Log.d("TAG", "showCartDialog: 10");

        while (res.moveToNext()){
            String id=res.getString(1);
            String pId=res.getString(2);
            String name=res.getString(3);
            String price=res.getString(4);
            String cost=res.getString(5);
            String quantity=res.getString(6);
            allTotalPrice=allTotalPrice+Double.parseDouble(cost);
            ModelCartItem modelCartItem=new ModelCartItem(
                    ""+id,
                    ""+pId,
                    ""+name,
                    ""+price,
                    ""+cost,
                    ""+quantity);
            cartItemList.add(modelCartItem);
        }
        Log.d("TAG", "showCartDialog: 11");

        //setup adapter
        adapterCartItem=new AdapterCartItem(this,cartItemList);
        Log.d("TAG", "showCartDialog: 12");

        //set to recyclerview
        cartItemRv.setAdapter(adapterCartItem);
        Log.d("TAG", "showCartDialog: 13");


        sTotalTv.setText("₹"+String.format("%.2f",allTotalPrice));
        //reset total price on dismmis
        AlertDialog dialog=builder.create();
        dialog.show();
        //reset total price on dialog dismmis
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                allTotalPrice=0.00;
            }
        });
        //place order
        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //first validate delivery address
                if(cartItemList.size()==0){
                    //cart is empty
                    Toast.makeText(KitchenDetailsActivity.this,"No item in the cart",Toast.LENGTH_SHORT).show();
                    return;//dont proceed further

                }
                submitOrder();
            }
        });


    }

    private void submitOrder() {
        //progress dialog
        progressDialog.setMessage("Placing order...");
        progressDialog.show();
        //for order id and order time
        String timestamp=""+System.currentTimeMillis();
        String cost=sTotalTv.getText().toString().trim().replace("₹","");//remove ₹ if contains
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("orderId",""+timestamp);
        hashMap.put("orderTime",""+timestamp);
        hashMap.put("orderStatus","In Progress");//In progress/complete/cancelled
        hashMap.put("orderCost",""+cost);
        hashMap.put("orderBy",""+firebaseAuth.getUid());
        hashMap.put("orderTo",""+kitchenUid);
//        //test add to db
//        FirebaseAuth fAuth;
//        FirebaseFirestore fStore;
//        fAuth = FirebaseAuth.getInstance();
//        fStore= FirebaseFirestore.getInstance();
//        String userID = fAuth.getCurrentUser().getUid();
//        DocumentReference documentReference=fStore.collection("users").document(""+fAuth.getUid());
//
//
//        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists()){
//                    String email=(documentSnapshot.getString("email"));
//                    String name=(documentSnapshot.getString("name"));
//                    String phone=(documentSnapshot.getString("phone"));
//
//                    FirebaseDatabase rootNode;
//                    DatabaseReference reference;
//                    rootNode=FirebaseDatabase.getInstance();
//                    reference=rootNode.getReference("kitchen");
//                    HashMap<String,String> hashMap1=new HashMap<>();
//                    hashMap1.put("name",name);
//                    hashMap1.put("email",email);
//                    hashMap1.put("phone",phone);
////                    Newid_helper helperClass=new Newid_helper(name,email,phone);
//                    reference.child("name").push().setValue(name);
//                    reference.child("email").push().setValue(email);
//                    reference.child("phone").push().setValue(phone);
//
//
//
//
//                }}});
        //add to db
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("kitchen").child(kitchenUid).child("orders");//this was orders
        ref.child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //order info added now add order items
                        for (int i=0;i<cartItemList.size();i++){
                            String pId=cartItemList.get(i).getpId();
                            String id=cartItemList.get(i).getId();
                            String cost=cartItemList.get(i).getCost();
                            String name=cartItemList.get(i).getName();
                            String price=cartItemList.get(i).getPrice();
                            String quantity=cartItemList.get(i).getQuantity();
                            HashMap<String,String> hashMap1=new HashMap<>();
                            hashMap1.put("pId",pId);
                            hashMap1.put("name",name);
                            hashMap1.put("cost",cost);
                            hashMap1.put("price",price);
                            hashMap1.put("quantity",quantity);

                            ref.child(timestamp).child("Items").child(pId).setValue(hashMap1);
                        }
                        progressDialog.dismiss();
                        Toast.makeText(KitchenDetailsActivity.this,"Order Placed Successfully.....",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed placing order
                        progressDialog.dismiss();
                        Toast.makeText(KitchenDetailsActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
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
                styleTv.setText("Food Style:"+kitchenStyle);
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

        reference.child(kitchenUid).child("foods")//this was order
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //clearing this list before adding item
                        foodsList.clear();


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