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
            val goback = Intent(this,MainActivity::class.java)
            startActivity(goback)
        }



    }
}