package com.shumslav.cardgamefortest

import android.content.Context
import android.widget.Toast

val listOfImages = mutableListOf(
    R.drawable.airplane,
    R.drawable.ambrella,
    R.drawable.bank,
    R.drawable.bug,
    R.drawable.bus,
    R.drawable.cake,
    R.drawable.compas,
    R.drawable.cup,
    R.drawable.fire,
    R.drawable.flag,
    R.drawable.flower,
    R.drawable.moon,
    R.drawable.tractor,
    R.drawable.anchor,
    R.drawable.hand
)

val defaults = mapOf(
    "key" to ""
)

fun makeToast(context: Context, string: String) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
}