package com.example.estatehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import vn.thanguit.toastperfect.ToastPerfect;

public class LoginScreen extends AppCompatActivity {
    private EditText txtName, txtPass;
    private FirebaseAuth auth;
    private Button btnLog;
    private TextView txtRegisterHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        anhXa();
        onClick();
    }

    private void onClick() {
        txtRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginScreen.this, RegisterScreen.class);
                startActivity(intent);
                finish();
            }
        });
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=txtName.getText().toString();
                String pass=txtPass.getText().toString();
                if (TextUtils.isEmpty(email)){
                    ToastPerfect.makeText(LoginScreen.this, ToastPerfect.ERROR, "Please enter username", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    ToastPerfect.makeText(LoginScreen.this, ToastPerfect.ERROR, "Please enter password", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    ToastPerfect.makeText(LoginScreen.this, ToastPerfect.ERROR, "Login failed, please check your network connection.", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                                }
                                else {
                                    ToastPerfect.makeText(LoginScreen.this, ToastPerfect.SUCCESS, "Login successully, you were redirected to HomepageScreen.java", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                                    Intent intent=new Intent(LoginScreen.this,HomepageScreen.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    private void anhXa() {
        auth = FirebaseAuth.getInstance();
        txtName=(EditText) findViewById(R.id.txtName);
        txtPass=(EditText) findViewById(R.id.txtPass);
        txtRegisterHere=(TextView)findViewById(R.id.txtRegisterHere);
        btnLog=(Button) findViewById(R.id.btnLog);
    }
}