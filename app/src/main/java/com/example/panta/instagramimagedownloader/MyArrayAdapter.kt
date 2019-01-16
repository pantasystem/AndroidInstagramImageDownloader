package com.example.panta.instagramimagedownloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import java.net.URL
import com.example.panta.instagramimagedownloader.MyArrayAdapter.ViewHolder
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

import javax.net.ssl.HttpsURLConnection


class MyArrayAdapter(private val context: Context, private val itemLayoutId:Int, private val imageUrlList: List<String>) : BaseAdapter() {

    data class ViewHolder(var image: ImageView)
    private val imgDownloader = ImageDownloader()

    private var inflater: LayoutInflater = LayoutInflater.from(context)
    private val layoutId = itemLayoutId

    override fun getCount(): Int {
        return imageUrlList.size
    }

    override fun getItem(position: Int): Any {
        return imageUrlList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()

    }

    override fun getView(position: Int,convertView: View?, parent: ViewGroup?): View {
         var convertViewTmp: View? = convertView

        val holder: ViewHolder


        if (convertView == null) {
            convertViewTmp = inflater.inflate(layoutId, null)
            holder = ViewHolder(convertViewTmp!!.findViewById(R.id.img_item))
            convertViewTmp.tag = holder
        } else {
            holder = convertViewTmp!!.tag as ViewHolder
        }

        //val handler = Handler()
        GlobalScope.launch{
            try{
                val asyncImage = imgDownloader.asyncDownloadBitmap(imageUrlList[position])
                val image = asyncImage.await()
                val resizedBitmap = controlBitmapSize(image)

                holder.image.maxWidth = resizedBitmap.width
                holder.image.maxHeight = resizedBitmap.height

                holder.image.setImageBitmap(resizedBitmap)


            }catch(e: Exception){
                e.printStackTrace()
            }

        }
        //holder.image.setImageBitmap()
        return convertViewTmp
    }

    private fun controlBitmapSize(bitmap: Bitmap): Bitmap{
        val ratio:Double = 300.0 / bitmap.width
        Log.d("Width", bitmap.width.toString())
        Log.d("Height",bitmap.height.toString())
        val width = (bitmap.width * ratio).toInt()
        val height = (bitmap.height * ratio).toInt()
        Log.d("NewWidth", width.toString())
        return Bitmap.createScaledBitmap(bitmap, width,height,true)
    }
}