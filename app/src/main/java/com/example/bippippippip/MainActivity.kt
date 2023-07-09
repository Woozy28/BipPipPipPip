package com.example.bippippippip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.SeekBar
import androidx.core.view.get


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val songmap = mutableMapOf<String,Int>("First song" to 120, "Second song" to 140) //список с бипиэмами
        val viewsong = mutableListOf<String>("First song","Second song") //Список песен
        var now_playing:String = ""

        //всё с фронта
        val songview: GridView = findViewById(R.id.view_song)
        val bpminput: EditText = findViewById(R.id.input_bpm)
        val bpmspin: SeekBar = findViewById(R.id.seekBar)
        val songinput: EditText = findViewById(R.id.input_song)
        val addnew: Button = findViewById(R.id.add_but)
        val deletesong: Button = findViewById(R.id.del_but)


        //адаптер для списка
        val songlistadapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,viewsong)
        songview.adapter = songlistadapter

        //выбор трека
        songview.setOnItemClickListener { parent, view, position, id ->
            bpminput.setText(songmap.get(songlistadapter.getItem(position).toString()).toString())
            now_playing = parent.getItemAtPosition(position).toString() //текущее название трека

        }

        //листенер сикбара
        bpmspin.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                bpminput.setText(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }


        })

        //добавление трека в список
        addnew.setOnClickListener {
            songmap.put(songinput.text.toString(),bpminput.text.toString().toInt()) //добавили название_трека : БПМ
            viewsong.add(songinput.text.toString()) // добавили название трека для списка фронта
            songlistadapter.notifyDataSetChanged()

        }

        //Удаление трека
        deletesong.setOnClickListener {
            viewsong.remove(now_playing)
            songmap.remove(now_playing)
            songlistadapter.notifyDataSetChanged()
        }

    }


}