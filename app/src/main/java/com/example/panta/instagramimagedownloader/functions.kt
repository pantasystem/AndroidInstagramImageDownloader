package com.example.panta.instagramimagedownloader

import android.os.Handler

fun uiThread(func: ()-> Unit){
    val handler = Handler()
    handler.post{
        func
    }
}