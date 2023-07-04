package com.example.bippippippip

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.SeekBar
import android.widget.Spinner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val songmap = mutableMapOf<String,Int>("First song" to 120, "Second song" to 140) //список с бипиэмами
        val viewsong = mutableListOf<String>("First song","Second song") //Список песен

        //всё с фронта
        val songview: GridView = findViewById(R.id.view_song)
        val bpminput: EditText = findViewById(R.id.input_bpm)
        val bpmspin: SeekBar = findViewById(R.id.seekBar)
        val songinput: EditText = findViewById(R.id.input_song)
        val addnew: Button = findViewById(R.id.add_but)

        //добавляем новый трек в список отображения и ключ для БПМ
        addnew.setOnClickListener {
            songmap.put(songinput.text.toString(),bpminput.text.toString().toInt())
            viewsong.add(songinput.text.toString())

        }

        //адаптер для списка
        val songlistadapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,viewsong)
        songview.adapter = songlistadapter

        bpmspin.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                bpminput.setText(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }


        })

        addnew.setOnClickListener {
            songmap.put(songinput.text.toString(),bpminput.text.toString().toInt())
            viewsong.add(songinput.text.toString())
            songlistadapter.notifyDataSetChanged()

        }

    }


}