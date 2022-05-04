package com.example.estatehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.estatehouse.adapter.CartAdapter;
import com.example.estatehouse.dao.CartDao;
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

    private List<HouseCart> houseCarts;
    private ListView listView;
    private CartAdapter cartAdapter;
    private FirebaseFirestore db;
    private CollectionReference cartReference, userReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageReference;
    private ImageView avatarView, btnBack;
    private TextView fullNameView, roleView, balanceView;
    private Button btnSetting, btnBalance;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_screen);
       anhXa();
        onClick();

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
                            ToastPerfect.makeText(CartScreen.this, ToastPerfect.ERROR, "ERROR, "+task.getException(), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                    }
                });
    }

    private void anhXa() {
        user = new User();
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
        balanceView = findViewById(R.id.cart_balanceView);
    }

    private void onClick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartScreen.this, ProfileScreen.class);
                startActivity(intent);
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartScreen.this, ProfileScreen.class);
                startActivity(intent);
            }
        });
        btnBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartScreen.this, BalanceScreen.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        userReference.whereEqualTo("email", currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user = document.toObject(User.class);
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
                                balanceView.setText("$"+user.getBalance());
                            }
                        }
                    }
                });
    }
}