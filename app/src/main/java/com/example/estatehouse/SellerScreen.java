package com.example.estatehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estatehouse.dao.HouseDao;
import com.example.estatehouse.database.SQLiteDatabaseInstance;
import com.example.estatehouse.entity.House;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import vn.thanguit.toastperfect.ToastPerfect;

public class SellerScreen extends AppCompatActivity {

    FirebaseFirestore db;
    CollectionReference houseRef;
    FirebaseStorage storage;
    StorageReference storageReference, ref;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Button btnReset, btnRegister;
    EditText edType, edPrice, edAddress, edSale, edTags, edDescription, edBedroom, edBathroom, edLivingArea;
    ImageView homePage, imageChosen;
    TextView btnChooseImage;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private String randomImageSelectedNameGenerated = "house_description_1.png";
    private String documentId = UUID.randomUUID().toString();
    HouseDao houseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_screen);

        anhXa();
        onClick();
    }

    private void onClick() {
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emptyField();
            }
        });
        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerScreen.this, HomepageScreen.class);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = edType.getText().toString();
                double price = Double.parseDouble(edPrice.getText().toString());
                String address = edAddress.getText().toString();
                int sale = Integer.parseInt(edSale.getText().toString());
                List<String> tags = Arrays.asList("NEW", edTags.getText().toString());
                String description = edDescription.getText().toString();
                int bedroom = Integer.parseInt(edBedroom.getText().toString());
                int bathroom = Integer.parseInt(edBathroom.getText().toString());
                int livingArea = Integer.parseInt(edLivingArea.getText().toString());
                if(dataIsValid(type, price, address, sale, tags, description, bedroom, bathroom, livingArea)){
                    randomImageSelectedNameGenerated = UUID.randomUUID().toString();
                    currentUser = mAuth.getCurrentUser();
                    String seller = currentUser.getEmail();
                    Map<String, Object> data = new HashMap<>();
                    data.put("type", type);
                    data.put("cost", price);
                    data.put("address", address);
                    data.put("sale", sale);
                    data.put("tags", tags);
                    data.put("bedrooms", bedroom);
                    data.put("bathrooms", bathroom);
                    data.put("livingarea", livingArea);
                    data.put("image", randomImageSelectedNameGenerated);
                    data.put("description", description);
                    data.put("seller", seller);
                    data.put("documentId", documentId);
                    houseRef.document(documentId)
                            .set(data);
                    ToastPerfect.makeText(SellerScreen.this, ToastPerfect.SUCCESS, "Register successfully product :: " + documentId, ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                    uploadImage();
                    emptyField();
                    House house = new House();
                    house.setDocumentId(documentId);
                    house.setType(type);
                    house.setCost(price);
                    house.setAddress(address);
                    house.setSale(sale);
                    house.setTags(tags);
                    house.setBedrooms(bedroom);
                    house.setBathrooms(bathroom);
                    house.setLivingarea(livingArea);
                    house.setImage(randomImageSelectedNameGenerated);
                    house.setDescription(description);
                    house.setSeller(seller);
                    houseDao.registerHouse(house);
                } else ToastPerfect.makeText(SellerScreen.this, ToastPerfect.ERROR, "Please fill out fields required!", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
            }
        });
    }
    private void anhXa() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        houseRef = db.collection("houses");
        btnChooseImage = findViewById(R.id.sll_btnChooseImage);
        btnReset = findViewById(R.id.sll_btnReset);
        btnRegister = findViewById(R.id.sll_btnRegister);
        edType = findViewById(R.id.sll_edType);
        edPrice = findViewById(R.id.sll_edPrice);
        edAddress = findViewById(R.id.sll_edAddress);
        edSale = findViewById(R.id.sll_edSale);
        edTags = findViewById(R.id.sll_edTags);
        edDescription = findViewById(R.id.sll_edDescription);
        edBedroom = findViewById(R.id.sll_edBedroom);
        edBathroom = findViewById(R.id.sll_edBathroom);
        edLivingArea = findViewById(R.id.sll_edLivingArea);
        homePage = findViewById(R.id.sll_btnBack);
        imageChosen = findViewById(R.id.sll_imageChosen);
        houseDao = new HouseDao(this);
    }

    private void uploadImage() {
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            ref = storageReference.child("images/"+ randomImageSelectedNameGenerated);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ToastPerfect.makeText(SellerScreen.this, ToastPerfect.SUCCESS, "Uploaded successfully!", ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            ToastPerfect.makeText(SellerScreen.this, ToastPerfect.ERROR, "Upload failed :: " + e.getMessage(), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/png");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
    }

    private boolean dataIsValid(String type, double price, String address, int sale, List<String> tags, String description, int bedroom, int bathroom, int livingArea) {
        if(!type.equals("BUY") && !type.equals("RENT")){
            Toast.makeText(this, "Type must be 'BUY' or 'RENT' (case sensitive)", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(price <= 0){
            Toast.makeText(this, "Price must > 0", Toast.LENGTH_LONG).show();
            return false;
        }
        if(address.equals(" ")){
            Toast.makeText(this, "Address must not empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(sale < 0 || sale >= 100){
            Toast.makeText(this, "0 < Sale < 100", Toast.LENGTH_LONG).show();
            return false;
        }
        if(description.equalsIgnoreCase("")){
            Toast.makeText(this, "Please describe your description", Toast.LENGTH_LONG).show();
            return false;
        }
        if(bedroom <= 0 || bathroom <= 0 || livingArea <= 0){
            Toast.makeText(this, "Bed, bath, area must not be negative", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void emptyField() {
        edType.setText("");
        edPrice.setText("");
        edAddress.setText("");
        edSale.setText("");
        edTags.setText("");
        edDescription.setText("");
        edBedroom.setText("");
        edBathroom.setText("");
        edLivingArea.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageChosen.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}