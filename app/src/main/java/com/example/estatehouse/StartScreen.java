package com.example.estatehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.estatehouse.dao.CartDao;
import com.example.estatehouse.dao.HouseDao;
import com.example.estatehouse.dao.UserDao;
import com.example.estatehouse.entity.House;
import com.example.estatehouse.entity.HouseCart;
import com.example.estatehouse.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class StartScreen extends AppCompatActivity {

    private Button btnStart;
    private FirebaseFirestore db;
    private CartDao cartDao;
    private HouseDao houseDao;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        btnStart = findViewById(R.id.start_getStart);
        db = FirebaseFirestore.getInstance();
        cartDao = new CartDao(this);
        houseDao = new HouseDao(this);
        userDao = new UserDao(this);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //User pull
                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        User user = documentSnapshot.toObject(User.class);
                                        String userId = user.getDocumentId();
                                        if(userDao.isUserExistedById(userId)){
                                            //Gọi hàm update
                                            userDao.updateUser(user, userId);
                                        } else{
                                            //Gọi hàm insert
                                            userDao.addUser(user);
                                        }
                                    }
                                }
                            }
                        });
                db.collection("houses")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        House house = documentSnapshot.toObject(House.class);
                                        String houseId = house.getDocumentId();
                                        if(houseDao.isHouseExistedById(houseId)){
                                            houseDao.updateHouse(house, houseId);
                                        } else{
                                            houseDao.registerHouse(house);
                                        }
                                    }
                                }
                            }
                        });
                db.collection("carts")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        HouseCart houseCart = documentSnapshot.toObject(HouseCart.class);
                                        String cartId = houseCart.getDocumentId();
                                        if(cartDao.isCartExistedById(cartId)){
                                            cartDao.updateCart(houseCart, cartId);
                                        } else{
                                            cartDao.addToCart(houseCart);
                                        }
                                    }
                                }
                            }
                        });
                Intent intent = new Intent(StartScreen.this, HomepageScreen.class);
                startActivity(intent);
            }
        });
    }
}