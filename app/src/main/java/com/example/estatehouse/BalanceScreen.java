package com.example.estatehouse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import vn.thanguit.toastperfect.ToastPerfect;

public class BalanceScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageReference;
    private FirebaseFirestore db;
    private CollectionReference userReference;
    private ImageView imgAvatar;
    private TextView txtName, txtRole, txtBalance, btnBack;
    private Button btnSetting, btnCart;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_screen);

        anhXa();
        onClick();
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
                                user = document.toObject(User.class);
                                user.setDocumentId(document.getId());
                                imageReference = storageReference.child("images/"+user.getAvatar());
                                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.get()
                                                .load(uri)
                                                .into(imgAvatar);
                                    }
                                });
                                txtName.setText(user.getFirstName() + " " + user.getLastName());
                                txtRole.setText(user.getRole());
                                txtBalance.setText("$" + String.valueOf(user.getBalance()));
                            }
                        } else
                            ToastPerfect.makeText(BalanceScreen.this, ToastPerfect.ERROR, "ERROR, get document failed", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                    }
                });
    }

    private void onClick() {
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BalanceScreen.this, ProfileScreen.class);
                startActivity(intent);
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BalanceScreen.this, CartScreen.class);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BalanceScreen.this, ProfileScreen.class);
                startActivity(intent);
            }
        });
    }

    private void anhXa() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://estatehouse-4ee84.appspot.com");
        db = FirebaseFirestore.getInstance();
        userReference = db.collection("users");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        user = new User();
        imgAvatar = findViewById(R.id.imgAvatar);
        txtName = findViewById(R.id.txtUserName);
        txtRole = findViewById(R.id.txtRole);
        txtBalance=findViewById(R.id.txtBalance);
        btnSetting=findViewById(R.id.bl_goSetting);
        btnCart = findViewById(R.id.bl_goCart);
        btnBack = findViewById(R.id.bl_btnBack);
    }
}