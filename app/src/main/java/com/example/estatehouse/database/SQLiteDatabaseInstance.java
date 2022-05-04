package com.example.estatehouse.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;

public class SQLiteDatabaseInstance extends SQLiteOpenHelper {

    private static SQLiteDatabaseInstance instance = null;

    private static final String DATABASE_NAME = "EstateHouse.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    private static final String TABLE_HOUSES = "Houses";
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

    private static final String TABLE_USERS = "Users";
    private static final String KEY_USERID="Id";
    private static final String KEY_AVATAR="Avatar";
    private static final String KEY_BALANCE="Balance";
    private static final String KEY_EMAIL="Email";
    private static final String KEY_FIRST_NAME="First_name";
    private static final String KEY_LAST_NAME="Last_name";
    private static final String KEY_LOCATION="Location";
    private static final String KEY_PASSWORD="Password";
    private static final String KEY_PHONE_NUMBER="Phone_number";
    private static final String KEY_ROLE="Role";

    private static final String TABLE_TAGS="Tags";
    private static final String KEY_TAGID = "Tagid";
    private static final String KEY_TAG = "Tag";

    private static final String TABLE_CARTS = "Carts";
    private static final String KEY_CARTID = "Cartid";

    public static SQLiteDatabaseInstance getInstance(Context context){
        if(instance == null){
            instance = new SQLiteDatabaseInstance(context.getApplicationContext());
        }
        return instance;
    }

    public SQLiteDatabaseInstance(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        String create_houses_table = "CREATE TABLE " + TABLE_HOUSES + "("+KEY_HOUSEID+" TEXT PRIMARY KEY,"+KEY_TYPE+" TEXT,"+KEY_COST+" DOUBLE,"+KEY_ADDRESS+" TEXT,"+KEY_SALE+" DOUBLE,"+KEY_BEDROOMS+" INTEGER,"+KEY_BATHROOMS+" INTEGER,"+KEY_LIVINGAREA+" INTEGER,"+KEY_IMAGE+" TEXT,"+KEY_DESCRIPTION+" TEXT,"+KEY_SELLER+" TEXT)";

        String create_tags_table = "CREATE TABLE "+TABLE_TAGS+" ("+KEY_TAGID+" INTEGER PRIMARY KEY, "+KEY_HOUSEID+" TEXT, "+KEY_TAG+" TEXT)";

        String create_users_table="CREATE TABLE "+TABLE_USERS+"("
                +KEY_USERID+" TEXT PRIMARY KEY,"+KEY_AVATAR+" TEXT,"+KEY_BALANCE+" DOUBLE,"+KEY_EMAIL+" TEXT,"
                +KEY_FIRST_NAME+" TEXT,"+KEY_LAST_NAME+" TEXT,"+KEY_LOCATION+" TEXT,"+KEY_PASSWORD+" TEXT,"
                +KEY_PHONE_NUMBER+" TEXT,"+KEY_ROLE+" TEXT)";

        String create_carts_table = "CREATE TABLE name (id TEXT PRIMARY KEY, email TEXT, cost DOUBLE, seller TEXT, bed INTEGER, bath INTEGER, living INTEGER, image TEXT)";

        Log.d("OnCreate table created ", TABLE_HOUSES);
        Log.d("OnCreate table created ", TABLE_TAGS);
        Log.d("OnCreate table created ", TABLE_USERS);
        Log.d("OnCreate table created ", TABLE_CARTS);
        db.execSQL(create_houses_table);
        db.execSQL(create_tags_table);
        db.execSQL(create_users_table);
        db.execSQL(create_carts_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d("onUpgrade table ::", TABLE_HOUSES);
        Log.d("onUpgrade table ::", TABLE_TAGS);
        Log.d("onUpgrade table ::", TABLE_USERS);
        Log.d("onUpgrade table ::", TABLE_CARTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_HOUSES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_TAGS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_CARTS);
        onCreate(sqLiteDatabase);
    }
}
