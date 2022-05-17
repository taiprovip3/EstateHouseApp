package com.example.estatehouse.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.estatehouse.database.SQLiteDatabaseInstance;
import com.example.estatehouse.entity.User;

public class UserDao{

    private SQLiteDatabaseInstance instance;

    public UserDao(Context context){
        this.instance = SQLiteDatabaseInstance.getInstance(context);
    }

    public void addUser(User user){
        SQLiteDatabase db=instance.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("Id", user.getDocumentId());
        values.put("Avatar", user.getAvatar());
        values.put("Balance",user.getBalance());
        values.put("Email",user.getEmail());
        values.put("First_name",user.getFirstName());
        values.put("Last_name",user.getLastName());
        values.put("Location",user.getLocation());
        values.put("Password", user.getPassword());
        values.put("Phone_number",user.getPhonenumber());
        values.put("Role",user.getRole());
        db.insert("Users",null,values);
        db.close();
    }

    public void updateUser(User user, String userId){
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Avatar", user.getAvatar());
        values.put("Balance", user.getBalance());
        values.put("Email", user.getEmail());
        values.put("First_name", user.getFirstName());
        values.put("Last_name", user.getLastName());
        values.put("Location", user.getLocation());
        values.put("Password", user.getPassword());
        values.put("Phone_number", user.getPhonenumber());
        values.put("Role", user.getRole());
        db.update("Users", values, "Id = ?", new String[] {String.valueOf(userId)});
    }

    public boolean isUserExistedById(String userId){
        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Id = ?", new String[] {String.valueOf(userId)});
        return cursor.moveToFirst();
    }
}
