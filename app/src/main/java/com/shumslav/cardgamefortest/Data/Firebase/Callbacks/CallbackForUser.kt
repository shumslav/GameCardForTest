package com.shumslav.cardgamefortest.Data.Firebase.Callbacks

import com.shumslav.cardgamefortest.Data.Models.User

interface CallbackForUser {
    fun onCallback(list: MutableList<User?>){

    }
}
