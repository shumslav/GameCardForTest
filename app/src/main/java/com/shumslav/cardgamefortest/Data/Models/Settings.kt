package com.shumslav.cardgamefortest.Data.Models

class SettingsApp{
    private var dificult = "10"
    private var volumeLevel = "100"

    constructor()

    constructor(dificult: String, volumeLevel: String) {
        this.dificult = dificult
        this.volumeLevel = volumeLevel
    }

    fun getDificult():Int{
        return this.dificult.toInt()
    }

    fun setDificult(dificult: String){
        this.dificult = dificult
    }

    fun setVolumeLevel(volumeLevel: String){
        this.volumeLevel = volumeLevel
    }

    fun getVolumeLevel():Int{
        return this.volumeLevel.toInt()
    }


}