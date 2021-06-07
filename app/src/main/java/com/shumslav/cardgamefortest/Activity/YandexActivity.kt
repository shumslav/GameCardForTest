package com.shumslav.cardgamefortest.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.shumslav.cardgamefortest.R
import com.shumslav.cardgamefortest.defaults

class YandexActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    private var key = defaults.get("key")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yandex)

        webView = findViewById(R.id.web_view_yandex)
        key = intent.getStringExtra("key")
            webView.loadUrl(key!!)
        webView.webViewClient = object: WebViewClient(){}
    }
}