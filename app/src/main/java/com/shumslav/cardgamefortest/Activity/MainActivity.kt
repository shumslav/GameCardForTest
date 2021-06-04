package com.shumslav.cardgamefortest.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.shumslav.cardgamefortest.R
import com.shumslav.cardgamefortest.makeToast

class MainActivity : Activity() {
    private lateinit var buttonGame: Button
    private lateinit var buttonSettings: Button
    private lateinit var buttonScore: Button
    private lateinit var buttonYouTube: Button
    private lateinit var buttonYandex: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonGame = findViewById(R.id.button_game)
        buttonSettings = findViewById(R.id.button_settings)
        buttonScore = findViewById(R.id.button_score)
        buttonYouTube = findViewById(R.id.button_web)
        buttonYandex = findViewById(R.id.button_web_yandex)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                makeToast(this, "can't take")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            makeToast(this,token!!)
        })


        buttonGame.setOnClickListener {
            startActivity(Intent(this,GameActivity::class.java))
        }

        buttonSettings.setOnClickListener {
            startActivity(Intent(this,SettingsActivity::class.java))
        }

        buttonScore.setOnClickListener {
            startActivity(Intent(this, ScoreActivity::class.java))
        }

        buttonYouTube.setOnClickListener {
            startActivity(Intent(this, YouTubeActivity::class.java))
        }

        buttonYandex.setOnClickListener {
            startActivity(Intent(this, YandexActivity::class.java))

        }
    }
}
