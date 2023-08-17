package com.example.bippippippip

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

//Клас для работы с БД
class DbHelper(val context: Context, val factory: CursorFactory?):
    SQLiteOpenHelper(context,"songlist",factory,1){

    //Создаём таблицу
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE songs (id INT PRIMARY KEY, songname TEXT, songbpm TEXT)"
        db!!.execSQL(query)
    }

    //обновляем таблицу
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS songs")
        onCreate(db)
    }

    //добавляем новый трек в БД
    fun addSong(song: Song){
        val values = ContentValues()
        values.put("songname", song.nameSong)
        values.put("songbpm", song.bmmSong)

        val db = this.writableDatabase
        db.insert("songs",null, values)
        db.close()
    }

    //читаем всю БД. Возвращает мап = трек + бпм
    fun getSong() : MutableMap<String,String>{
        val db = this.readableDatabase
        val songMap = mutableMapOf<String,String>()

        val cursor = db.query("songs",null,null,null,null,null,null,null)

        with(cursor){
            while (moveToNext()) {
                val songout = getString(getColumnIndexOrThrow("songname"))
                val bpmout = getString(getColumnIndexOrThrow("songbpm"))
                songMap.put(songout,bpmout)
            }
        }

        cursor.close()
        return songMap
    }

    //удаляем трек из БД. ищем по имени трека (Возможно не работает!!!)
    fun delSong( name: String){
        val db = this.writableDatabase
        db.delete("songs","songname"+"=?", arrayOf(name))

    }


}