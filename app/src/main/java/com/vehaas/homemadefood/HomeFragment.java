package com.vehaas.homemadefood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import java.util.concurrent.Executor;

public class HomeFragment extends Fragment {
    TextView hello_name1;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    String userID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fStore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();

        userID=fAuth.getCurrentUser().getUid();
        Log.d("TAG", "userid: "+userID+"fstore:"+fStore+"fauth:"+fAuth);
        View v=inflater.inflate(R.layout.fragment_home,container,false);
        hello_name1=v.findViewById(R.id.hello_name);
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner_timing);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.food_timing_spinner, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        return v;
    }

}
