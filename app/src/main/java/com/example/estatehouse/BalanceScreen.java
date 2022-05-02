package com.example.estatehouse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.estatehouse.dao.UserDao;
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

public class BalanceScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageReference;
    private FirebaseFirestore db;
    private CollectionReference userReference;

    private ImageView imgAvatar;
    private TextView txtName;
    private TextView txtRole;
    private TextView txtBalance;
    private TextView btnBack;
    private Button btnSetting, btnCart;

    private User user;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_screen);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://estatehouse-4ee84.appspot.com");
        db = FirebaseFirestore.getInstance();
        userReference = db.collection("users");

        user = new User();
        userDao=new UserDao(this);

        imgAvatar = findViewById(R.id.imgAvatar);
        txtName = findViewById(R.id.txtUserName);
        txtRole = findViewById(R.id.txtRole);
        txtBalance=findViewById(R.id.txtBalance);
        btnSetting=findViewById(R.id.bl_goSetting);
        btnCart = findViewById(R.id.bl_goCart);
        btnBack = findViewById(R.id.bl_btnBack);

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BalanceScreen.this, ProfieScreen.class);
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
                Intent intent=new Intent(BalanceScreen.this, ProfieScreen.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLogged()) {
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
                                    userDao.addUser(user);
                                }
                            } else
                                Toast.makeText(BalanceScreen.this, "GET DOCUMENT FAILED", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private boolean isLogged() {
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
            return false;
        return true;
    }
}