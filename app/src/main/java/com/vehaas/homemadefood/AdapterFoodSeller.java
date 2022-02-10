package com.vehaas.homemadefood;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterFoodSeller extends RecyclerView.Adapter<AdapterFoodSeller.HolderFoodSeller> implements Filterable {
    private Context context;
    public ArrayList<ModelFood> foodList,filterList;
    private FilterFood filter;

    public AdapterFoodSeller(Context context, ArrayList<ModelFood> foodList) {
        this.context = context;
        this.foodList = foodList;
        this.filterList=foodList;
    }

    @NonNull
    @Override
    public HolderFoodSeller onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view= LayoutInflater.from(context).inflate(R.layout.row_food_seller,parent,false);
        return new HolderFoodSeller(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFoodSeller holder, int position) {
        //get data
        ModelFood modelFood= foodList.get(position);
        String id=modelFood.getFoodId();
        String uid=modelFood.getUserID();
        String foodDescription=modelFood.getFoodDesc();
        String timing=modelFood.getTiming();
        String icon=modelFood.getFoodIcon();
        String quantity=modelFood.getFoodQuantity();
        String title=modelFood.getFoodName();
        String timestamp=modelFood.getTimestamp();
        String orignalPrice=modelFood.getOriginalPrice();

        //set data
        holder.titleTv.setText(title);
        holder.quantityTv.setText(quantity);
        holder.orignalPriceTv.setText(orignalPrice);
        try {
            Picasso.with(context.getApplicationContext()).load(icon).placeholder(R.drawable.ic_baseline_shopping_cart_24).into(holder.foodImgIv);//this line can be error
        }
        catch (Exception e){
            holder.foodImgIv.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //handle item clicks,show item details


            }
        });

    }

    @Override
    public int getItemCount() {
        return foodList.size();//this can be an error
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new FilterFood(this,filterList);
        }
        return filter;
    }


    class HolderFoodSeller extends RecyclerView.ViewHolder{
        /*holds a view of Recyclerview*/
        private ImageView foodImgIv;
        private TextView titleTv,quantityTv,orignalPriceTv;
        public HolderFoodSeller(@NonNull View itemView) {
            super(itemView);
            foodImgIv=itemView.findViewById(R.id.foodImgIv);
            titleTv=itemView.findViewById(R.id.titleTv);
            quantityTv=itemView.findViewById(R.id.quantityTv);
            orignalPriceTv=itemView.findViewById(R.id.orignalPriceTv);



        }
    }
}
