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

        //Table Phone adding
         val TABLE_PHONEBLOCKER = "phoneblocker"
         val PHONEBLOCKER_ID = "id"
         val PHONEBLOCKER_NAME = "name"
         val PHONEBLOCKER_PHONE = "phoneNumber"

        //Table Phone spaming
        val TABLE_PHONESPAMER = "phonespamer"
        val PHONESPAMER_ID = "id"
        val PHONESPAMER_NAME = "name"
        val PHONESPAMER_PHONE = "phoneNumber"

        //Table Phone history
        val TABLE_PHONEHISTORY = "phones_blockhistory"
        val PHONEHISTORY_ID = "id"
        val PHONEHISTORY_NAME = "name"
        val PHONEHISTORY_PHONE = "phoneNumber"
        val PHONEHISTORY_datetime = "datetime"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //Table Phone adding
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_PHONEBLOCKER + "("
                + PHONEBLOCKER_ID + " INTEGER PRIMARY KEY," + PHONEBLOCKER_NAME + " TEXT,"
                + PHONEBLOCKER_PHONE + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)

        val CREATE_HISTORY_TABLE = ("CREATE TABLE " + TABLE_PHONEHISTORY + "("
                + PHONEHISTORY_ID + " INTEGER PRIMARY KEY," + PHONEHISTORY_NAME + " TEXT,"
                + PHONEHISTORY_PHONE + " TEXT,"+ PHONEHISTORY_datetime+" TEXT" + ")")
        db?.execSQL(CREATE_HISTORY_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONEBLOCKER)
        onCreate(db)
    }


    // add phone
    fun addPhone(name : String, phone : String ){

        val values = ContentValues()
        values.put(PHONEBLOCKER_NAME, name)
        values.put(PHONEBLOCKER_PHONE, phone)
        val db = this.writableDatabase

        db.insert(TABLE_PHONEBLOCKER, null, values)
        db.close()
    }
    fun deletePhone(id:String){

        val db = this.writableDatabase
        db.execSQL("delete from "+ TABLE_PHONEBLOCKER +" WHERE id ="+id);
    }

    fun getPhone(phone:String): Cursor? {
        val db = this.readableDatabase
        if (phone=="" || phone == null){
            return db.rawQuery("SELECT * FROM "+TABLE_PHONEBLOCKER, null)
        }else{
            return db.rawQuery("SELECT * FROM "+TABLE_PHONEBLOCKER+" WHERE ($PHONEBLOCKER_NAME LIKE '%"+ phone+"%' or $PHONEBLOCKER_PHONE LIKE '%"+phone+"%')", null)
        }
    }

    fun getBlocknumber():ArrayList<String>{
        val db = this.readableDatabase
        var phone = ArrayList<String>()
        var i = db.rawQuery("SELECT * FROM "+TABLE_PHONEBLOCKER, null)
            while (i.moveToNext()){
                phone.add(i.getString(2))
            }
        return  phone
    }


    //historyphone



}