package com.shumslav.cardgamefortest.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.shumslav.cardgamefortest.Data.Models.SettingsApp
import com.shumslav.cardgamefortest.Data.SQLite.SQLiteHelper
import com.shumslav.cardgamefortest.R

class WinActivity : Activity() {

    private val sqlHelper = SQLiteHelper(this)
    private var score = 0
    private var time = 0

    lateinit var settings:SettingsApp
    lateinit var dificultText:TextView
    lateinit var scoreText:TextView
    lateinit var timeText:TextView
    lateinit var toMainButton:Button
    lateinit var mFirebaseAnalitics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)

        settings = sqlHelper.getSettings()
        dificultText = findViewById(R.id.dificult_text)
        scoreText= findViewById(R.id.score)
        timeText = findViewById(R.id.time)
        toMainButton = findViewById(R.id.button_to_main)
        mFirebaseAnalitics = FirebaseAnalytics.getInstance(this)

        var bundle = Bundle()
        bundle.putInt("key", 69)
        mFirebaseAnalitics.logEvent("name",bundle)



        toMainButton.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        score = intent.getIntExtra("score", 0)
        time = intent.getIntExtra("time", 0)
        scoreText.text = "Шагов: ${score}"
        when(settings.getDificult().toInt()){
            5 -> dificultText.text = "Уровень сложности:\nЛегкий"
            10 -> dificultText.text = "Уровень сложности:\nСредний"
            15 -> dificultText.text = "Уровень сложности:\nСложный"
        }
        timeText.text = ("Время: ${time/60}:${time%60}")

    }
}