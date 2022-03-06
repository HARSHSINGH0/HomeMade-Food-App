package com.vehaas.homemadefood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vehaas.homemadefood.R;
import com.vehaas.homemadefood.model.ModelOrderedItem;

import java.util.ArrayList;

public class AdapterOrderedItem extends RecyclerView.Adapter<AdapterOrderedItem.HolderOrderedItem>{
    private Context context;
    private ArrayList<ModelOrderedItem> orderedItemArrayList;

    public AdapterOrderedItem(Context context, ArrayList<ModelOrderedItem> orderedItemArrayList) {
        this.context = context;
        this.orderedItemArrayList = orderedItemArrayList;
    }

    @NonNull
    @Override
    public HolderOrderedItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//not problem in this code
        //inflate layout

        View view=LayoutInflater.from(context).inflate(R.layout.row_ordereditem,parent,false);
        return new HolderOrderedItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderedItem holder, int position) {//not problem in this
        //get data position
        ModelOrderedItem modelOrderedItem=orderedItemArrayList.get(position);
        String getPid=modelOrderedItem.getpId();
        String name=modelOrderedItem.getName();
        String cost=modelOrderedItem.getCost();
        String price=modelOrderedItem.getPrice();
        String quantity=modelOrderedItem.getQuantity();
        //set data
        //edited
        holder.itemTitleTv.setText(name);
        holder.itemPriceTv.setText("₹"+price);
        holder.itemPriceEachTv.setText("₹"+cost);
        holder.itemQuantityTv.setText("x"+quantity);
    }

    @Override
    public int getItemCount() {
        return orderedItemArrayList.size();//return list size
    }

    //view holder class

    //edited
    class HolderOrderedItem extends RecyclerView.ViewHolder{
        //views of row_ordereditem.xml
        private TextView itemTitleTv,itemPriceTv,itemPriceEachTv,itemQuantityTv;
        public HolderOrderedItem(@NonNull View itemView){
            super(itemView);
            //init views
            itemTitleTv=itemView.findViewById(R.id.itemTitleTv);
            itemPriceTv=itemView.findViewById(R.id.itemPriceTv);
            itemPriceEachTv=itemView.findViewById(R.id.itemPriceEachTv);
            itemQuantityTv=itemView.findViewById(R.id.itemQuantityTv);
        }
    }
}
