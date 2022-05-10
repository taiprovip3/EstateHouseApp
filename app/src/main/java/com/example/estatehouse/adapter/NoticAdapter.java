package com.example.estatehouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.estatehouse.R;
import com.example.estatehouse.entity.HouseCart;
import com.example.estatehouse.entity.Notic;

import java.util.List;

public class NoticAdapter extends BaseAdapter {

    private List<Notic> notics;
    private Context context;

    public NoticAdapter(List<Notic> notics, Context context){
        this.notics = notics;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notics.size();
    }

    @Override
    public Object getItem(int i) {
        return notics.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if( view == null){
            view = LayoutInflater.from(context).inflate(R.layout.activity_notic_itemview, viewGroup, false);
        }
        ImageView imageView = view.findViewById(R.id.notic_imageView);
        TextView messageView = view.findViewById(R.id.notic_Message);
        TextView numberView = view.findViewById(R.id.notic_numberView);

        Notic n = notics.get(i);
        Context contextImageView = imageView.getContext();
        int id = contextImageView.getResources().getIdentifier(n.getImage(), "drawable", contextImageView.getPackageName());
        imageView.setImageResource(id);
        messageView.setText(n.getMessage());
        numberView.setText("" + n.getNumber());
        return view;
    }
}
