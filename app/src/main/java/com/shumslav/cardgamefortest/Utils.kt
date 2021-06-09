package com.shumslav.cardgamefortest

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import kotlin.concurrent.thread
import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.appsflyer.AppsFlyerLib
import com.shumslav.cardgamefortest.Data.Models.PersonalUrl
import com.shumslav.cardgamefortest.Data.SQLite.SQLiteHelper

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
    "key" to "",
    "url" to ""
)

val af_complete_registration = listOf("registration", "reg", "new", "lead")
val af_purchase = listOf("sale", "approved")


fun makeToast(context: Context, string: String) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
}

fun insertBetween(string: String, inputString: String, from: String, to: String): String {
    val partsString = string.split(from)
    var result = partsString.first() + from + inputString + to + partsString.last().split(to).last()
    return result
}

fun checkStatus(context: Context, personalUrl: PersonalUrl): Boolean {
    var result = false
    val sqLiteHelper = SQLiteHelper(context)
    var statusPurchase = personalUrl.getStatusPurchase()
    var statusReg = personalUrl.getStatusReg()
    thread {
        val doc = Jsoup.connect(personalUrl.getUclickUrl()).get()
        val jsonData = doc.outerHtml().split("<body>")[1].split("</body>")[0].trim()
        if (JSONArray(jsonData).length() != 0) {
            val jsonObj = JSONArray(jsonData)[0] as JSONObject
            val map = jsonObj.toMap()
            val status = map.get("status") as String
            if (status in af_purchase) {
                if (!statusPurchase.toBoolean()) {
                    val eventValue: MutableMap<String, Any> = mutableMapOf()
                    eventValue.put(AFInAppEventParameterName.PRICE, 350)
                    eventValue.put(AFInAppEventParameterName.CONTENT_ID, "221")
// for multiple product categories, set the param value as: // new String {"221", "124"}
                    eventValue.put(AFInAppEventParameterName.CONTENT_TYPE, "shirt")
// for multiple product categories,, set the param value as: new String {"shoes", "pants"}
                    eventValue.put(AFInAppEventParameterName.CURRENCY, "USD")
                    eventValue.put(AFInAppEventParameterName.QUANTITY, 2)
// for multiple product categories, set the param value as: new int {2, 5}
                    eventValue.put(AFInAppEventParameterName.RECEIPT_ID, "X123ABC")
                    eventValue.put("af_order_id", "X123ABC")
                    AppsFlyerLib.getInstance()
                        .logEvent(context, AFInAppEventType.PURCHASE, eventValue)
                    statusPurchase = "true"
                    result = true
                    Log.d("Status_purchase", "Find")
                }
            }
            if (status in af_complete_registration) {
                if (!statusReg.toBoolean()) {
                    val eventValue: MutableMap<String, Any> = mutableMapOf()
                    eventValue.put(AFInAppEventParameterName.REGSITRATION_METHOD, "Registration");
                    AppsFlyerLib.getInstance()
                        .logEvent(context, AFInAppEventType.COMPLETE_REGISTRATION, eventValue)
                    statusReg = "true"
                    result = true
                    Log.d("Status_reg", "Find")
                }
            }
            if (result) {
                val newPersonalUrl = PersonalUrl(
                    personalUrl.getUclick(),
                    personalUrl.getUclickUrl(),
                    statusReg,
                    statusPurchase
                )
                sqLiteHelper.insertPersonalUrl(newPersonalUrl)
            }
        }
    }.join()
    return result
}

fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
    when (val value = this[it]) {
        is JSONArray -> {
            val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
            JSONObject(map).toMap().values.toList()
        }
        is JSONObject -> value.toMap()
        JSONObject.NULL -> null
        else -> value
    }
}