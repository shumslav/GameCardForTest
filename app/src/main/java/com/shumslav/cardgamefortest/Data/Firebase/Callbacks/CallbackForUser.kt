package com.sigufyndufi.finfangam.Data.Firebase.Callbacks

import com.sigufyndufi.finfangam.Data.Models.User

interface CallbackForUser {
    fun onCallback(list: MutableList<User?>){

    }
}
