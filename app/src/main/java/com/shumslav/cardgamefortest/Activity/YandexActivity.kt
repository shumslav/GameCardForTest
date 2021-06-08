package com.shumslav.cardgamefortest.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.shumslav.cardgamefortest.Data.Models.PersonalUrl
import com.shumslav.cardgamefortest.Data.SQLite.SQLiteHelper
import com.shumslav.cardgamefortest.R
import com.shumslav.cardgamefortest.defaults
import com.shumslav.cardgamefortest.insertBetween
import com.shumslav.cardgamefortest.makeToast
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
    private lateinit var personalUrl: PersonalUrl
    private lateinit var context: Context

    private var key = defaults.get("key")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yandex)

        sqLiteHelper = SQLiteHelper(this)
        personalUrl = sqLiteHelper.getPersonalUrl()
        clickValue = personalUrl.getUclick()
        personalJsonUrl = personalUrl.getUclickUrl()
        if (!clickValue.isEmpty() && !personalJsonUrl.isEmpty()){
            startActivity(Intent(this,MainActivity::class.java))
        }

        context = this
        webView = findViewById(R.id.web_view_yandex)
        key = intent.getStringExtra("key")
        webView.loadUrl(key!!)
        remoteConfig = FirebaseRemoteConfig.getInstance()
        cookieManager = CookieManager.getInstance()


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
                Log.i("Cookie", key!!)
                val cookies = cookieManager.getCookie(key)
                Log.i("Cookie", cookies)
                for (cookie in cookies.split(";")) {
                    if (cookie.contains("uclick=")) {
                        clickValue = cookie.split("=")[1]
                        personalJsonUrl = insertBetween(remoteConfig.getString("url"), clickValue, "*****", "***")
                        break
                    }
                }
                sqLiteHelper.insertPersonalUrl(PersonalUrl(clickValue, personalJsonUrl))
                Log.i("JsonFileUrl", personalJsonUrl)
                startActivity(Intent(context,MainActivity::class.java))
            }
        }
    }
}