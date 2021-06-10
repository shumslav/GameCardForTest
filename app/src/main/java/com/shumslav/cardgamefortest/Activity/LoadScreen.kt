package com.sigufyndufi.finfangam.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.sigufyndufi.finfangam.*
import com.sigufyndufi.finfangam.Data.Models.PersonalUrl
import com.sigufyndufi.finfangam.Data.SQLite.SQLiteHelper
import java.nio.charset.StandardCharsets


class LoadScreen : AppCompatActivity() {

    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_screen)

        remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.reset()
        remoteConfig.setDefaultsAsync(defaults)

        remoteConfig.fetchAndActivate().addOnSuccessListener {
            val intent = Intent(this, YandexActivity::class.java)
            val result = remoteConfig.getString("key")
            Log.i("key", result)
            if (result.isNotEmpty()) {
                Log.i("Base64", result)
                val data = Base64.decode(result, Base64.DEFAULT)
                val text = String(data, StandardCharsets.UTF_8)
                intent.putExtra("key", text)
                Log.i("UrlFromRemote", text)
                startActivity(intent)
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }.addOnFailureListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}