package com.shumslav.cardgamefortest.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Chronometer
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.shumslav.cardgamefortest.*
import com.shumslav.cardgamefortest.Data.Models.PersonalUrl
import com.shumslav.cardgamefortest.Data.SQLite.SQLiteHelper
import kotlinx.coroutines.GlobalScope
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.log

class YandexActivity : AppCompatActivity() {

    companion object {
        var clickValue = ""
        var personalJsonUrl = ""
    }

    private lateinit var webView: WebView
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var cookieManager: CookieManager
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var context: Context
    private lateinit var personalUrl: PersonalUrl
    private lateinit var timer: CountDownTimer

    private var key = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yandex)

        context = this
        webView = findViewById(R.id.web_view_yandex)
        key = intent.getStringExtra("key")!!
        webView.loadUrl(key)
        remoteConfig = FirebaseRemoteConfig.getInstance()
        cookieManager = CookieManager.getInstance()
        sqLiteHelper = SQLiteHelper(this)
        personalUrl = sqLiteHelper.getPersonalUrl()
        timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                Log.i("Check", "StartCheck")
                Log.i("StatusReg", personalUrl.getStatusReg())
                Log.i("StatusPurchase", personalUrl.getStatusPurchase())
                if (!checkStatus(context, personalUrl)) {
                    timer.start()
                    Log.d("Status", "NothingFind")
                } else {
                    personalUrl = sqLiteHelper.getPersonalUrl()
                    if (!personalUrl.getStatusPurchase().toBoolean())
                        timer.start()
                    else
                        timer.cancel()
                }
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                cookieManager.flush()
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                cookieManager.flush()
                if (personalUrl.getUclick().isEmpty() || personalUrl.getUclickUrl().isEmpty()) {
                    val cookies = cookieManager.getCookie(key)
                    Log.i("Cookie", cookies)
                    for (cookie in cookies.split(";")) {
                        if (cookie.contains("uclick=")) {
                            clickValue = cookie.split("=")[1]
                            personalJsonUrl = insertBetween(
                                remoteConfig.getString("url"),
                                clickValue,
                                "*****",
                                "***"
                            )
                            break
                        }
                    }
                    sqLiteHelper.insertPersonalUrl(
                        PersonalUrl(
                            clickValue,
                            personalJsonUrl,
                            "false",
                            "false"
                        )
                    )
                    personalUrl = PersonalUrl(clickValue, personalJsonUrl, "false", "false")
                }
                Log.d("Uclick", personalUrl.getUclick())
                Log.d("UclickURL", personalUrl.getUclickUrl())
                if (!personalUrl.getStatusPurchase().toBoolean()) {
                    if (!checkStatus(context, personalUrl)) {
                        Log.d("Status", "NothingFind")
                        timer.start()
                    } else {
                        personalUrl = sqLiteHelper.getPersonalUrl()
                        if (!personalUrl.getStatusPurchase().toBoolean())
                            timer.start()
                    }
                } else {
                    Log.d("Status_purchase", "Find")
                }
            }
        }
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }
}