package com.example.estatehouse.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.estatehouse.database.SQLiteDatabaseInstance;
import com.example.estatehouse.entity.HouseCart;

public class CartDao {

    private SQLiteDatabaseInstance instance;

    public CartDao(Context context) {
        this.instance = SQLiteDatabaseInstance.getInstance(context.getApplicationContext());
    }

    public void addToCart(HouseCart cart){
        //..do somethings here
    }

    public void deleteCart(String cartId){
        //..do your code here
    }

}
