package com.sigufyndufi.finfangam.Data.Models

import android.graphics.Point
import android.widget.ImageView

class Card(val image: ImageView,val point: Point) {
    var isFind = false

    fun equelsPoint(card: Card):Boolean{
        return (this.point.equals(card.point.x,card.point.y))
    }
}