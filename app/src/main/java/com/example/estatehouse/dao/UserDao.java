package com.example.estatehouse.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.estatehouse.entity.User;

public class UserDao extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="EstateHouse";
    private static final String TABLE_USERS="Users";
    private static final String KEY_ID="Id";
    private static final String KEY_AVATAR="Avatar";
    private static final String KEY_BALANCE="Balance";
    private static final String KEY_EMAIL="Email";
    private static final String KEY_FIRST_NAME="First_name";
    private static final String KEY_LAST_NAME="Last_name";
    private static final String KEY_LOCATION="Location";
    private static final String KEY_PASSWORD="Password";
    private static final String KEY_PHONE_NUMBER="Phone_number";
    private static final String KEY_ROLE="Role";

    public UserDao(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_users_table="CREATE TABLE "+TABLE_USERS+"("
                +KEY_ID+" TEXT PRIMARY KEY,"+KEY_AVATAR+" TEXT,"+KEY_BALANCE+" DOUBLE,"+KEY_EMAIL+" TEXT,"
                +KEY_FIRST_NAME+" TEXT,"+KEY_LAST_NAME+" TEXT,"+KEY_LOCATION+" TEXT,"+KEY_PASSWORD+" TEXT,"
                +KEY_PHONE_NUMBER+" TEXT,"+KEY_ROLE+" TEXT)";
        sqLiteDatabase.execSQL(create_users_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        onCreate(sqLiteDatabase);
    }

    public void addUser(User user){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_ID, user.getDocumentId());
        values.put(KEY_AVATAR, user.getAvatar());
        values.put(KEY_BALANCE,user.getBalance());
        values.put(KEY_EMAIL,user.getEmail());
        values.put(KEY_FIRST_NAME,user.getFirstName());
        values.put(KEY_LAST_NAME,user.getLastName());
        values.put(KEY_LOCATION,user.getLocation());
        values.put(KEY_PASSWORD,user.getPassword());
        values.put(KEY_PHONE_NUMBER,user.getPhonenumber());
        values.put(KEY_ROLE,user.getRole());
        db.insert(TABLE_USERS,null,values);
        Log.d("New user registed", "added user :: " + user);
        db.close();
    }
}
