package com.example.phoneblockerproject.databass

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


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

        //Table  history block
        val TABLE_BLOCKEHISTORY = "phones_blockhistory"
        val BLOCKHISTORY_ID = "id"
        val BLOCKHISTORY_NAME = "name"
        val BLOCKHISTORY_PHONE = "phoneNumber"
        val BLOCKHISTORY_datetime = "datetime"
        val BLOCKHISTORY_status = "status" //0 = phone || 1 = sms

        //Table test
        val TABLE_TEST = "testTABLE"
        val TABLE_TEST_id ="id"
        val TABLE_TEST_name ="name"
        val TABLE_TEST_email="email"

        //Table smsblock
        val TABLE_SMSBLOCK = "smsblock"
        val TABLE_SMSBLOCK_id ="id"
        val TABLE_SMSBLOCK_thread_id ="thread_id"
        val TABLE_SMSBLOCK_address="address"
    }



    override fun onCreate(db: SQLiteDatabase?) {
        //Table Phone adding
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_PHONEBLOCKER + "("
                + PHONEBLOCKER_ID + " INTEGER PRIMARY KEY," + PHONEBLOCKER_NAME + " TEXT,"
                + PHONEBLOCKER_PHONE + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)

        //Table history
        val CREATE_HISTORY_TABLE = ("CREATE TABLE " + TABLE_BLOCKEHISTORY + "("
                + BLOCKHISTORY_ID + " INTEGER PRIMARY KEY," + BLOCKHISTORY_NAME + " TEXT,"
                + BLOCKHISTORY_PHONE + " TEXT,"+ BLOCKHISTORY_datetime+" TEXT," + BLOCKHISTORY_status+" TEXT" +")")
        db?.execSQL(CREATE_HISTORY_TABLE)

        //Table data from server
        val CREATE_PHONESPAM_TABLE = ("CREATE TABLE " + TABLE_PHONESPAMER + "("
                + PHONESPAMER_ID + " INTEGER PRIMARY KEY," + PHONESPAMER_NAME + " TEXT,"
                + PHONESPAMER_PHONE + " TEXT" + ")")
        db?.execSQL(CREATE_PHONESPAM_TABLE)

        //Table test
        val CREATE_TEST_TABLE = ("CREATE TABLE " + TABLE_TEST + "("
                + TABLE_TEST_id + " INTEGER PRIMARY KEY," + TABLE_TEST_name + " TEXT,"
                + TABLE_TEST_email + " TEXT" + ")")
        db?.execSQL(CREATE_TEST_TABLE)

        //Table smsblock
        val CREATE_SMSBLOCK_TABLE = ("CREATE TABLE " + TABLE_SMSBLOCK + "("
                + TABLE_SMSBLOCK_id + " INTEGER PRIMARY KEY," + TABLE_SMSBLOCK_thread_id + " TEXT,"
                + TABLE_SMSBLOCK_address + " TEXT" + ")")
        db?.execSQL(CREATE_SMSBLOCK_TABLE)


    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONEBLOCKER)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOCKEHISTORY)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONESPAMER)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_SMSBLOCK)
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
    fun addhistory(name: String,phone:String,date:String,status:String){
        val values = ContentValues()
        values.put(BLOCKHISTORY_NAME, name)
        values.put(BLOCKHISTORY_PHONE, phone)
        values.put(BLOCKHISTORY_datetime, date)
        values.put(BLOCKHISTORY_status, status)
        val db = this.writableDatabase
        db.insert(TABLE_BLOCKEHISTORY, null, values)
        db.close()
    }


    fun gethistory(status: String): Cursor?{
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM "+TABLE_BLOCKEHISTORY+" WHERE "+BLOCKHISTORY_status+" ="+status +" ORDER BY "+ BLOCKHISTORY_ID +" DESC" , null)
    }


    //phonespamer data from server
    fun getAllphonespam():Cursor{
        val db= this.readableDatabase
        var sql ="SELECT * FROM "+TABLE_PHONESPAMER
        return  db.rawQuery(sql,null)
    }

    fun addPhoneSpam(name:String,phone:String){

        val values = ContentValues()
        val db = this.writableDatabase
        values.put(PHONESPAMER_NAME,name)
        values.put(PHONESPAMER_PHONE,phone)

        db.insert(TABLE_PHONESPAMER, null, values)
        db.close()
    }

    fun deletePhoneSpamer(){
        val db = this.writableDatabase
        db.execSQL("delete from "+ TABLE_PHONESPAMER )
    }

    //เพิ่มข้อมูล
    fun adddataTable_Test(name:String,email:String){
        val values = ContentValues()
        val db = this.writableDatabase
        values.put(TABLE_TEST_name,name)
        values.put(TABLE_TEST_email,email)
        //ชื่อ ตาราง
        db.insert(TABLE_TEST, null, values)
        db.close()
    }
    //แสดงข้อมูลบล็อก
    fun datafromTest():Cursor{
        val db = this.readableDatabase
        return  db.rawQuery("SELECT * FROM " +TABLE_TEST,null)
    }
    //ลบข้อมูล
    fun deleteTest(id: String){
        val db = this.writableDatabase
        db.execSQL("delete from "+ TABLE_TEST+" WHERE id = "+ id )


    }

    //เพิ่มข้อมูล
    fun addsms(thread_id:String,address:String){
        val values = ContentValues()
        val db = this.writableDatabase
        values.put(TABLE_SMSBLOCK_thread_id,thread_id)
        values.put(TABLE_SMSBLOCK_address,address)
        //ชื่อ ตาราง
        db.insert(TABLE_SMSBLOCK, null, values)
        db.close()
    }

    fun selectsms():Cursor{
        val db = this.readableDatabase
        return  db.rawQuery("SELECT * FROM " +TABLE_SMSBLOCK,null)
    }

    //ลบข้อมูล
    fun deletesmsblock(id: String){
        val db = this.writableDatabase
        db.execSQL("delete from "+ TABLE_SMSBLOCK+" WHERE id = "+ id )


    }

}