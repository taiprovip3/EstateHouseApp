package com.example.estatehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estatehouse.dao.CartDao;
import com.example.estatehouse.dao.HouseDao;
import com.example.estatehouse.entity.House;
import com.example.estatehouse.entity.HouseCart;
import com.example.estatehouse.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import vn.thanguit.toastperfect.ToastPerfect;

public class DetailScreen extends AppCompatActivity {

    private ImageView imageHouseView, btnBack;
    private TextView priceHouseView, addressHouseView, bedroomNumberView, bathroomNumberView, livingareView, sellerView, descriptionView, balanceView, typeView;
    private Button btnBuy, btnAddToCart;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageReference;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private CollectionReference cartReference, userReference, houseReference;
    private String imageHouse, addressHouse, documentIdHouse, descriptionHouse, sellerHouse, typeHouse;
    private double priceHouse;
    private int bedroomHouse, bathroomHouse, livingareaHouse;
    private User user;
    private AlertDialog dialogBuy;
    private AlertDialog.Builder builderBuy;
    private CartDao cartDao;
    private HouseDao houseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        cartReference = db.collection("carts");
        userReference = db.collection("users");
        houseReference = db.collection("houses");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://estatehouse-4ee84.appspot.com");
        imageHouseView = findViewById(R.id.dt_imageView);
        btnBuy = findViewById(R.id.dt_btnBuy);
        btnAddToCart = findViewById(R.id.dt_addToCart);
        priceHouseView = findViewById(R.id.dt_priceView);
        addressHouseView = findViewById(R.id.dt_addressView);
        bedroomNumberView = findViewById(R.id.dt_bedNumber);
        bathroomNumberView = findViewById(R.id.dt_bathRooms);
        livingareView = findViewById(R.id.dt_livingareView);
        sellerView = findViewById(R.id.dt_sellerView);
        descriptionView = findViewById(R.id.dt_descriptionView);
        typeView = findViewById(R.id.dt_typeView);
        btnBack = findViewById(R.id.dt_btnBack);
        balanceView = findViewById(R.id.dt_balanceView);
        cartDao = new CartDao(this);
        houseDao = new HouseDao(this);

        onClick();
        
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            imageHouse = bundle.getString("imageHouse", "image_57");
            priceHouse = bundle.getDouble("priceHouse", 0.0);
            addressHouse = bundle.getString("addressHouse", "No such address");
            bedroomHouse = bundle.getInt("bedroomHouse", 0);
            bathroomHouse = bundle.getInt("bathroomHouse", 0);
            livingareaHouse = bundle.getInt("livingareaHouse", 0);
            documentIdHouse = bundle.getString("documentIdHouse", "");
            descriptionHouse = bundle.getString("descriptionHouse", "No description");
            sellerHouse = bundle.getString("sellerHouse", "Incognito");
            typeHouse = bundle.getString("typeHouse", "BUY");

            imageReference = storageReference.child("images/" + imageHouse);
            imageReference.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get()
                                    .load(uri)
                                    .into(imageHouseView);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailScreen.this, "FAILED TO GET IMG", Toast.LENGTH_LONG).show();
                        }
                    });
            priceHouseView.setText("$"+priceHouse);
            addressHouseView.setText(addressHouse);
            bedroomNumberView.setText(""+bedroomHouse);
            bathroomNumberView.setText(""+bathroomHouse);
            livingareView.setText(""+livingareaHouse);
            descriptionView.setText(descriptionHouse);
            sellerView.setText(sellerHouse);
            typeView.setText(typeHouse);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            userReference.whereEqualTo("email", currentUser.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document: task.getResult()){
                                    user = document.toObject(User.class);
                                    balanceView.setText("$" + user.getBalance());
                                }
                            }
                        }
                    });
        } else{
            Intent intent = new Intent(DetailScreen.this, LoginScreen.class);
            startActivity(intent);
        }
    }

    private void onClick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailScreen.this, HomepageScreen.class);
                startActivity(intent);
            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HouseCart cart = new HouseCart();
                String documentId = UUID.randomUUID().toString();
                cart.setDocumentId(documentId);
                cart.setEmail(currentUser.getEmail());
                cart.setCost(priceHouse);
                cart.setSeller(sellerHouse);
                cart.setBedrooms(bedroomHouse);
                cart.setBathrooms(bathroomHouse);
                cart.setLivingarea(livingareaHouse);
                cart.setImage(imageHouse);
                cartReference.document(documentId).set(cart);
                ToastPerfect.makeText(DetailScreen.this, ToastPerfect.SUCCESS, "Added success", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
//                cartDao.addToCart(cart);
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builderBuy = new AlertDialog.Builder(DetailScreen.this);
                builderBuy.setTitle("Confirmation buy/rent a house");
                builderBuy.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builderBuy.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(user.getBalance() < priceHouse){
                            ToastPerfect.makeText(DetailScreen.this, ToastPerfect.ERROR, "You don't have enough $ to purchase", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_LONG).show();
                        } else{
                            double moneyLeft = user.getBalance() - priceHouse;
                            user.setBalance(moneyLeft);
                            balanceView.setText("$"+moneyLeft);
                            userReference.document(user.getDocumentId())
                                    .update("balance", moneyLeft);
                            userReference.whereEqualTo("seller", sellerHouse)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isSuccessful()){
                                                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                                                    User sellerUser = documentSnapshot.toObject(User.class);
                                                                    userReference.document(sellerUser.getDocumentId())
                                                                            .update("balance", sellerUser.getBalance() + priceHouse);
                                                                }
                                                            }
                                                        }
                                                    });
                            houseReference.document(documentIdHouse)
                                    .delete();
//                            houseDao.deleteHouse(documentIdHouse);
                            imageReference
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("images storage", "success to delete image "+imageHouse);
                                        }
                                    });
                            ToastPerfect.makeText(DetailScreen.this, ToastPerfect.SUCCESS, "You purchased success", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_LONG).show();
                            Intent intent = new Intent(DetailScreen.this, HomepageScreen.class);
                            startActivity(intent);
                        }
                    }
                });
                dialogBuy = builderBuy.create();
                dialogBuy.show();
            }
        });
    }
}