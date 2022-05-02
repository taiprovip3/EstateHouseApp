package com.example.estatehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estatehouse.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import vn.thanguit.toastperfect.ToastPerfect;

public class RegisterScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnRegister;
    private EditText txtEmail, txtPassword, txtRePassword;
    private TextView txtLoginHere;
    private String email;
    private String password;
    private String rePassword;
    private FirebaseFirestore db;
    CollectionReference userReference;
    String documentId = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        anhXa();
        onClick();
    }

    private void onClick() {
        txtLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=txtEmail.getText().toString();
                password=txtPassword.getText().toString();
                rePassword=txtRePassword.getText().toString();
                if(email.equals("")){
                    ToastPerfect.makeText(RegisterScreen.this, ToastPerfect.ERROR, "Please enter username", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                    return;
                }
                if(password.equals("")){
                    ToastPerfect.makeText(RegisterScreen.this, ToastPerfect.ERROR, "Please enter password", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                    return;
                }
                if(rePassword.equals("")){
                    ToastPerfect.makeText(RegisterScreen.this, ToastPerfect.ERROR, "Please enter re-enter password", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                    return;
                }
                if(password.equals(rePassword)){
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@Nonnull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> userObject = new HashMap<>();
                                        userObject.put("email", email);
                                        userObject.put("password", password);
                                        userObject.put("first_name", "Un");
                                        userObject.put("last_name", "know");
                                        userObject.put("role", "member");
                                        userObject.put("location", "US");
                                        userObject.put("phone_number", "");
                                        userObject.put("balance", 0.0);
                                        userObject.put("avatar", "image_6.png");
                                        userObject.put("documentId", documentId);
                                        userReference
                                                .document(documentId)
                                                .set(userObject);
                                        ToastPerfect.makeText(RegisterScreen.this, ToastPerfect.SUCCESS, "Register success", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                                        emptyField();
                                    } else {
                                        ToastPerfect.makeText(RegisterScreen.this, ToastPerfect.ERROR, "Register failed", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else ToastPerfect.makeText(RegisterScreen.this, ToastPerfect.ERROR, "Password and Re-enter password are not the same", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
            }
        });
    }

    private void anhXa() {
        db = FirebaseFirestore.getInstance();
        userReference = db.collection("users");
        txtEmail=(EditText) findViewById(R.id.txtEmail);
        txtPassword=(EditText) findViewById(R.id.txtPassword);
        txtRePassword=(EditText) findViewById(R.id.txtRePassword);
        txtLoginHere=(TextView)findViewById(R.id.hp_txtViewLogin);
        btnRegister=(Button)findViewById(R.id.btnRegister);
    }

    private void emptyField() {
        txtEmail.setText("");
        txtPassword.setText("");
        txtRePassword.setText("");
    }
}