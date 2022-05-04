package com.example.estatehouse.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.estatehouse.database.SQLiteDatabaseInstance;
import com.example.estatehouse.entity.HouseCart;

public class CartDao {

    private SQLiteDatabaseInstance instance;

    public CartDao(Context context) {
        this.instance = SQLiteDatabaseInstance.getInstance(context.getApplicationContext());
    }
    public void addToCart(HouseCart cart){
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", cart.getDocumentId());
        values.put("email", cart.getEmail());
        values.put("cost",cart.getCost());
        values.put("seller", cart.getSeller());
        values.put("bed", cart.getBedrooms());
        values.put("bath", cart.getBathrooms());
        values.put("living", cart.getLivingarea());
        values.put("image", cart.getImage());
        db.insert("Carts",null,values);
        Log.d("Registed a new house", "added a new cart :: " + cart);
        db.close();

    }

    public void deleteCart(String cartId){
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete("Carts", "id = " + cartId, null);
        Log.d("DELETE Cart DAO", "DELETE SUCCESS A Carts FROM SQLITE");
    }

}
