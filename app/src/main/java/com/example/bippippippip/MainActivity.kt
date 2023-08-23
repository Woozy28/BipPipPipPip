package com.example.bippippippip

import android.content.pm.ActivityInfo
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bippippippip.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {


    lateinit var bind  : ActivityMainBinding
    private var soundPool: SoundPool? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //биндинг для вьюшек
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //задаём параметры для саундпула и проверяем актуальную версию СДК
        soundPool = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(
                    AudioAttributes.USAGE_ASSISTANCE_SONIFICATION
                )
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build()
        }else
        {
            SoundPool(1,AudioManager.STREAM_MUSIC,0)
        }

        //Загружаем клик из папки роу
        val click = soundPool!!.load(this, R.raw.clic,1)

        //Поворот экрана по горизонтали
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        //Инициализируем хэлпер для sql
        val db = DbHelper(this,null)

        //костыли чтобы не читать треки напрямую из sql
        val songmap = mutableMapOf<String,String>() //список с бипиэмами
        val viewsong = mutableListOf<String>() //Список песен

        //костыль чтобы понимать какой трек удалять
        var now_playing:String = ""

        //костыль чтобы останавливать while
        var startstop: Boolean = true

        //Корутина для асинхронного запуска клика
        val viewModelJob = Job()
        val uiScope = CoroutineScope(Main + viewModelJob)

        //адаптер для списка
        val songlistadapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,viewsong)
        bind.viewSong.adapter = songlistadapter

        //прокидываем из БД таблицу в мап для вью
            db.getSong().forEach { (i, j) ->
            songmap[i] = j
            viewsong.add(i)
            songlistadapter.notifyDataSetChanged()
        }


        //выбор трека
        bind.viewSong.setOnItemClickListener { parent, view, position, id ->
            bind.inputBpm.setText(songmap.get(songlistadapter.getItem(position).toString()).toString())
            now_playing = parent.getItemAtPosition(position).toString() //текущее название трека
            bind.seekBar.setProgress(songmap.get(now_playing)!!.toInt())


        }

        //листенер сикбара
        bind.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                bind.inputBpm.setText(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }


        })

        //добавление трека в список
        bind.addBut.setOnClickListener {
            if (viewsong.indexOf(bind.inputSong.text.toString()) >= 0){
                //Алерт при добавлении трека с одинаковым названием трека.
                val addalert = AlertDialog.Builder(this@MainActivity)
                addalert.setTitle("Can't do that")
                addalert.setMessage("Need new name of song")
                addalert.setPositiveButton("OK"){_,_ ->

                }

                addalert.show()
            }
            else{
                songmap.put(bind.inputSong.text.toString(),bind.inputBpm.text.toString()) //добавили название_трека : БПМ
                viewsong.add(bind.inputSong.text.toString()) // добавили название трека для списка фронта
                songlistadapter.notifyDataSetChanged()

                //добавляем в БД
                val value = Song(bind.inputSong.text.toString(), bind.inputBpm.text.toString())
                db.addSong(value)

            }

        }

        //Удаление трека
        bind.delBut.setOnClickListener {
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

        //Запуск клика
        bind.playBut.setOnClickListener {
            val bpm:Int= 60000/bind.inputBpm.text.toString().toInt()
            //playClick()
            startstop = true
            uiScope.launch {
                while(startstop) {
                    soundPool!!.play(click,1f,1f,0,0,1f)
                    delay(bpm.toLong())
                }
            }
        }

        //Остановка клика
        bind.stopBut.setOnClickListener {
            startstop = false

        }

        //+1 клик
        bind.onePlus.setOnClickListener{
            var counter: Int = bind.inputBpm.text.toString().toInt()
            counter++
            bind.inputBpm.setText(counter.toString())
            bind.seekBar.setProgress(counter)
        }

        //-1 клик
        bind.oneMinus.setOnClickListener{
            var counter: Int = bind.inputBpm.text.toString().toInt()
            counter--
            bind.inputBpm.setText(counter.toString())
            bind.seekBar.setProgress(counter)
        }
    }


    override fun onStop() {
        super.onStop()
    }
}
