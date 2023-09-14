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






        //переход обратно на главную
        bindset.goBack.setOnClickListener {

            val selectWeakId = bindset.WeakGroup.checkedRadioButtonId
            val weak: Int = when(selectWeakId){
                bindset.weak1.id -> 1
                bindset.weak2.id -> 2
                bindset.weak3.id -> 3
                else -> {2}
            }


            val selectStrongId = bindset.StrongGroup.checkedRadioButtonId
            val strong: Int = when(selectStrongId){
                bindset.strong1.id -> 1
                bindset.strong2.id -> 2
                bindset.strong3.id -> 3
                else -> {2}
            }

            val goback = Intent(this,MainActivity::class.java)
            goback.putExtra("weak", weak)
            goback.putExtra("strong",strong)
            startActivity(goback)


        }
    }
}