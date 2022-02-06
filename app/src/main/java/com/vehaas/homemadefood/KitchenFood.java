package com.vehaas.homemadefood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class KitchenFood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_food);

    }

    public void create_Kitchen_food_button(View view) {
        startActivity(new Intent(getApplicationContext(),KitchenOrders.class));
    }
    public void check_order_button(View view) {
        startActivity(new Intent(getApplicationContext(),Kitchen_order_requests.class));
    }
}