package com.shumslav.cardgamefortest.Data.Models

class User {
    private var login = ""
    private var password = ""
    private var settingsApp:SettingsApp? = null

    constructor(login: String, password: String) {
        this.login = login
        this.password = password
    }

    fun getLogin():String{
        return this.login
    }

    fun getPassword():String{
        return this.password
    }
}