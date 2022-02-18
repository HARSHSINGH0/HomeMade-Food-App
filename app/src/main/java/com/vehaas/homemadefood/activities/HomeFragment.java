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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.vehaas.homemadefood.R;

import java.util.concurrent.Executor;

public class HomeFragment extends Fragment {
    TextView hello_name1;
    TextView tabKitchenUser,tabOrderUser;
    private RelativeLayout kitchenRl,orderRl;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
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
