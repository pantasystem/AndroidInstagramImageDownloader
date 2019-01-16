package com.example.panta.instagramimagedownloader

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    lateinit var webView:WebView
    lateinit var urlBox: EditText
    lateinit var getImgButton:Button
    lateinit var deleteBoxButton: Button
    lateinit var pasteButton: Button
    lateinit var inputLayout:LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        urlBox = findViewById(R.id.instagramUrl)
        getImgButton = findViewById(R.id.getImageButton)
        deleteBoxButton = findViewById(R.id.deleteURLButton)
        pasteButton = findViewById(R.id.pasteButton)

        inputLayout = findViewById(R.id.inputLayout)


    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun onStart(){
        super.onStart()


        getImgButton.setOnClickListener{
            val intent = Intent(applicationContext, WebViewActivity::class.java)
            intent.putExtra("url", urlBox.text.toString())
            startActivity(intent)

        }

        deleteBoxButton.setOnClickListener{
            urlBox.setText("")
        }

        pasteButton.setOnClickListener{
            val cm: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val cd: ClipData? = cm.primaryClip
            if(cd != null){
                val item = cd.getItemAt(0)
                urlBox.setText(item.text)
            }
        }
    }
}

/*
var a = "";
var d = document.links;
for(var i = 0; i < d.length; i++){
    a += d[i];
}
alert(a);
 */