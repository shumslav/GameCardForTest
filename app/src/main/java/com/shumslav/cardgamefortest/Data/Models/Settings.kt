package com.sigufyndufi.finfangam.Data.Models

class SettingsApp{
    private var dificult = "10"
    private var volumeLevel = "100"

    constructor()

    constructor(dificult: String, volumeLevel: String) {
        this.dificult = dificult
        this.volumeLevel = volumeLevel
    }

    fun getDificult():String{
        return this.dificult
    }

    fun setDificult(dificult: String){
        this.dificult = dificult
    }

    fun setVolumeLevel(volumeLevel: String){
        this.volumeLevel = volumeLevel
    }

    fun getVolumeLevel():String{
        return this.volumeLevel
    }


}