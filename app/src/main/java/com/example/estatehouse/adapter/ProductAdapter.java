package com.example.estatehouse.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.estatehouse.R;
import com.example.estatehouse.entity.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private List<Product> products;
    private Context context;

    public ProductAdapter(List<Product> products, Context context){
        this.products=products;
        this.context=context;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if( view == null){
            view = LayoutInflater.from(context).inflate(R.layout.thck_itemview, viewGroup, false);
        }

        //Lấy view trong itemview layout
        ImageView productImageView = view.findViewById(R.id.productImageView);
        TextView productNameView = view.findViewById(R.id.productNameView);
        TextView productPriceView = view.findViewById(R.id.productPriceView);
        ImageView btnDelete = view.findViewById(R.id.btnDelete);

        Product p = products.get(i);

        //set thuộc tính cho view trên
            //set Image
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://estatehouse-4ee84.appspot.com");
            StorageReference imageReference = storageReference.child("images/" + p.getImage());
            imageReference.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get()
                                    .load(uri)
                                    .into(productImageView);
                        }
                    });
            //setName
            productNameView.setText(p.getName());
            //setPrice
            productPriceView.setText("$" + p.getPrice());
        return view;
    }
}
