package com.example.panta.instagramimagedownloader

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.net.ssl.HttpsURLConnection

class ImageDownloader(private val context: Context){

    @SuppressLint("ShowToast")
    fun asyncDownloadBitmap(url : String) = GlobalScope.async {

        try{
            val imgUrl = URL(url)
            val connection =
                if(url.startsWith("http://")){
                    imgUrl.openConnection() as HttpURLConnection
                    //url.openConnection() as HttpsURLConnection
                }else{
                    imgUrl.openConnection() as HttpsURLConnection
                }
            //val connection = imgUrl.openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            val stream = connection.inputStream
            return@async BitmapFactory.decodeStream(stream)
        }catch(e: MalformedURLException){
            uiThread{
                Toast.makeText(context, "URLが空です",Toast.LENGTH_LONG).show()
            }


            null
        }

    }

    @SuppressLint("ShowToast")
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveImage(url: String){

        Log.d("URL", url)

        if(url.isBlank()){
            //urlが空でないかを判定
            uiThread{
                Toast.makeText(context, "画像ではありません", Toast.LENGTH_LONG).show()
            }
            return
        }
        val extDir = Environment.getExternalStorageDirectory()
        val basePath = "${extDir.absolutePath}/${Environment.DIRECTORY_DCIM}"
        val apkFilePath = "$basePath/ImageDownloader"
        if(Files.notExists(Paths.get(apkFilePath))){
            val newDir = File(apkFilePath)
            newDir.mkdir()
        }
        val file = File("$apkFilePath/${makeFileName()}")

        val outStream = FileOutputStream(file)

        val deferredImg = asyncDownloadBitmap(url)
        GlobalScope.launch{
            val bitmapImg = deferredImg.await()

            //Bitmapが画像であるかを判定
            if(bitmapImg == null){
                uiThread{
                    Toast.makeText(context, "", Toast.LENGTH_LONG)
                }
            }else{
                bitmapImg.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            }
        }
    }

    //時間にも基づいてファイル名を作成します。
    private fun makeFileName(): String{
        val c = Calendar.getInstance()
        return "${c[Calendar.YEAR]}_${c[Calendar.MONTH] + 1}_${c[Calendar.DAY_OF_MONTH]}_${c[Calendar.HOUR_OF_DAY]}_${c[Calendar.MINUTE]}${c[Calendar.SECOND]}_${c[Calendar.MILLISECOND]}.jpg"
    }
}