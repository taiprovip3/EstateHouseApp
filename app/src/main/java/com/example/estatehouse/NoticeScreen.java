package com.example.estatehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.estatehouse.adapter.HomepageAdapter;
import com.example.estatehouse.adapter.NoticAdapter;
import com.example.estatehouse.entity.House;
import com.example.estatehouse.entity.Notic;
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

import vn.thanguit.toastperfect.ToastPerfect;

public class NoticeScreen extends AppCompatActivity {

    FirebaseFirestore db;
    CollectionReference noticsRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ImageView btnGoHome;
    List<Notic> notics;
    NoticAdapter noticAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_screen);

        anhXa();
        onClick();

        notics = new ArrayList<Notic>();
        String email = currentUser.getEmail();
        if(email == null)
            email = "taito1doraemon@gmail.com";
        noticsRef.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Notic notic = documentSnapshot.toObject(Notic.class);
                                notics.add(notic);
                                Log.w("NOTIC", ""+notic);
                            }
                            noticAdapter = new NoticAdapter(notics, NoticeScreen.this);
                            listView.setAdapter(noticAdapter);
                        } else{
                            ToastPerfect.makeText(NoticeScreen.this, ToastPerfect.ERROR, "Could't load data, please check your network connection." + task.getException(), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onClick() {
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeScreen.this, HomepageScreen.class);
                startActivity(intent);
            }
        });
    }

    private void anhXa() {
        btnGoHome = findViewById(R.id.notic_goHome);
        db = FirebaseFirestore.getInstance();
        noticsRef = db.collection("notics");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        listView = findViewById(R.id.noti_listView);
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(NoticeScreen.this, LoginScreen.class);
            startActivity(intent);
        }
    }
}