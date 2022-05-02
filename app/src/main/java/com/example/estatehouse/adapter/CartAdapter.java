package com.example.estatehouse.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.estatehouse.CartScreen;
import com.example.estatehouse.R;
import com.example.estatehouse.entity.HouseCart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class CartAdapter extends BaseAdapter {
    private List<HouseCart> houseCarts;
    private Context context;

    public CartAdapter(List<HouseCart> houseCarts, Context context){
        this.houseCarts = houseCarts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return houseCarts.size();
    }

    @Override
    public Object getItem(int i) {
        return houseCarts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.activity_cart_itemview, viewGroup, false);

        ImageView imageView = view.findViewById(R.id.cart_Image);
        TextView costView = view.findViewById(R.id.cart_Cost);
        TextView sellerView = view.findViewById(R.id.cart_Seller);
        TextView bedroomsView = view.findViewById(R.id.cart_Bedrooms);
        TextView bathroomsView = view.findViewById(R.id.cart_Bathrooms);
        TextView livingAreaView = view.findViewById(R.id.cart_Livingarea);
        ImageView removeItem = view.findViewById(R.id.cart_remoteItem);

        HouseCart hc = houseCarts.get(i);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://estatehouse-4ee84.appspot.com");
        StorageReference cartReference = storageReference.child("images/"+hc.getImage());
        cartReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get()
                                .load(uri)
                                .into(imageView);
                    }
                });
        costView.setText("$"+hc.getCost()+" (USD)");
        sellerView.setText(hc.getSeller());
        bedroomsView.setText("" + hc.getBedrooms() + " bedrooms");
        bathroomsView.setText(""+hc.getBathrooms() + " bathrooms");
        livingAreaView.setText(""+hc.getLivingarea() + "m²");

        removeItem = view.findViewById(R.id.cart_remoteItem);
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cartId = hc.getDocumentId();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("carts").document(cartId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Removing...", Toast.LENGTH_LONG).show();
                                Intent intent= new Intent(context, CartScreen.class);
                                context.startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Error deleting document" + e, Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        return view;
    }
}
