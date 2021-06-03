package com.shumslav.cardgamefortest.Activity

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.shumslav.cardgamefortest.R
import com.shumslav.cardgamefortest.makeToast

class YouTubeActivity : AppCompatActivity() {

    private val context = this
    private lateinit var webView: WebView
    private lateinit var fullScreen: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        webView = findViewById(R.id.web_view_youtube)
        fullScreen = findViewById(R.id.full_screen)

        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ")

        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view!!.loadUrl(url!!)
                return true
            }
        }

        webView.webChromeClient = object: WebChromeClient(){
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                fullScreen.addView(view)
                fullScreen.visibility = View.VISIBLE
                webView.visibility = View.GONE
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                fullScreen.removeAllViews()
                fullScreen.visibility = View.GONE
                webView.visibility = View.VISIBLE
            }
        }
    }
}