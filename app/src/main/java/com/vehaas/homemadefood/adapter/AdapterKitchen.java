package com.vehaas.homemadefood.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.vehaas.homemadefood.R;
import com.vehaas.homemadefood.activities.KitchenDetailsActivity;
import com.vehaas.homemadefood.model.ModelFood;
import com.vehaas.homemadefood.model.ModelKitchen;

import java.util.ArrayList;

public class AdapterKitchen extends RecyclerView.Adapter<AdapterKitchen.HolderKitchen>{
    private Context context;
    public ArrayList<ModelKitchen> kitchenlist;

    public AdapterKitchen(Context context, ArrayList<ModelKitchen> kitchenlist) {
        this.context = context;
        this.kitchenlist = kitchenlist;
    }

    @NonNull
    @Override
    public HolderKitchen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row
        View view= LayoutInflater.from(context).inflate(R.layout.row_kitchen,parent,false);
        return new HolderKitchen(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderKitchen holder, int position) {
        FirebaseAuth firebaseAuth;
        firebaseAuth=FirebaseAuth.getInstance();
        //get data
        ModelKitchen modelKitchen=kitchenlist.get(position);
        String kitchen_userId=modelKitchen.getKitchen_userId();
//        String uid=firebaseAuth.getCurrentUser().getUid();
//        String name=modelKitchen.getName();
        String kitchen_name=modelKitchen.getKitchen_name();
        String kitchen_phone=modelKitchen.getKitchen_phone();
        String kitchen_address= modelKitchen.getKitchen_address();

        //String kitchen_style= modelKitchen.getKitchen_style();

        //set data
        holder.kitchenNameTv.setText(""+kitchen_name);
        holder.phoneTv.setText("+91"+kitchen_phone);
        holder.addressTv.setText("Location:"+kitchen_address);

//        Log.d("TAG", "onBindViewHolder: "+kitchen_style);

        //handle on click listener,show kitchen details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, KitchenDetailsActivity.class);
                intent.putExtra("kitchenID",kitchen_userId);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return kitchenlist.size();//return number of records
    }

    //view holder
    class HolderKitchen extends RecyclerView.ViewHolder{

        //ui views of row_kitchen.xml
        private ImageView kitchenIv;
        private TextView kitchenNameTv,phoneTv,addressTv,kitchenstyleTv;
        public HolderKitchen(@NonNull View itemView) {
            super(itemView);
            //init uid views
            kitchenIv=itemView.findViewById(R.id.kitchenIv);
            kitchenNameTv=itemView.findViewById(R.id.kitchenNameTv);
            phoneTv=itemView.findViewById(R.id.phoneTv);
            addressTv=itemView.findViewById(R.id.addressTv);
            //kitchenstyleTv=itemView.findViewById(R.id.kitchenstyleTv);
        }
    }
}
