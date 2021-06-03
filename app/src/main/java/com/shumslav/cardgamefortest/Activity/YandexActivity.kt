package com.shumslav.cardgamefortest.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.shumslav.cardgamefortest.R

class YandexActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yandex)

        webView = findViewById(R.id.web_view_yandex)
        webView.loadUrl("https://yandex.ru/")
        webView.webViewClient = object: WebViewClient(){}
    }
}