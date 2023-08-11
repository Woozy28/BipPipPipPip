package com.example.bippippippip.DB

import android.provider.BaseColumns

object DbSongMap {
    const val TABLE_NAME = "songmap"
    const val COLUMN_NAME_SONGNAME = "songname"
    const val COLUMN_NAME_SONGBPM = "songbpm"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "songmapDb.db"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME("+
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$COLUMN_NAME_SONGNAME TEXT, " +
            "$COLUMN_NAME_SONGBPM TEXT)"

    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}