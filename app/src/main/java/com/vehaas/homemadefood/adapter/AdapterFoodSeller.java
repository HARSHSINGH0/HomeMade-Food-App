package com.vehaas.homemadefood.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.vehaas.homemadefood.FilterFood;
import com.vehaas.homemadefood.R;
import com.vehaas.homemadefood.activities.EditFoodActivity;
import com.vehaas.homemadefood.activities.KitchenFood;
import com.vehaas.homemadefood.model.ModelFood;

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
                detailsBottomSheet(modelFood);//here modelProduct contains details of clicked photo

            }
        });

    }

    private void detailsBottomSheet(ModelFood modelFood) {
        //bottom sheet
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(context);
        //inflate view for bottomsheet
        View view=LayoutInflater.from(context).inflate(R.layout.bs_food_details_seller,null);
        //set view to bottomsheet
        bottomSheetDialog.setContentView(view);


        //init views of bottomsheet

        ImageButton editButtonIb=view.findViewById(R.id.editButtonIb);
        ImageButton deleteButtonTb=view.findViewById(R.id.deleteButtonTb);
        ImageView imageFoodIm=view.findViewById(R.id.imageFoodIm);
        TextView foodNameTv=view.findViewById(R.id.foodNameTv);
        TextView foodDesc=view.findViewById(R.id.foodDesc);
        TextView timingFoodTv=view.findViewById(R.id.timingFoodTv);
        TextView stockRemainingTv=view.findViewById(R.id.stockRemainingTv);
        TextView priceTv=view.findViewById(R.id.priceTv);

        //get data
        String id=modelFood.getFoodId();
        String uid=modelFood.getUserID();
        String desc=modelFood.getFoodDesc();
        String foodDescription=modelFood.getFoodDesc();
        String timing=modelFood.getTiming();
        String icon=modelFood.getFoodIcon();
        String quantity=modelFood.getFoodQuantity();
        String title=modelFood.getFoodName();
        String timestamp=modelFood.getTimestamp();
        String orignalPrice=modelFood.getOriginalPrice();

        //set data
        foodNameTv.setText(title);
        foodDesc.setText(desc);
        foodDesc.setText(desc);
        timingFoodTv.setText(timing);
        stockRemainingTv.setText(quantity);
        priceTv.setText(orignalPrice);
        try{
            Picasso.with(context.getApplicationContext()).load(icon).placeholder(R.drawable.ic_baseline_fastfood_24).into(imageFoodIm);
        }catch (Exception e){
            imageFoodIm.setImageResource(R.drawable.ic_baseline_fastfood_24);
        }
        //show dialog
        bottomSheetDialog.show();
        //edit button click
        editButtonIb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //open edit product acitvity,pass id of food
                bottomSheetDialog.dismiss();
                Log.d("TAG", "onClick: id: "+id);
                Intent intent=new Intent(context, EditFoodActivity.class);
                intent.putExtra("foodId",id);
                context.startActivity(intent);
            }
        });
        //delete button click
        deleteButtonTb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //show delete confirm dialog
//                AlertDialog.Builder builder=new AlertDialog.Builder(context);
//
//                builder.setTitle("Delete")
//                        .setMessage("Are you sure you want to delete food "+title+" ?")
//                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Log.d("TAG", ":before food deleted");
//                                //delete
//                                deletefood(id);//this id is food id
//
//                            }
//                        })
//                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //cancel,dismiss dialog
//                    }
//                });
                deletefood(id);
                bottomSheetDialog.dismiss();
                Intent intent=new Intent(context, KitchenFood.class);
                context.startActivity(intent);
            }
        });
        //back button click

    }

    private void deletefood(String id) {
        //delete products using its id
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("kitchen");

        reference.child(firebaseAuth.getUid()).child("foods").child(""+id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //product deleted
                        Log.d("TAG", ": food deleted");
                        Toast.makeText(context,"Food deleted....",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failed deleting product
                Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_SHORT).show();
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
