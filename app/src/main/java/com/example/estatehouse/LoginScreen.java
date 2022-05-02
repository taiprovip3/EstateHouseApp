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

public class LoginScreen extends AppCompatActivity {
    private EditText txtName, txtPass;
    private FirebaseAuth auth;
    private Button btnLog;
    private TextView txtRegisterHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        auth=FirebaseAuth.getInstance();
        txtName=(EditText) findViewById(R.id.txtName);
        txtPass=(EditText) findViewById(R.id.txtPass);
        txtRegisterHere=(TextView)findViewById(R.id.txtRegisterHere);
        txtRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginScreen.this, RegisterScreen.class);
                startActivity(intent);
                finish();
            }
        });
        btnLog=(Button) findViewById(R.id.btnLog);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=txtName.getText().toString();
                String pass=txtPass.getText().toString();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Enter user name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(),"Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(LoginScreen.this, "Login successully, we redirect you to homepage", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(LoginScreen.this,HomepageScreen.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}