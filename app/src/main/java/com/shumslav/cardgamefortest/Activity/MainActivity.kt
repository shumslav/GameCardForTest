package com.shumslav.cardgamefortest.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.shumslav.cardgamefortest.R

class MainActivity : AppCompatActivity() {
    private lateinit var buttonGame: Button
    private lateinit var buttonSettings: Button
    private lateinit var buttonScore: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonGame = findViewById(R.id.button_game)
        buttonSettings = findViewById(R.id.button_settings)
        buttonScore = findViewById(R.id.button_score)

        buttonGame.setOnClickListener {
            startActivity(Intent(this,GameActivity::class.java))
        }

        buttonSettings.setOnClickListener {
            startActivity(Intent(this,SettingsActivity::class.java))
        }
    }
}