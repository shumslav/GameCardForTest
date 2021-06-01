package com.shumslav.cardgamefortest.Data.Models

class Score {
    private var steps = "0"
    private var time = "0:0"
    private var dificult = "10"



    constructor(steps: String, time: String, dificult: String) {
        this.steps = steps
        this.time = time
        this.dificult = dificult
    }

    constructor()

    fun getSteps():String{
        return this.steps
    }

    fun getTime():String{
        return this.time
    }

    fun getDificult():String{
        return this.dificult
    }

}