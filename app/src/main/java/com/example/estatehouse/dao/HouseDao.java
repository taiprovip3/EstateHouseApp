package com.example.estatehouse.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.estatehouse.database.SQLiteDatabaseInstance;
import com.example.estatehouse.entity.House;
import com.example.estatehouse.entity.User;

import java.util.UUID;

public class HouseDao{

    private SQLiteDatabaseInstance instance;

    public HouseDao(Context context){
        this.instance = SQLiteDatabaseInstance.getInstance(context);
    }

    public void deleteTable(String tableName) {
        SQLiteDatabase db = instance.getWritableDatabase();
        db.execSQL("delete from "+ tableName);
        db.close();
        Log.d("DELETED TABLE", tableName);
    }
    public void selectFromTableTest(){
        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Tags ", null);
        if (c.moveToFirst()){
            do {
                // Passing values
                String column1 = c.getString(0);
                String column2 = c.getString(1);
                String column3 = c.getString(2);
                Log.w("DATA_SELECT1", c.getString(0));
                Log.w("DATA_SELECT2", c.getString(1));
                Log.w("DATA_SELECT3", c.getString(2));
                // Do something Here with values
            } while(c.moveToNext());
        }
        c.close();
        db.close();
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

        Log.e("LIST_GETTAGS", ""+house.getTags());
        ContentValues values1 = new ContentValues();
        for (int i = 0; i< house.getTags().size(); i++){
            Log.w("TAG_ID", house.getTags().get(i));
            values1.put("Tagid", UUID.randomUUID().toString());
            values1.put("Houseid", house.getDocumentId());
            values1.put("Tag", house.getTags().get(i));
        }
//        values1.put("Tagid", "Tagid1");
//        values1.put("Houseid", house.getDocumentId());
//        values1.put("Tag", house.getTags().get(0));
        db.insert("Houses", null, values);
        db.insert("Tags", null, values1);
        Log.d("Registed a new house", "added a new house :: " + house);
        db.close();
    }

    public boolean isHouseExistedById(String houseId){
        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Houses WHERE Houseid = ?", new String[] {String.valueOf(houseId)});
        return cursor.moveToFirst();
    }

    public void deleteHouse(String houseId){
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete("Houses", "Houseid = ?", new String[] {String.valueOf(houseId)});
        Log.d("DELETE HOUSE DAO", "DELETE SUCCESS A HOUSE FROM SQLITE");
    }

    public void updateHouse(House house, String houseId){
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
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
        db.update("Houses", values, "Houseid = ?", new String[] {String.valueOf(houseId)});
    }
}