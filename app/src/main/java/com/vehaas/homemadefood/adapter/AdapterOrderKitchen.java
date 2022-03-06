package com.vehaas.homemadefood.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.vehaas.homemadefood.FilterOrderKitchen;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vehaas.homemadefood.FilterOrderKitchen;
import com.vehaas.homemadefood.R;
import com.vehaas.homemadefood.activities.OrderDetailsSellerActivtity;
import com.vehaas.homemadefood.model.ModelOrderKitchen;
import com.vehaas.homemadefood.model.ModelOrderUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdapterOrderKitchen extends RecyclerView.Adapter<AdapterOrderKitchen.HolderOrderKitchen>  implements Filterable {
    private Context context;
    public ArrayList<ModelOrderKitchen> orderKitchenArrayList,filterList;
    private FilterOrderKitchen filter;

    public AdapterOrderKitchen(Context context, ArrayList<ModelOrderKitchen> orderKitchenArrayList) {
        this.context = context;
        this.orderKitchenArrayList = orderKitchenArrayList;
        this.filterList = orderKitchenArrayList;
    }

    @NonNull
    @Override
    public HolderOrderKitchen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view= LayoutInflater.from(context).inflate(R.layout.row_order_seller,parent,false);


        return new HolderOrderKitchen(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderKitchen holder, int position) {
        //get data at position
        ModelOrderKitchen modelOrderKitchen=orderKitchenArrayList.get(position);
        String orderId=modelOrderKitchen.getOrderId();
        String orderBy=modelOrderKitchen.getOrderBy();
        String orderTo=modelOrderKitchen.getOrderTo();
        String orderCost=modelOrderKitchen.getOrderCost();
        String orderStatus=modelOrderKitchen.getOrderStatus();
        String orderTime=modelOrderKitchen.getOrderTime();
//
//        //load user/buyer info
//        loadUserInfo(modelOrderKitchen,holder);
        //loadUserInfo(modelOrderKitchen,holder);

        //set data
        holder.amountTv.setText("Amount: ₹"+orderCost);
        holder.statusTv.setText(orderStatus);
        holder.orderIdTv.setText("Order ID:"+orderId);

        //change order status text color
        if (orderStatus.equals("In Progress")){
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.green_color));
        }
        else if (orderStatus.equals("Completed")){
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.activeBottomNavIconColor2));
        }
        else if (orderStatus.equals("Cancelled")){
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.activeBottomNavIconColor));
        }
//
        //convert time to proper format e.g. dd/mm/yyyy
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String formatedDate = format1.format(date);
        holder.orderDateTv.setText(formatedDate);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open order details
                Intent intent=new Intent(context, OrderDetailsSellerActivtity.class);
                intent.putExtra("orderId",orderId); //to load order info
                intent.putExtra("orderBy",orderBy); //to load info of the user who places order
                context.startActivity(intent);

            }
        });
    }



    private void loadUserInfo(ModelOrderKitchen modelOrderKitchen,HolderOrderKitchen holder) {
        //to load email of the user/buyer:modelOrderKitchen.getOrderBy() contains uid of that user/buyer
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("kitchen");
        ref.child(modelOrderKitchen.getOrderBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email=""+snapshot.child("email").getValue();
                        //holder.emailTv.setText(email);//set this tommorow as userid
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
//                    holder.emailTv.setText(documentSnapshot.getString("email"));//this is printing email of admin
//
//                }
//            }
//
//
//        });

    }

    @Override
    public int getItemCount() {
        return orderKitchenArrayList.size();//returns the size of list/number of records
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            //init filter
            filter=new FilterOrderKitchen(this,filterList);
        }
        return filter;
    }


    //view holder class for row_order_seller.xml
    class HolderOrderKitchen extends RecyclerView.ViewHolder{

        //ui views of row_order_seller.xml
        private TextView orderIdTv,orderDateTv,emailTv,amountTv,statusTv;
        private ImageView nextIv;
        public HolderOrderKitchen(@NonNull View itemView) {
            super(itemView);
            orderIdTv=itemView.findViewById(R.id.orderIdTv);
            orderDateTv=itemView.findViewById(R.id.orderDateTv);
            amountTv=itemView.findViewById(R.id.amountTv);
            statusTv=itemView.findViewById(R.id.statusTv);
        }

    }



}
