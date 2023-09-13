package com.example.bippippippip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bippippippip.databinding.SettingsBinding

class Settings : AppCompatActivity() {
    lateinit var bindset : SettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindset = SettingsBinding.inflate(layoutInflater)
        setContentView(bindset.root)




        val selectWeakId = bindset.WeakGroup.checkedRadioButtonId
        val weak: Int = when(selectWeakId){
            R.id.weak_1 -> 1
            R.id.weak_2 -> 2
            R.id.weak_3 -> 3
            else -> {1}
        }


        val selectStrongId = bindset.StrongGroup.checkedRadioButtonId
        val strong: Int = when(selectStrongId){
            R.id.strong_1 -> 1
            R.id.strong_2 -> 2
            R.id.strong_3 -> 3
            else -> {1}
        }

        //переход обратно на главную
        bindset.goBack.setOnClickListener {
            val goback = Intent(this,MainActivity::class.java).putExtra("weak", weak)
            startActivity(goback)
            intent.putExtra("weak", weak)
            intent.putExtra("strong",strong)

        }
    }
}