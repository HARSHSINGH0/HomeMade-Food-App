package com.vehaas.homemadefood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.vehaas.homemadefood.R;
import com.vehaas.homemadefood.adapter.AdapterOrderedItem;
import com.vehaas.homemadefood.model.ModelOrderedItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class OrderDetailsSellerActivtity extends AppCompatActivity {
    //order ui
    public ImageButton backBtn,editBtn;
    private TextView orderIdTv,dateTv,orderStatusTv,phoneTv,totalItemsTv,amountTv,emailTv;
    private RecyclerView itemsRv;
    String orderId,orderBy;
    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelOrderedItem> orderedItemArrayList;//this will be error
    private AdapterOrderedItem adapterOrderedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_seller_activtity);

        //init ui views
        backBtn=findViewById(R.id.backBtn);
        editBtn=findViewById(R.id.editBtn);
        orderIdTv=findViewById(R.id.orderIdTv);
        dateTv=findViewById(R.id.dateTv);
        orderStatusTv=findViewById(R.id.orderStatusTv);
        phoneTv=findViewById(R.id.phoneTv);
        totalItemsTv=findViewById(R.id.totalItemsTv);
        amountTv=findViewById(R.id.amountTv);
//        emailTv=findViewById(R.id.emailTv);
        itemsRv=findViewById(R.id.itemsRv);

        //get data from intent
        orderId=getIntent().getStringExtra("orderId");
        orderBy=getIntent().getStringExtra("orderBy");
        Log.d("TAG", "onCreate: "+orderBy);

        firebaseAuth=FirebaseAuth.getInstance();
//        loadMyinfo();
        //loadBuyerinfo();
        loadOrderDetails();
        loadOrderedItems();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go back
                onBackPressed();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit order status:In Progress,Completed,Cancelled
                editOrderStatusDialog();
            }
        });

    }

    private void editOrderStatusDialog() {
        //options to display in dialog
        String[] options={"In Progress","Completed","Cancelled"};
        //dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Edit Order Status")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //handle item clicks
                        String selectedOptions=options[i];
                        editOrderStatus(selectedOptions);
                    }
                }).show();
    }

    private void editOrderStatus(String selectedOption) {
        //setup data to put in firebase db
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("orderStatus",""+selectedOption);
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("kitchen");
        ref.child(firebaseAuth.getUid()).child("orders").child(orderId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //status updated
                        Toast.makeText(OrderDetailsSellerActivtity.this,"Order is now "+selectedOption,Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed updating status,show reason
                        Toast.makeText(OrderDetailsSellerActivtity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadOrderDetails() {
        //load detailed info of this order based on order id
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("kitchen");
        ref.child(firebaseAuth.getUid()).child("orders").child(orderId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get order info
                        String orderBy=""+snapshot.child("orderBy").getValue();
                        String orderCost=""+snapshot.child("orderCost").getValue();
                        String orderId=""+snapshot.child("orderId").getValue();
                        String orderStatus=""+snapshot.child("orderStatus").getValue();
                        String orderTIme=""+snapshot.child("orderTime").getValue();
                        String orderTo=""+snapshot.child("orderTo").getValue();

                        //convert timestamp
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, 1);
                        Date date = cal.getTime();
                        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                        String formatedDate = format1.format(date);

                        //order status
                        if(orderStatus.equals("In Progress")){
                            orderStatusTv.setTextColor(getResources().getColor(R.color.green_color));
                        }
                        else if(orderStatus.equals("Completed")){
                            orderStatusTv.setTextColor(getResources().getColor(R.color.activeBottomNavIconColor2));
                        }
                        else if(orderStatus.equals("Cancelled")){
                            orderStatusTv.setTextColor(getResources().getColor(R.color.activeBottomNavIconColor));
                        }


                        //set data
                        orderIdTv.setText(orderId);
                        orderStatusTv.setText(orderStatus);
                        amountTv.setText("₹"+orderCost);
                        dateTv.setText(formatedDate);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

//    private void loadBuyerinfo() {
//        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("kitchen");
//        ref.child(orderBy).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //get buyer info
//                String email=""+snapshot.child("email").getValue();
//                String phone=""+snapshot.child("phone").getValue();
//                //set info
//                emailTv.setText(email);
//                phoneTv.setText(phone);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    private void loadMyinfo() {
//        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("kitchen");
//        ref.child(firebaseAuth.getUid())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        //testing
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//    }
    private void loadOrderedItems(){
        //load the foods of order
        //init list
        orderedItemArrayList=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("kitchen");
        ref.child(firebaseAuth.getUid()).child("orders").child(orderId).child("Items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        orderedItemArrayList.clear();//before adding data clear list
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            ModelOrderedItem modelOrderedItem=ds.getValue(ModelOrderedItem.class);
                            //add to list
                            //Log.d("TAG", "onDataChange: "+modelOrderedItem.getName());
                            orderedItemArrayList.add(modelOrderedItem);
                        }
//                        //setup adapter
                        adapterOrderedItem=new AdapterOrderedItem(OrderDetailsSellerActivtity.this,orderedItemArrayList);
//                        //set adapter to our recyclerview
                        itemsRv.setAdapter(adapterOrderedItem);
//                        //set total numberof items/products in order
                        totalItemsTv.setText(""+dataSnapshot.getChildrenCount());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}