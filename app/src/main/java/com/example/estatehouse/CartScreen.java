package com.example.estatehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.estatehouse.adapter.CartAdapter;
import com.example.estatehouse.entity.HouseCart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartScreen extends AppCompatActivity {

    List<HouseCart> houseCarts;
    ListView listView;
    CartAdapter cartAdapter;
    FirebaseFirestore db;
    CollectionReference cartReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_screen);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        cartReference = db.collection("carts");
        houseCarts = new ArrayList<HouseCart>();
        listView = (ListView) findViewById(R.id.cart_listView);
        cartReference
                .whereEqualTo("email", currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                HouseCart cart = documentSnapshot.toObject(HouseCart.class);
                                houseCarts.add(cart);
                            }
                            cartAdapter = new CartAdapter(houseCarts, CartScreen.this);
                            listView.setAdapter(cartAdapter);
                        }else
                            Log.w("ERROR-GET-FIRESTORE", "Error getting documents.", task.getException());
                    }
                });
    }
}