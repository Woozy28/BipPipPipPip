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



class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

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
        val playclick: Button = findViewById(R.id.play_but)
        val stopclick: Button = findViewById(R.id.stop_but)


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
            songmap.put(songinput.text.toString(),bpminput.text.toString().toInt()) //добавили название_трека : БПМ
            viewsong.add(songinput.text.toString()) // добавили название трека для списка фронта
            songlistadapter.notifyDataSetChanged()

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

        //Запуск клика
        playclick.setOnClickListener {
            playClick()
        }

        stopclick.setOnClickListener {
            stopSound()
        }

    }

    private fun playClick() {
        // Создаем MediaPlayer и загружаем звуковой файл из ресурсов
        mediaPlayer = MediaPlayer.create(this, R.raw.clic)

        // Устанавливаем обработчик окончания воспроизведения звука
        mediaPlayer?.setOnCompletionListener {
            stopSound()
        }

        // Начинаем воспроизведение звука
        mediaPlayer?.start()
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







//        soundPool =
//            val audioAttributes = Builder()
//                .setContentType(CONTENT_TYPE_SONIFICATION)
//                .setUsage(USAGE_ASSISTANCE_SONIFICATION)
//                .build();
//            SoundPool.Builder()
//                .setMaxStreams(3)
//                .setAudioAttributes(audioAttributes)
//                .build()

//        playclick.setOnClickListener {
//            soundPool!!.play(click,1f,1f,0,0,1f)
//            soundPool!!.autoPause()
//        }