package com.example.estatehouse.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.estatehouse.entity.User;

public class UserDao{


    public UserDao(Context context){

    }

    public void addUser(User user){
//        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("", user.getDocumentId());
        values.put("", user.getAvatar());
        values.put("",user.getBalance());
        values.put("",user.getEmail());
        values.put("",user.getFirstName());
        values.put("",user.getLastName());
        values.put("",user.getLocation());
        values.put("", user.getPassword());
        values.put("",user.getPhonenumber());
        values.put("",user.getRole());
//        db.insert("",null,values);
        Log.d("New user registed", "added user :: " + user);
//        db.close();
    }
}
