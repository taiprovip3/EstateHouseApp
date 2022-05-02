package com.example.estatehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estatehouse.entity.House;
import com.example.estatehouse.entity.HouseCart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DetailScreen extends AppCompatActivity {

    private ImageView imageHouseView;
    private TextView priceHouseView, addressHouseView, bedroomNumberView, bathroomNumberView, livingareView, sellerView, descriptionView;
    private Button btnBuy, btnAddToCart;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageReference;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private CollectionReference cartReference;
    String imageHouse, addressHouse, documentIdHouse, descriptionHouse, sellerHouse;
    double priceHouse;
    int bedroomHouse, bathroomHouse, livingareaHouse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        anhXa();
        
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
                            Toast.makeText(DetailScreen.this, "FAILED TO GET IMAGE", Toast.LENGTH_LONG).show();
                        }
                    });
            priceHouseView.setText(""+priceHouse);
            addressHouseView.setText(addressHouse);
            bedroomNumberView.setText(""+bedroomHouse);
            bathroomNumberView.setText(""+bathroomHouse);
            livingareView.setText(""+livingareaHouse);
            descriptionView.setText(descriptionHouse);
            sellerView.setText(sellerHouse);
        }
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String documentId = UUID.randomUUID().toString();
                HouseCart cart = new HouseCart();
                cart.setDocumentId(documentId);
                cart.setEmail(currentUser.getEmail());
                cart.setCost(priceHouse);
                cart.setSeller(sellerHouse);
                cart.setBedrooms(bedroomHouse);
                cart.setBathrooms(bathroomHouse);
                cart.setLivingarea(livingareaHouse);
                cart.setImage(imageHouse);
                cartReference.document(documentId).set(cart);
                Toast.makeText(DetailScreen.this, "Added to your cart success!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void anhXa() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        cartReference = db.collection("carts");
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
    }
}