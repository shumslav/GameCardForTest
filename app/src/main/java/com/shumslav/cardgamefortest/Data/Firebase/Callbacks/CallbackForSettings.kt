package com.sigufyndufi.finfangam.Data.Firebase.Callbacks

import com.sigufyndufi.finfangam.Data.Models.SettingsApp

interface CallbackForSettings {
    fun onCallback(list: MutableList<SettingsApp?>){

    }
}
