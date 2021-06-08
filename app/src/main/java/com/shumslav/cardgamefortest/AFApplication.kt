package com.shumslav.cardgamefortest

import android.app.Application
import android.util.Log
import com.appsflyer.AppsFlyerLib
import com.appsflyer.AppsFlyerConversionListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class AFApplication: Application() {
    companion object {
        private val AF_DEV_KEY = "euG25HRdy99WGJ4DJuejom"
        private val LOG_TAG = "TAG"
    }

    override fun onCreate() {
        super.onCreate()
        val conversionListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                data?.let { cvData ->
                    cvData.map {
                        Log.i(LOG_TAG, "conversion_attribute:  ${it.key} = ${it.value as String}")
                    }
                }
            }

            override fun onConversionDataFail(p0: String?) {
                TODO("Not yet implemented")
            }

            override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                TODO("Not yet implemented")
            }

            override fun onAttributionFailure(p0: String?) {
                TODO("Not yet implemented")
            }
        }
        AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionListener, this)
        AppsFlyerLib.getInstance().start(this)
    }
}