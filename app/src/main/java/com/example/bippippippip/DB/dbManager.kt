package com.example.bippippippip.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class dbManager(context: Context) {
    val DbHelper = DbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = DbHelper.writableDatabase
    }
    fun insertToDb(song: String, bpm:String){
        val values = ContentValues().apply {
            put(DbSongMap.COLUMN_NAME_SONGNAME, song)
            put(DbSongMap.COLUMN_NAME_SONGBPM, bpm)
        }
        db?.insert(DbSongMap.TABLE_NAME,null,values)
    }

    fun getDbData() : ArrayList<String> {
        val datalist = ArrayList<String>()
        val cursor = db?.query(
            DbSongMap.TABLE_NAME, null, null,
            null, null, null, null
        )

        with(cursor) {
            while (this?.moveToNext()!!) {
                val dataName = cursor?.getString(cursor.getColumnIndex(DbSongMap.COLUMN_NAME_SONGNAME))
                val dataBPM = cursor?.getString(cursor.getColumnIndex(DbSongMap.COLUMN_NAME_SONGBPM))
                datalist.add(dataName.toString())
            }
        cursor?.close()
        return datalist
        }
    }
    fun closeDb(){
        DbHelper.close()
    }


}