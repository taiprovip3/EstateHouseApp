package com.example.estatehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProfieScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageReference;
    private FirebaseFirestore db;
    private CollectionReference userReference;

    private ImageView avatarView, uploadView;
    private TextView fullNameView, roleView, btnUpdateProfile;
    private EditText edEmail, edFirstName, edLastName, edPhoneNumber, edPassword;
    private Spinner spinnerLocation;
    private Button btnBack, btnYourCart;
    private ArrayAdapter<CharSequence> adapterLocation;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profie_screen);

        anhXa();

        adapterLocation = ArrayAdapter.createFromResource(this, R.array.locations, android.R.layout.simple_spinner_item);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerLocation.setAdapter(adapterLocation);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfieScreen.this, HomepageScreen.class);
                startActivity(intent);
            }
        });
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
        btnYourCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfieScreen.this, CartScreen.class);
                startActivity(intent);
            }
        });
        uploadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfieScreen.this, UploadScreen.class);
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
                                                .into(avatarView);
                                        }
                                    });
                                    fullNameView.setText(user.getFirstName() + " " + user.getLastName());
                                    roleView.setText(user.getRole());
                                    edEmail.setText(user.getEmail());
                                    edFirstName.setText(user.getFirstName());
                                    edLastName.setText(user.getLastName());
                                    spinnerLocation.setSelection(adapterLocation.getPosition(user.getLocation()));
                                    edPhoneNumber.setText(user.getPhonenumber());
                                    edPassword.setText(user.getPassword());
                                }
                            } else
                                Toast.makeText(ProfieScreen.this, "GET DOCUMENT FAILED", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void updateProfile() {
        user.setFirstName(edFirstName.getText().toString());
        user.setLastName(edLastName.getText().toString());
        user.setLocation(spinnerLocation.getSelectedItem().toString());
        user.setPhonenumber(edPhoneNumber.getText().toString());
        user.setPassword(edPassword.getText().toString());
        userReference.document(user.getDocumentId()).set(user);
        Toast.makeText(this, "Update profile success", Toast.LENGTH_LONG).show();
        finish();
        startActivity(getIntent());
    }

    private boolean isLogged() {
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
            return false;
        return true;
    }

    private void anhXa() {
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://estatehouse-4ee84.appspot.com");
        db = FirebaseFirestore.getInstance();
        userReference = db.collection("users");

        user = new User();
        avatarView = findViewById(R.id.pr_imageAvatar);
        uploadView = findViewById(R.id.pr_uploadImage);
        fullNameView = findViewById(R.id.pr_txtFullName);
        roleView = findViewById(R.id.pr_txtRole);
        edEmail = findViewById(R.id.pr_edEmail);
        edFirstName = findViewById(R.id.pr_edFirstName);
        edLastName = findViewById(R.id.pr_edLastName);
        edPhoneNumber = findViewById(R.id.pr_edPhoneNumber);
        edPassword = findViewById(R.id.pr_edPassword);
        spinnerLocation = findViewById(R.id.pr_spinnerLocation);
        btnBack = findViewById(R.id.pr_btnBack);
        btnUpdateProfile = findViewById(R.id.pr_updateProfile);
        btnYourCart = findViewById(R.id.pr_btnYourCart);
    }
}