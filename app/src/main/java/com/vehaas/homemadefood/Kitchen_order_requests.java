package com.vehaas.homemadefood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Kitchen_order_requests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_order_requests);
    }

    public void gotohome_button(View view) {
        startActivity(new Intent(getApplicationContext(),KitchenFood.class));
    }
}