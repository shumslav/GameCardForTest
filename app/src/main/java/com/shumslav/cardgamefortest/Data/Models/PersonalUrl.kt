package com.shumslav.cardgamefortest.Data.Models

class PersonalUrl(private val uclick:String, private val uclickUrl:String,
                  private val status_reg:String, private val status_purchase: String){

    fun getUclick():String{
        return this.uclick
    }

    fun getUclickUrl():String{
        return this.uclickUrl
    }

    fun getStatusReg():String{
        return this.status_reg
    }

    fun getStatusPurchase():String{
        return this.status_purchase
    }
}