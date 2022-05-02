package com.example.estatehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estatehouse.adapter.CartAdapter;
import com.example.estatehouse.entity.HouseCart;
import com.example.estatehouse.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import vn.thanguit.toastperfect.ToastPerfect;

public class CartScreen extends AppCompatActivity {

    List<HouseCart> houseCarts;
    ListView listView;
    CartAdapter cartAdapter;
    FirebaseFirestore db;
    CollectionReference cartReference, userReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference imageReference;
    ImageView avatarView, btnBack;
    TextView fullNameView, roleView;
    Button btnSetting, btnBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_screen);

        avatarView = findViewById(R.id.cart_avatarView);
        fullNameView = findViewById(R.id.cart_fullNameView);
        roleView = findViewById(R.id.cart_roleView);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://estatehouse-4ee84.appspot.com");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userReference = db.collection("users");
        cartReference = db.collection("carts");
        houseCarts = new ArrayList<HouseCart>();
        listView = (ListView) findViewById(R.id.cart_listView);
        btnBack = findViewById(R.id.cart_btnBack);
        btnSetting = findViewById(R.id.cart_btnSetting);
        btnBalance = findViewById(R.id.cart_btnBalance);

        setOnClick();

        cartReference
                .whereEqualTo("email", currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                HouseCart cart = documentSnapshot.toObject(HouseCart.class);
                                houseCarts.add(cart);
                            }
                            cartAdapter = new CartAdapter(houseCarts, CartScreen.this);
                            listView.setAdapter(cartAdapter);
                        } else
                            Log.w("ERROR-GET-FIRESTORE", "Error getting documents.", task.getException());
                    }
                });
    }

    private void setOnClick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartScreen.this, ProfieScreen.class);
                startActivity(intent);
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartScreen.this, ProfieScreen.class);
                startActivity(intent);
            }
        });
        btnBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastPerfect.makeText(CartScreen.this, ToastPerfect.INFORMATION, "Change to balance screen!", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        String email = currentUser.getEmail();
        userReference.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = new User();
                                user = document.toObject(User.class);
                                user.setDocumentId(document.getId());
                                imageReference = storageReference.child("images/"+user.getAvatar());
                                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.get()
                                                .load(uri)
                                                .into(avatarView);
                                    }
                                });
                                fullNameView.setText(user.getFirstName() + " " + user.getLastName());
                                roleView.setText(user.getRole());
                            }
                        }
                    }
                });
    }
}