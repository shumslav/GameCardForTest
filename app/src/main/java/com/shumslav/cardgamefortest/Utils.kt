package com.shumslav.cardgamefortest

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import kotlin.concurrent.thread

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

fun insertBetween(string: String, inputString: String, from:String,to:String):String{
    val partsString = string.split(from)
    var result = partsString.first()+from+inputString+to+partsString.last().split(to).last()
    return result
}

fun checkStatus(url:String):Boolean{
    var result = false
    thread {
        val doc = Jsoup.connect(url).get()
        val jsonData = doc.outerHtml().split("<body>")[1].split("</body>")[0].trim()
        val jsonObj = JSONArray(jsonData)[0] as JSONObject
        val map = jsonObj.toMap()
        val status = map.get("status") as String
        if (status=="qwe")
            result = true
    }.join()
    return result
}

fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
    when (val value = this[it])
    {
        is JSONArray ->
        {
            val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
            JSONObject(map).toMap().values.toList()
        }
        is JSONObject -> value.toMap()
        JSONObject.NULL -> null
        else            -> value
    }
}