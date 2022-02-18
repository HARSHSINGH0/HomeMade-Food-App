package com.vehaas.homemadefood.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vehaas.homemadefood.R;
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
        //get data
        ModelKitchen modelKitchen=kitchenlist.get(position);
        String uid=modelKitchen.getUid();
        String name=modelKitchen.getName();
        String kitchen_name=modelKitchen.getKitchen_name();
        String kitchen_phone=modelKitchen.getKitchen_phone();
        String Kitchen_style= modelKitchen.getKitchen_style();
        String kitchen_address= modelKitchen.getKitchen_address();

        //set data
        holder.kitchenNameTv.setText(kitchen_name);
        holder.phoneTv.setText(kitchen_phone);
        holder.addressTv.setText(kitchen_address);



    }

    @Override
    public int getItemCount() {
        return kitchenlist.size();//return number of records
    }

    //view holder
    class HolderKitchen extends RecyclerView.ViewHolder{

        //ui views of row_kitchen.xml
        private ImageView kitchenIv;
        private TextView kitchenNameTv,phoneTv,addressTv;
        public HolderKitchen(@NonNull View itemView) {
            super(itemView);
            //init uid views
            kitchenIv=itemView.findViewById(R.id.kitchenIv);
            kitchenNameTv=itemView.findViewById(R.id.kitchenNameTv);
            phoneTv=itemView.findViewById(R.id.phoneTv);
            addressTv=itemView.findViewById(R.id.addressTv);
        }
    }
}
