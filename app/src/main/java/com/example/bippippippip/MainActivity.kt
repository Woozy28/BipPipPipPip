package com.example.bippippippip

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val songmap = mutableMapOf<String,Int>("First song" to 120, "Second song" to 140) //список с бипиэмами
        val viewsong = mutableListOf<String>("First song","Second song") //Список песен

        var songview: GridView = findViewById(R.id.view_song)
        val songlistadapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,viewsong)
        songview.adapter = songlistadapter
    }


}