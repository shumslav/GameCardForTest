package com.sigufyndufi.finfangam

import android.app.Application
import android.util.Log
import com.appsflyer.AppsFlyerLib
import com.appsflyer.AppsFlyerConversionListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.onesignal.OneSignal

class AFApplication: Application() {
    companion object {
        private val AF_DEV_KEY = "MKdgX4DJhRU5cEhbo6mrrL"
        private val LOG_TAG = "TAG"
        private val ONESIGNAL_APP_ID = "37a57f74-f4af-4fa4-baa4-7c6d98220514"
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


        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}