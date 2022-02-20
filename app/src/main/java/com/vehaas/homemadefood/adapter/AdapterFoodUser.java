package com.vehaas.homemadefood.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vehaas.homemadefood.FilterFoodUser;
import com.vehaas.homemadefood.R;
import com.vehaas.homemadefood.activities.KitchenDetailsActivity;
import com.vehaas.homemadefood.model.ModelFood;

import org.w3c.dom.Text;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

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
                showQuantityDialog(modelFood);

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show food details
            }
        });

    }

    private double cost,finalCost=0;
    private int quantity=0;
    private void showQuantityDialog(ModelFood modelFood) {
        //inflate layout for dialog
        View view=LayoutInflater.from(context).inflate(R.layout.dialog_quantity, null);
        //init layout views
        ImageView foodIv=view.findViewById(R.id.foodIv);
        TextView titleTv=view.findViewById(R.id.titleTv);
        TextView descriptionTv=view.findViewById(R.id.descriptionTv);
        TextView orignalPriceTv=view.findViewById(R.id.orignalPriceTv);
        TextView finalPriceTv=view.findViewById(R.id.finalPriceTv);
        ImageButton decrementBtn=view.findViewById(R.id.decrementBtn);
        TextView quantityTv=view.findViewById(R.id.quantityTv);
        ImageButton incrementBtn=view.findViewById(R.id.incrementBtn);
        Button continueBtn=view.findViewById(R.id.continueBtn);
        TextView timingTv=view.findViewById(R.id.timingTv);

        //get data from model
        String foodName=modelFood.getFoodName();
        String foodDesc=modelFood.getFoodDesc();
        String originalPrice=modelFood.getOriginalPrice();
        String timing=modelFood.getTiming();
        String foodId=modelFood.getFoodId();

        String price;
        price=modelFood.getOriginalPrice();
        cost=Double.parseDouble(price.replaceAll("₹",""));
        finalCost=Double.parseDouble(price.replaceAll("₹",""));
        quantity=1;
        //dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);
        //set data
        titleTv.setText(""+foodName);
        descriptionTv.setText(""+foodDesc);
        orignalPriceTv.setText("₹"+originalPrice);
        timingTv.setText(""+timing);
        finalPriceTv.setText(""+finalCost);

        AlertDialog dialog=builder.create();
        dialog.show();

        //increase quantity of food
        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCost=finalCost+cost;
                quantity++;
                finalPriceTv.setText("₹"+finalCost);
                quantityTv.setText(""+quantity);
            }
        });
        //decrease quantity of food
        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity>1){
                    finalCost=finalCost-cost;
                    quantity--;
                    finalPriceTv.setText("₹"+finalCost);
                    quantityTv.setText(""+quantity);
                }
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=titleTv.getText().toString().trim();
                String priceEach=orignalPriceTv.getText().toString().trim().replace("₹","");
                String price=finalPriceTv.getText().toString().trim().replace("₹","");
                String quantity=quantityTv.getText().toString().trim();

                //add to db(SQlite)
                addToCart(foodId,title,priceEach,price,quantity);
            }
        });
    }

    private int itemId=1;
    private void addToCart(String foodId, String title, String priceEach, String price, String quantity) {
        itemId++;
        EasyDB easyDB=EasyDB.init(context,"ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id",new String[]{"text","unique"}))
                .addColumn(new Column("Item_PID",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Name",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price_Each",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Quantity",new String[]{"text","not null"}))
                .doneTableColumn();
        Boolean b=easyDB.addData("Item_Id",itemId)
                .addData("Item_Id",foodId)
                .addData("Item_Name",title)
                .addData("Item_Price_Each",priceEach)
                .addData("Item_Price",price)
                .addData("Item_Quantity",quantity)
                .doneDataAdding();
        Toast.makeText(context,"Added to cart.....",Toast.LENGTH_SHORT).show();
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
