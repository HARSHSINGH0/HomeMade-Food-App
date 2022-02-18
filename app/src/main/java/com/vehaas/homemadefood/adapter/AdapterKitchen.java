package com.vehaas.homemadefood.adapter;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vehaas.homemadefood.R;

public class AdapterKitchen {
    //view holder
    class HolderKitchen extends RecyclerView.ViewHolder{
        //ui views of row_kitchen.xml
        private ImageView kitchenIv,kitchenNameTv,kitchenNameTv;
        public HolderKitchen(@NonNull View itemView) {
            super(itemView);
            //init uid views
            kitchenIv=itemView.findViewById(R.id.kitchenIv);
            kitchenNameTv=itemView.findViewById(R.id.kitchenNameTv);

        }
    }
}
