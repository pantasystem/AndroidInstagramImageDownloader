package com.example.panta.instagramimagedownloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.support.annotation.RequiresApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.net.ssl.HttpsURLConnection

class ImageDownloader{
    fun asyncDownloadBitmap(url : String) = GlobalScope.async {
        val imgUrl = URL(url)

        val connection =
            if(url.startsWith("http://")){
                imgUrl.openConnection() as HttpURLConnection
                //url.openConnection() as HttpsURLConnection
            }else{
                imgUrl.openConnection() as HttpsURLConnection
            }

        try{
            //val connection = imgUrl.openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            val stream = connection.inputStream
            return@async BitmapFactory.decodeStream(stream)
        }catch(e: Exception){
            e.printStackTrace()
            throw Exception()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveImage(url: String){
        if(url.isBlank()){

        }else{
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
                bitmapImg.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            }
        }
    }

    private fun makeFileName(): String{
        val c = Calendar.getInstance()
        return "${c[Calendar.YEAR]}_${c[Calendar.MONTH] + 1}_${c[Calendar.DAY_OF_MONTH]}_${c[Calendar.HOUR_OF_DAY]}_${c[Calendar.MINUTE]}${c[Calendar.SECOND]}_${c[Calendar.MILLISECOND]}.jpg"
    }
}