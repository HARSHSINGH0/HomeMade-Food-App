package com.vehaas.homemadefood;

import android.widget.Filter;

import com.vehaas.homemadefood.adapter.AdapterFoodSeller;
import com.vehaas.homemadefood.adapter.AdapterOrderKitchen;
import com.vehaas.homemadefood.model.ModelFood;
import com.vehaas.homemadefood.model.ModelOrderKitchen;
import com.vehaas.homemadefood.model.ModelOrderUser;

import java.util.ArrayList;

public class FilterOrderKitchen extends Filter {
    private AdapterOrderKitchen adapter;
    private ArrayList<ModelOrderKitchen> filterList;



    public FilterOrderKitchen(AdapterOrderKitchen adapter, ArrayList<ModelOrderKitchen> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //validate data for search query
        if(constraint != null && constraint.length()>0){
            //search filed not empty, searching something,perform search
            //change to upper case,to make case insensitive
            constraint=constraint.toString().toUpperCase();
            //store our filtered list
            ArrayList<ModelOrderKitchen> filterModels=new ArrayList<>();
            for (int i=0;i<filterList.size();i++){
                //check,search by title and dish style
                if (filterList.get(i).getOrderStatus().toUpperCase().contains(constraint)){
                    //add filtered data to list
                    filterModels.add(filterList.get(i));
                }
            }
            results.count=filterModels.size();
            results.values=filterModels;
        }
        else {

            //search filed empty,not searching , return orignal/all/complete list
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.orderKitchenArrayList= (ArrayList<ModelOrderKitchen>) results.values;
        //refresh adapter
        adapter.notifyDataSetChanged();
    }
}
