package com.example.bippippippip

import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {



    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        //Инициализируем хэлпер для sql
        val db = DbHelper(this,null)


        //Списки для вьюх и запуска клика в медиаплеере
        val songmap = mutableMapOf<String,String>() //список с бипиэмами
        val viewsong = mutableListOf<String>() //Список песен
        var now_playing:String = ""
        var startstop: Int = 1



        //кнопки и вьюхи с фронта (надо переделать на байдинг)
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

        //адаптер для списка
        val songlistadapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,viewsong)
        songview.adapter = songlistadapter

        //прокидываем из БД таблицу в мап для вью
            db.getSong().forEach { (i, j) ->
            songmap[i] = j
            viewsong.add(i)
            songlistadapter.notifyDataSetChanged()
        }





        //выбор трека
        songview.setOnItemClickListener { parent, view, position, id ->
            bpminput.setText(songmap.get(songlistadapter.getItem(position).toString()).toString())
            now_playing = parent.getItemAtPosition(position).toString() //текущее название трека
            bpmspin.setProgress(songmap.get(now_playing)!!.toInt())


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
            if (viewsong.indexOf(songinput.text.toString()) >= 0){
                //Алерт при добавлении трека с одинаковым названием трека.
                val addalert = AlertDialog.Builder(this@MainActivity)
                addalert.setTitle("Can't do that")
                addalert.setMessage("Need new name of song")
                addalert.setPositiveButton("OK"){_,_ ->

                }

                addalert.show()
            }
            else{
                songmap.put(songinput.text.toString(),bpminput.text.toString()) //добавили название_трека : БПМ
                viewsong.add(songinput.text.toString()) // добавили название трека для списка фронта
                songlistadapter.notifyDataSetChanged()

                //добавляем в БД
                val value = Song(songinput.text.toString(), bpminput.text.toString())
                db.addSong(value)

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

                //удаляем из БД
                db.delSong(now_playing)
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

