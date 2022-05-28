package com.example.estatehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estatehouse.adapter.HomepageAdapter;
import com.example.estatehouse.adapter.ProductAdapter;
import com.example.estatehouse.entity.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vn.thanguit.toastperfect.ToastPerfect;

public class THCK extends AppCompatActivity {

    TextView btnAddView;
    ListView listView;
    Button btnAdd;
    List<Product> products;
    FirebaseFirestore db;
    EditText edProductIdView, edProductNameView, edProductPriceVIew;
    TextView txtChooseImageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    String productImageName = "default.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thck);

        btnAddView = findViewById(R.id.btnUpdate);
        listView = findViewById(R.id.product_listView);
        db = FirebaseFirestore.getInstance();
        btnAdd = findViewById(R.id.btnAdd);
        edProductIdView = findViewById(R.id.edProductIdView);
        edProductNameView = findViewById(R.id.edProductNameView);
        edProductPriceVIew = findViewById(R.id.edProductPriceView);
        txtChooseImageView = findViewById(R.id.txtChooseImageView);


        products = new ArrayList<>();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Product p = documentSnapshot.toObject(Product.class);
                                products.add(p);
                            }
                            ProductAdapter productAdapter = new ProductAdapter(products, THCK.this);
                            listView.setAdapter(productAdapter);
                        } else
                            Toast.makeText(THCK.this, "FAILTED TASK", Toast.LENGTH_SHORT).show();
                    }
                });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productId = UUID.randomUUID().toString();
                String productName = edProductNameView.getText().toString();
                double productPrice = Double.parseDouble(edProductPriceVIew.getText().toString());
                productImageName = productId;

                Product p = new Product();

                p.setDocumentId(productId);
                p.setName(productName);
                p.setPrice(productPrice);
                p.setImage(productName);

                db.collection("products")
                        .document(productId)
                        .set(p);

                uploadImage();
            }
        });
        txtChooseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/png");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    private void uploadImage() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            StorageReference imageRef = storageReference.child("images/" + productImageName);
            imageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imageChosen.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}