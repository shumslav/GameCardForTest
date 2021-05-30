package com.shumslav.cardgamefortest.Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.shumslav.cardgamefortest.Data.Models.SettingsApp
import com.shumslav.cardgamefortest.Data.SQLite.SQLiteHelper
import com.shumslav.cardgamefortest.R

class WinActivity : AppCompatActivity() {

    private val sqlHelper = SQLiteHelper(this)
    private var score = 0

    lateinit var settings:SettingsApp
    lateinit var dificultText:TextView
    lateinit var scoreText:TextView
    lateinit var name:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)

        settings = sqlHelper.getSettings()
        dificultText = findViewById(R.id.dificult_text)
        scoreText= findViewById(R.id.score)
        name = findViewById(R.id.name)
        score = intent.getIntExtra("com.shumslav.cardgamefortest.Activity.score",0)
        scoreText.text = score.toString()
        when(settings.getDificult()){
            5 -> dificultText.text = "Уровень сложности:\nЛегкий"
            10 -> dificultText.text = "Уровень сложности:\nСредний"
            15 -> dificultText.text = "Уровень сложности:\nСложный"
        }

    }
}