package com.sigufyndufi.finfangam.Activity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.sigufyndufi.finfangam.*
import com.sigufyndufi.finfangam.Data.Models.PersonalUrl
import com.sigufyndufi.finfangam.Data.SQLite.SQLiteHelper
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

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

    val INPUT_FILE_REQUEST_CODE = 1
    private var key = ""
    private var mFilePathCallback: ValueCallback<Array<Uri?>>? = null
    private var mCameraPhotoPath: String? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yandex)

        context = this
        webView = findViewById(R.id.web_view_yandex)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        key = intent.getStringExtra("key")!!
        webView.settings.apply {
            this.javaScriptEnabled = true
            this.domStorageEnabled = true
            this.databaseEnabled = true
            this.allowFileAccess = true
            this.allowFileAccessFromFileURLs = true
        }
        remoteConfig = FirebaseRemoteConfig.getInstance()
        cookieManager = CookieManager.getInstance()
        sqLiteHelper = SQLiteHelper(this)
        personalUrl = sqLiteHelper.getPersonalUrl()
        timer = object : CountDownTimer(time_to_check_status, 1000) {
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

        webView.setDownloadListener { url: String, userAgent: String,
                                      contentDisposition: String, mimetype: String,
                                      contentLength: Long ->
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) //Notify client once download is completed!
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mimetype)
            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, ".png")
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
            Toast.makeText(applicationContext, "Downloading File", Toast.LENGTH_LONG).show()
        }



        webView.loadUrl(key)
        webView.webViewClient = object : WebViewClient() {
            var progressDialog: ProgressDialog? = null
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
                    try {
                        // Close progressDialog
                        if (progressDialog!!.isShowing()) {
                            progressDialog!!.dismiss()
                            progressDialog = null
                        }
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
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
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                mFilePathCallback?.onReceiveValue(null)
                mFilePathCallback = filePathCallback
                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent!!.resolveActivity(packageManager) != null) {
                    // Create the File where the photo should go
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        Log.e("File", "Unable to create Image File", ex)
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.absolutePath
                        takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile)
                        )
                    } else {
                        takePictureIntent = null
                    }
                }
                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.type = "image/*"
                val intentArray: Array<Intent?> = takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
                return true
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, intent)
            return
        }

        var results: Array<Uri?>? = null
        // Check that the response is a good one
        if (resultCode == RESULT_OK) {
            if (intent == null) {
                // If there is not data, then we may have taken a photo
                if (mCameraPhotoPath != null) {
                    results = arrayOf(Uri.parse(mCameraPhotoPath))
                }
            } else {
                val dataString: String? = intent.dataString
                if (dataString != null) {
                    results = arrayOf(Uri.parse(dataString))
                }
            }
        }

        mFilePathCallback!!.onReceiveValue(results)
        mFilePathCallback = null
        return
    }

    override fun onBackPressed() {
        when (webView.canGoBack()) {
            true -> webView.goBack()
            false -> {
                val spaceBuilder = AlertDialog.Builder(this)
                spaceBuilder.setTitle("Confirm")
                    .setMessage("Do you want exit this app")
                    .setPositiveButton("Yep") { _, _ ->
                        finishAffinity()
                        exitProcess(0)
                    }
                    .setNegativeButton("Nop") { _, _ ->
                    }
                val publicDialog: AlertDialog = spaceBuilder.create()
                publicDialog.show()
            }
        }
    }
}