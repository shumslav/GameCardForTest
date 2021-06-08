package com.shumslav.cardgamefortest.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.shumslav.cardgamefortest.*
import com.shumslav.cardgamefortest.Data.Models.PersonalUrl
import com.shumslav.cardgamefortest.Data.SQLite.SQLiteHelper
import java.nio.charset.StandardCharsets


class LoadScreen : AppCompatActivity() {

    lateinit var remoteConfig:FirebaseRemoteConfig
    lateinit var sqLiteHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_screen)

        sqLiteHelper = SQLiteHelper(this)
        remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.reset()
        val personalUrl = sqLiteHelper.getPersonalUrl()
        remoteConfig.setDefaultsAsync(defaults)
        remoteConfig.fetchAndActivate().addOnSuccessListener {
                val intent = Intent(this, YandexActivity::class.java)
                val result = remoteConfig.getString("key")
                if (!result.isNullOrEmpty()) {
                    Log.i("Base64", result)
                    val data = Base64.decode(result, Base64.DEFAULT)
                    val text = String(data, StandardCharsets.UTF_8)
                    intent.putExtra("key", text)
                    startActivity(intent)
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
        }
    }

}