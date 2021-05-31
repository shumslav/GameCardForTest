package com.shumslav.cardgamefortest.Data.Firebase.Callbacks

import com.shumslav.cardgamefortest.Data.Models.SettingsApp

interface CallbackForSettings {
    fun onCallback(list: MutableList<SettingsApp?>){

    }
}
