package com.shumslav.cardgamefortest.Data.Models

class SettingsApp{
    var dificult = "10"
    var volumeLevel = "100"

    constructor()

    constructor(dificult: String, volumeLevel: String) {
        this.dificult = dificult
        this.volumeLevel = volumeLevel
    }


}