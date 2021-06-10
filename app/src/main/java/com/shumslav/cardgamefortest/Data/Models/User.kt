package com.sigufyndufi.finfangam.Data.Models

class User {
    private var login = ""
    private var password = ""

    constructor(login: String, password: String) {
        this.login = login
        this.password = password
    }

    constructor()


    fun setLogin(login: String){
        this.login = login
    }

    fun getLogin():String{
        return this.login
    }

    fun setPassword(password: String){
        this.password=password
    }

    fun getPassword():String{
        return this.password
    }

}