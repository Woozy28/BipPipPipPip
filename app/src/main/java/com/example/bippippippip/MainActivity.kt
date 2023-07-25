package com.example.bippippippip

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {


    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE


        val songmap = mutableMapOf<String,Int>("First song" to 120, "Second song" to 140) //список с бипиэмами
        val viewsong = mutableListOf<String>("First song","Second song") //Список песен
        var now_playing:String = ""
        var startstop: Int = 1

        //всё с фронта
        val songview: GridView = findViewById(R.id.view_song)
        val bpminput: TextView = findViewById(R.id.input_bpm)
        val bpmspin: SeekBar = findViewById(R.id.seekBar)
        val songinput: EditText = findViewById(R.id.input_song)
        val addnew: Button = findViewById(R.id.add_but)
        val deletesong: Button = findViewById(R.id.del_but)
        val playclick: Button = findViewById(R.id.play_but)
        val stopclick: Button = findViewById(R.id.stop_but)
        val onePlus: Button = findViewById(R.id.onePlus)
        val oneMinus: Button = findViewById(R.id.oneMinus)


        //Алерт при добавлении трека с одинаковым названием трека.
        val addalert = AlertDialog.Builder(this@MainActivity)
        addalert.setTitle("Can't do that")
        addalert.setMessage("Need new name of song")
        addalert.setPositiveButton("OK"){_,_ ->

        }



        //адаптер для списка
        val songlistadapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,viewsong)
        songview.adapter = songlistadapter


        //выбор трека
        songview.setOnItemClickListener { parent, view, position, id ->
            bpminput.setText(songmap.get(songlistadapter.getItem(position).toString()).toString())
            now_playing = parent.getItemAtPosition(position).toString() //текущее название трека
            bpmspin.setProgress(songmap.get(now_playing)!!)
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
            if (viewsong.indexOf(songinput.text.toString()) > 0){
            addalert.show()
            }
            else{
                songmap.put(songinput.text.toString(),bpminput.text.toString().toInt()) //добавили название_трека : БПМ
                viewsong.add(songinput.text.toString()) // добавили название трека для списка фронта
                songlistadapter.notifyDataSetChanged()
            }

        }

        //Удаление трека
        deletesong.setOnClickListener {
            val deletalert = AlertDialog.Builder(this@MainActivity)
            deletalert.setTitle("Delete song")
            deletalert.setMessage("you want to delete ${now_playing}")
            deletalert.setPositiveButton("Delete"){dialog,which ->
                viewsong.remove(now_playing)
                songmap.remove(now_playing)
                songlistadapter.notifyDataSetChanged()
            }
            deletalert.setNeutralButton("Stop it!"){_,_ ->}

            deletalert.show()
        }

         val viewModelJob = Job()
         val uiScope = CoroutineScope(Main + viewModelJob)

        //Запуск клика
        playclick.setOnClickListener {
            val bpm:Int= 60000/bpminput.text.toString().toInt()
            playClick()
            startstop =1
            uiScope.launch {
                while(startstop == 1) {
                    mediaPlayer?.start()
                    delay(bpm.toLong())
                }
            }
        }

        //Остановка клика
        stopclick.setOnClickListener {
            startstop = 0
            stopSound()
        }

        onePlus.setOnClickListener{
            var counter: Int = bpminput.text.toString().toInt()
            counter++
            bpminput.setText(counter.toString())
            bpmspin.setProgress(counter)
        }

        oneMinus.setOnClickListener{
            var counter: Int = bpminput.text.toString().toInt()
            counter--
            bpminput.setText(counter.toString())
            bpmspin.setProgress(counter)
        }
    }


    private fun playClick() {
        // Создаем MediaPlayer и загружаем звуковой файл из ресурсов
        mediaPlayer = MediaPlayer.create(this, R.raw.clic)


    }

    private fun stopSound() {
        // Проверяем, был ли MediaPlayer создан
        mediaPlayer?.let {
            if (it.isPlaying) {
                // Останавливаем воспроизведение звука
                it.stop()
            }
            // Освобождаем ресурсы MediaPlayer
            it.release()
            mediaPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()

        // Освобождаем ресурсы MediaPlayer при закрытии активности или фрагмента
        stopSound()
    }

}

