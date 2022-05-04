package com.example.estatehouse.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.estatehouse.entity.House;

public class HouseDao extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EstateHouse";
    private static final String TABLE_HOUSE_NAME = "Houses";
    private static final String KEY_HOUSEID = "Houseid";
    private static final String KEY_TYPE = "Type";
    private static final String KEY_COST = "Cost";
    private static final String KEY_ADDRESS = "Address";
    private static final String KEY_SALE = "Sale";
    private static final String KEY_BEDROOMS = "Bedrooms";
    private static final String KEY_BATHROOMS = "Bathrooms";
    private static final String KEY_LIVINGAREA = "Livingarea";
    private static final String KEY_IMAGE = "Image";
    private static final String KEY_DESCRIPTION = "Description";
    private static final String KEY_SELLER = "Seller";

    private static final String TABLE_TAG_NAME="Tags";
    private static final String KEY_TAGID = "Tagid";
    private static final String KEY_TAG = "Tag";


    public HouseDao(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        String scriptHouse = "CREATE TABLE x (x TEXT PRIMARY KEY, type TEXT, cost DOUBLE, address TEXT, sale INTEGER, bedroom INTEGER, bathroom INTEGER, livingarea INTEGER, image TEXT, des TEXT, seller TEXT)";
        String scriptHouse = "CREATE TABLE " + TABLE_HOUSE_NAME + " ("+KEY_HOUSEID+" TEXT PRIMARY KEY, "+KEY_TYPE+" TEXT, "+KEY_COST+" DOUBLE, "+KEY_ADDRESS+" TEXT, "+KEY_SALE+" DOUBLE, "+KEY_BEDROOMS+" INTEGER, "+KEY_BATHROOMS+" INTEGER, "+KEY_LIVINGAREA+" INTEGER, "+KEY_IMAGE+" TEXT, "+KEY_DESCRIPTION+" TEXT, "+KEY_SELLER+" TEXT)";
//        String scriptTag = "CREATE TABLE "+TABLE_TAG_NAME+" ("+KEY_TAGID+" INTEGER PRIMARY KEY, "+KEY_HOUSEID+" TEXT, "+KEY_TAG+" TEXT)";
        sqLiteDatabase.execSQL(scriptHouse);
//        sqLiteDatabase.execSQL(scriptTag);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG_NAME);
        onCreate(sqLiteDatabase);
    }

    public void registerHouse (House house){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_HOUSEID, house.getDocumentId());
        values.put(KEY_TYPE, house.getType());
        values.put(KEY_COST, house.getCost());
        values.put(KEY_ADDRESS, house.getAddress());
        values.put(KEY_SALE, house.getSale());
        values.put(KEY_BEDROOMS, house.getBedrooms());
        values.put(KEY_BATHROOMS, house.getBathrooms());
        values.put(KEY_LIVINGAREA, house.getLivingarea());
        values.put(KEY_IMAGE, house.getImage());
        values.put(KEY_DESCRIPTION, house.getDescription());
        values.put(KEY_SELLER, house.getSeller());

//        ContentValues values1 = new ContentValues();
//        for (int i = 0; i< house.getTags().size(); i++){
//            values1.put(KEY_TAGID, "");
//            values1.put(KEY_HOUSEID, house.getDocumentId());
//            values1.put(KEY_TAG, house.getTags().get(i));
//        }
        db.insert(TABLE_HOUSE_NAME, null, values);
//        db.insert(TABLE_TAG_NAME, null, values1);
        Log.d("REGISTER HOUSE DAO", "REGISTED SUCCESS HOUSE FROM SQLITE");
        db.close();
    }

    public void deleteHouse(String houseId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOUSE_NAME, KEY_HOUSEID + " = " + houseId, null);
        Log.d("DELETE HOUSE DAO", "DELETE SUCCESS A HOUSE FROM SQLITE");
    }
}
