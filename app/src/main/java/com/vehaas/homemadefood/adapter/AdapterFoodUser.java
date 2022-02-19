package com.vehaas.homemadefood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vehaas.homemadefood.FilterFoodUser;
import com.vehaas.homemadefood.R;
import com.vehaas.homemadefood.activities.KitchenDetailsActivity;
import com.vehaas.homemadefood.model.ModelFood;

import java.util.ArrayList;

public class AdapterFoodUser extends RecyclerView.Adapter<AdapterFoodUser.HolderFoodUser> implements Filterable {
    private Context context;
    public ArrayList<ModelFood> foodsList,filterList;
    private FilterFoodUser filter;

    public AdapterFoodUser(Context context, ArrayList<ModelFood> foodsList) {
        this.context = context;
        this.foodsList = foodsList;
        this.filterList = foodsList;
    }



    @NonNull
    @Override
    public HolderFoodUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view= LayoutInflater.from(context).inflate(R.layout.row_food_user,parent,false);

        return new HolderFoodUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFoodUser holder, int position) {
        //get data
        ModelFood modelFood=foodsList.get(position);
        String foodId=modelFood.getFoodId();
        String foodName=modelFood.getFoodName();
        String foodDesc=modelFood.getFoodDesc();
        String dishStyle=modelFood.getDishStyle();
        String foodQuantity=modelFood.getFoodQuantity();
        String foodIcon=modelFood.getFoodIcon();
        String originalPrice=modelFood.getOriginalPrice();
        String timing=modelFood.getTiming();
        String timestamp=modelFood.getTimestamp();
        String userID=modelFood.getUserID();

        //set data
        holder.titleTv.setText(foodName);
        holder.orignalPriceTv.setText(originalPrice);
        holder.timingFoodTv.setText(timing);
        holder.foodStyleTvs.setText(dishStyle);

        holder.addToCartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add food to cart

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show food details
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodsList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter=new FilterFoodUser(this,filterList);
        }
        return filter;
    }

    class HolderFoodUser extends RecyclerView.ViewHolder{
        //ui views
        private TextView titleTv,foodStyleTvs,orignalPriceTv,timingFoodTv,
                addToCartTv;
        public HolderFoodUser(@NonNull View itemView) {
            super(itemView);

            titleTv=itemView.findViewById(R.id.titleTv);
            foodStyleTvs=itemView.findViewById(R.id.foodStyleTvs);
            orignalPriceTv=itemView.findViewById(R.id.orignalPriceTv);
            timingFoodTv=itemView.findViewById(R.id.timingFoodTv);
            addToCartTv=itemView.findViewById(R.id.addToCartTv);

        }
    }
}
