package com.shumslav.cardgamefortest.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import com.shumslav.cardgamefortest.Data.Models.SettingsApp
import com.shumslav.cardgamefortest.Data.SQLite.SQLiteHelper
import com.shumslav.cardgamefortest.R

class SettingsActivity : AppCompatActivity() {

    private val sqlHelper = SQLiteHelper(this)
    private var dificult = 10
    private var volumeLevel = 100

    private lateinit var settingsApp: SettingsApp
    private lateinit var buttonFiveCard:Button
    private lateinit var buttonTenCard:Button
    private lateinit var buttonFifteenCard:Button
    private lateinit var seekVolume:SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        buttonFiveCard = findViewById(R.id.dificult_five)
        buttonTenCard = findViewById(R.id.dificult_ten)
        buttonFifteenCard = findViewById(R.id.dificult_fifteen)
        seekVolume= findViewById(R.id.seek_bar_volume)
    }

    override fun onResume() {
        super.onResume()
        settingsApp = sqlHelper.getSettings()
        dificult = settingsApp.dificult.toInt()
        volumeLevel = settingsApp.volumeLevel.toInt()
        seekVolume.setProgress(volumeLevel)

        when(dificult){
            5 -> setColorButtons(buttonFiveCard)
            10 -> setColorButtons(buttonTenCard)
            15 -> setColorButtons(buttonFifteenCard)
        }

        buttonFiveCard.setOnClickListener {
            dificult = 5
            setColorButtons(buttonFiveCard)

        }

        buttonTenCard.setOnClickListener {
            dificult = 10
            setColorButtons(buttonTenCard)
        }

        buttonFifteenCard.setOnClickListener {
            dificult = 15
            setColorButtons(buttonFifteenCard)
        }

        seekVolume.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                volumeLevel = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onPause() {
        sqlHelper.insertSettings(SettingsApp(dificult.toString(), volumeLevel.toString()))
        super.onPause()
    }

    fun setColorButtons(without:Button){
        if (buttonFiveCard!=without){
            buttonFiveCard.setTextColor(resources.getColor(R.color.white))
            buttonFiveCard.setBackgroundColor(resources.getColor(R.color.black))
        }
        if (buttonTenCard!=without){
            buttonTenCard.setTextColor(resources.getColor(R.color.white))
            buttonTenCard.setBackgroundColor(resources.getColor(R.color.black))
        }
        if (buttonFifteenCard!=without){
            buttonFifteenCard.setTextColor(resources.getColor(R.color.white))
            buttonFifteenCard.setBackgroundColor(resources.getColor(R.color.black))
        }
        without.setTextColor(resources.getColor(R.color.black))
        without.setBackgroundColor(resources.getColor(R.color.white))
    }
}