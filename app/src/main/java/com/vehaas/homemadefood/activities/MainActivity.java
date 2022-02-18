package com.vehaas.homemadefood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.vehaas.homemadefood.R;

public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav=findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navlister);


        //setting Home fragment as a main fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new HomeFragment()).commit();

    }
    //Listener Nav bar
    private BottomNavigationView.OnNavigationItemSelectedListener navlister=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;
            switch (item.getItemId()){
                case R.id.nav_home:
//                    showUserName();
                    selectedFragment=new HomeFragment();
                    break;
                case R.id.nav_account:
                    selectedFragment=new AccountFragment();
                    break;
                case R.id.nav_cart:
                    selectedFragment =new CartFragment();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
            //Begin Transaction
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,selectedFragment).commit();
            return true;
        };
    };
    private Fragment showUserName(){
        Bundle bundle = new Bundle();
//        TextView hello_name=findViewById(R.id.hello_name);
        Intent intent;
        Fragment selectedFragment=null;
        intent = getIntent();
        String user_name= intent.getStringExtra("name").toString();
        bundle.putString("user_name",user_name);
        selectedFragment=new HomeFragment();
        selectedFragment.setArguments(bundle);
        return selectedFragment;
//        hello_name.setText(user_name);
    }

    public void logout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FirebaseAuth.getInstance().signOut();//logging out the user
                startActivity(new Intent(getApplicationContext(),LoginFragment.class));
                finish();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
    public void login_txt(View view) {
        startActivity(new Intent(getApplicationContext(),LoginFragment.class));

    }




    //this is part where when user clicks on switch to homekitchen then this function runs
    public void switchToKitchen(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), HomeMakerKitchen.class));
                dialog.dismiss();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}