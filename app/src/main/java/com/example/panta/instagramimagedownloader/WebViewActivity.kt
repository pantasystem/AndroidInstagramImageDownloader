package com.example.panta.instagramimagedownloader

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewActivity : AppCompatActivity() {

    lateinit var webView:WebView
    lateinit var url:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val intent = intent
        url = intent.getStringExtra("url")

        webView = findViewById(R.id.webView)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onStart(){
        super.onStart()
        webView.webViewClient = MyWebClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = true

        webView.visibility = View.VISIBLE
        webView.loadUrl(url)
        webView.addJavascriptInterface(MyJavaScriptInterface(applicationContext),"ImageGetter")
    }

    inner class MyWebClient: WebViewClient(){
        //private val jsTest = "javascript:Kotlin.callMe(\"呼び出したよ\");"
        private val jsTest = "javascript:" +
                "var text = \"\";\n" +
                "var imgList = document.getElementsByTagName(\"img\");\n" +
                "for(var i = 0; i < imgList.length; i++){" +
                "   text += imgList[i].getAttribute(\"src\") + \",\"" +
                "}" +

                "ImageGetter.callMe(text)"
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
           
            view?.loadUrl(jsTest)
        }
    }

    inner class MyJavaScriptInterface(context: Context){
        @JavascriptInterface
        fun callMe(message: String){
            val list = message.split(",")
            /*val urlList = list.map{
                URL(it)
            }*/
            Log.d("list",list.toString())
            val serializableList = ListBean(list)
            val intent = Intent(applicationContext, ImageSelectorActivity::class.java)
            intent.putExtra("listBean", serializableList)
            startActivity(intent)
            /*runOnUiThread{
                //webView.loadUrl(message)
                webView.stopLoading()
            }*/
            finish()
        }
    }
}
