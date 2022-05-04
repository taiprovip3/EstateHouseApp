package com.example.estatehouse.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.estatehouse.database.SQLiteDatabaseInstance;
import com.example.estatehouse.entity.House;
import com.example.estatehouse.entity.User;

public class HouseDao{

    private SQLiteDatabaseInstance instance;

    public HouseDao(Context context){
        this.instance = SQLiteDatabaseInstance.getInstance(context);
    }

    public void registerHouse (House house){
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Houseid", house.getDocumentId());
        values.put("Type", house.getType());
        values.put("Cost", house.getCost());
        values.put("Address", house.getAddress());
        values.put("Sale", house.getSale());
        values.put("Bedrooms", house.getBedrooms());
        values.put("Bathrooms", house.getBathrooms());
        values.put("Livingarea", house.getLivingarea());
        values.put("Image", house.getImage());
        values.put("Description", house.getDescription());
        values.put("Seller", house.getSeller());

        ContentValues values1 = new ContentValues();
        for (int i = 0; i< house.getTags().size(); i++){
            values1.put("Tagid", "");
            values1.put("Houseid", house.getDocumentId());
            values1.put("Tag", house.getTags().get(i));
        }
        db.insert("Houses", null, values);
        db.insert("Tags", null, values1);
        Log.d("Registed a new house", "added a new house :: " + house);
        db.close();
    }

    public void deleteHouse(String houseId){
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete("Houses", "Houseid = " + houseId, null);
        Log.d("DELETE HOUSE DAO", "DELETE SUCCESS A HOUSE FROM SQLITE");
    }
}