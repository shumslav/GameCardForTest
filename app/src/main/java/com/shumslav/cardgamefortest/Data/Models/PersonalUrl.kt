package com.shumslav.cardgamefortest.Data.Models

class PersonalUrl(private val uclick:String, private val uclickUrl:String,){
    fun getUclick():String{
        return this.uclick
    }
    fun getUclickUrl():String{
        return this.uclickUrl
    }
}