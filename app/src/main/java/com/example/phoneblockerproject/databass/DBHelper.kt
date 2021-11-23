package com.example.phoneblockerproject.databass

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION)  {
    companion object {
         val DATABASE_VERSION = 1
         val DATABASE_NAME = "Database"
         val TABLE_CONTACTS = "phoneblocker"
         val KEY_ID = "id"
         val KEY_NAME = "name"
         val KEY_PHONE = "phoneNumber"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }
    fun addPhone(name : String, phone : String ){

        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_PHONE, phone)
        val db = this.writableDatabase

        db.insert(TABLE_CONTACTS, null, values)
        db.close()
    }
    fun deletePhone(id:String){

        val db = this.writableDatabase
        db.execSQL("delete from "+ TABLE_CONTACTS +" WHERE id ="+id);
    }


    fun getPhone(phone:String): Cursor? {

        val db = this.readableDatabase

        if (phone=="" || phone == null){
            return db.rawQuery("SELECT * FROM "+TABLE_CONTACTS, null)
        }else{
            return db.rawQuery("SELECT * FROM "+TABLE_CONTACTS+" WHERE ($KEY_NAME LIKE '%"+ phone+"%' or $KEY_PHONE LIKE '%"+phone+"%')", null)
        }
    }
}