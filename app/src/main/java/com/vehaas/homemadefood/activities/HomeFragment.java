package com.vehaas.homemadefood.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.vehaas.homemadefood.R;
import com.vehaas.homemadefood.adapter.AdapterKitchen;
import com.vehaas.homemadefood.adapter.AdapterOrderUser;
import com.vehaas.homemadefood.model.ModelKitchen;
import com.vehaas.homemadefood.model.ModelOrderUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

public class HomeFragment extends Fragment {
    TextView hello_name1;
    TextView tabKitchenUser,tabOrderUser,hello_name;
    private RelativeLayout kitchenRl,orderRl;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private RecyclerView kitchenRv,ordersRv;

    private ArrayList<ModelKitchen> kitchensList;
    private AdapterKitchen adapterKitchen;

    private ArrayList<ModelOrderUser> ordersList;
    private AdapterOrderUser adapterOrderUser;
    String userID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fStore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();

        userID=fAuth.getCurrentUser().getUid();
        View v=inflater.inflate(R.layout.fragment_home,container,false);



        hello_name1=v.findViewById(R.id.hello_name);
        tabKitchenUser=v.findViewById(R.id.tabKitchenUser);
        tabOrderUser=v.findViewById(R.id.tabOrderUser);
        kitchenRl=v.findViewById(R.id.kitchenRl);
        orderRl=v.findViewById(R.id.orderRl);
        kitchenRv=v.findViewById(R.id.kitchenRv);
        ordersRv=v.findViewById(R.id.ordersRv);
        hello_name=v.findViewById(R.id.hello_name);

        loadKitchen();
        loadOrders();

        //at start show kitchen
        showKitchenUI();
        tabKitchenUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show kitchen
                showKitchenUI();
            }
        });
        tabOrderUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show orders
                showOrderUI();
            }
        });

        return v;
    }



    private void loadOrders() {
        //init order list
        ordersList=new ArrayList<>();

        //get Orders
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("kitchen");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    String uid=""+ds.getRef().getKey();

                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("kitchen").child(uid).child("orders");//this was orders
                    ref.orderByChild("orderBy").equalTo(fAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        for (DataSnapshot ds:snapshot.getChildren()){
                                            ModelOrderUser modelOrderUser=ds.getValue(ModelOrderUser.class);

                                            //add to list
                                            ordersList.add(modelOrderUser);
                                        }
                                        //setup adapter
                                        adapterOrderUser=new AdapterOrderUser(getActivity(),ordersList);
                                        //set to recyclerview
                                        ordersRv.setAdapter(adapterOrderUser);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadKitchen() {
        //init list
        kitchensList=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("kitchen");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear list before adding
                kitchensList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelKitchen modelKitchen=ds.getValue(ModelKitchen.class);
                    kitchensList.add(modelKitchen);
                }
                adapterKitchen=new AdapterKitchen(getContext(),kitchensList);
                //set adapter to recyclerview
                kitchenRv.setAdapter(adapterKitchen);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    private void loadMyInfo() {
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("kitchen");
//        reference.orderByChild("uid").equalTo(fAuth.getUid())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot ds: snapshot.getChildren()){
//                            //get user data
//                            String name=""+ds.child("kitchen_name").getValue();
//                            String phone=""+ds.child("kitchen_phone").getValue();
//                            String address=""+ds.child("kitchen_address").getValue();
//                            //set user data
//                            //name
//                            
//                            //phone
//                            //address
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//    }


    private void showKitchenUI() {
        //show kitchen ui,hide order ui
        kitchenRl.setVisibility(getView().VISIBLE);
        orderRl.setVisibility(getView().GONE);
        tabKitchenUser.setTextColor(getResources().getColor(R.color.black));
        tabKitchenUser.setBackgroundResource(R.drawable.shape_rect01);
        tabOrderUser.setTextColor(getResources().getColor(R.color.white));
        tabOrderUser.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
    private void showOrderUI() {
        //show kitchen ui,hide order ui
        kitchenRl.setVisibility(getView().GONE);
        orderRl.setVisibility(getView().VISIBLE);
        tabKitchenUser.setTextColor(getResources().getColor(R.color.white));
        tabKitchenUser.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        tabOrderUser.setTextColor(getResources().getColor(R.color.black));
        tabOrderUser.setBackgroundResource(R.drawable.shape_rect01);
    }


}
