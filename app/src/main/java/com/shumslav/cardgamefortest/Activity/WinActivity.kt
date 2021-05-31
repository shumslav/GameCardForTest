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
    private var time = 0

    lateinit var settings:SettingsApp
    lateinit var dificultText:TextView
    lateinit var scoreText:TextView
    lateinit var name:EditText
    lateinit var timeText:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)

        settings = sqlHelper.getSettings()
        dificultText = findViewById(R.id.dificult_text)
        scoreText= findViewById(R.id.score)
        name = findViewById(R.id.name)
        timeText = findViewById(R.id.time)

        score = intent.getIntExtra("score", 0)
        time = intent.getIntExtra("time", 0)
        scoreText.text = "Шагов: ${score}"
        when(settings.getDificult()){
            5 -> dificultText.text = "Уровень сложности:\nЛегкий"
            10 -> dificultText.text = "Уровень сложности:\nСредний"
            15 -> dificultText.text = "Уровень сложности:\nСложный"
        }
        timeText.text = ("Время: ${time/60}:${time%60}")

    }
}