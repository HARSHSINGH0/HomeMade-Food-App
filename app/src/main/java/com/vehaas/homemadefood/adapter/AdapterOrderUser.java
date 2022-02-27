package com.vehaas.homemadefood.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vehaas.homemadefood.R;
import com.vehaas.homemadefood.activities.HomeFragment;
import com.vehaas.homemadefood.model.ModelOrderUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdapterOrderUser extends RecyclerView.Adapter<AdapterOrderUser.HolderOrderUser>{
    private Context context;
    private ArrayList<ModelOrderUser> orderUserList;

    public AdapterOrderUser(Context context,ArrayList<ModelOrderUser> orderUserList){
        this.context=context;
        this.orderUserList=orderUserList;

    }



    @NonNull
    @Override
    public HolderOrderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view= LayoutInflater.from(context).inflate(R.layout.row_order_user,parent,false);
        return new HolderOrderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderUser holder, int position) {
        //get data
        ModelOrderUser modelOrderUser=orderUserList.get(position);
        String orderId=modelOrderUser.getOrderId();
        String orderBy=modelOrderUser.getOrderBy();
        String orderCost=modelOrderUser.getOrderCost();
        String orderStatus=modelOrderUser.getOrderStatus();
        String orderTime=modelOrderUser.getOrderTime();
        String orderTo=modelOrderUser.getOrderTo();

        //get kitcheninfo
        loadKitchenInfo(modelOrderUser,holder);


        //set data
        holder.amountTv.setText("Amount: ₹"+orderCost);
        holder.statusTv.setText(orderStatus);
        holder.orderIdTv.setText("OrderID:"+orderId);

        //change order status text color
        if(orderStatus.equals("In Progress")){
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.green_color));
        }
        else if(orderStatus.equals("Completed")){
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.activeBottomNavIconColor));
        }
        else if(orderStatus.equals("Cancelled")){
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.activeBottomNavIconColor2));
        }

        //convert timestamp to proper format
//        Calendar calendar=Calendar.getInstance();
//        Log.d("TAG", "onBindViewHolder: "+calendar);
//        calendar.setTimeInMillis(Long.parseLong(orderTime));
//        String formatedDate=DateFormat.format(calendar).toString();//this can be error
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String formatedDate = format1.format(date);
        holder.dateTv.setText(formatedDate);
    }

    private void loadKitchenInfo(ModelOrderUser modelOrderUser, HolderOrderUser holder) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("kitchen");
        ref.child(modelOrderUser.getOrderTo())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String kitchenName=""+snapshot.child("kitchen_name").getValue();
                holder.kitchenNameTv.setText(kitchenName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderUserList.size();
    }

    //view holder class
    class HolderOrderUser extends RecyclerView.ViewHolder{
        //views of layout
        private TextView orderIdTv,dateTv,kitchenNameTv,amountTv,statusTv;

        public HolderOrderUser(@NonNull View itemView) {
            super(itemView);

            //init views of layout
            orderIdTv=itemView.findViewById(R.id.orderIdTv);
            dateTv=itemView.findViewById(R.id.dateTv);
            kitchenNameTv=itemView.findViewById(R.id.kitchenNameTv);
            amountTv=itemView.findViewById(R.id.amountTv);
            statusTv=itemView.findViewById(R.id.statusTv);

        }
    }
}
