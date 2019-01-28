package com.example.panta.instagramimagedownloader

import android.annotation.SuppressLint
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast

class ImageSelectorActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var returnButton: Button
    lateinit var allSaveButton: Button

    private lateinit var imageDownloader:ImageDownloader

    @SuppressLint("ShowToast")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_selector)

        listView = findViewById(R.id.listView)
        imageDownloader = ImageDownloader(applicationContext)

        val intent = intent
        val listBean = intent.getSerializableExtra("listBean") as ListBean
        //Log.d("list", listBean.list.toString())

        title = "保存する画像をタップしてください"

        val adapter: BaseAdapter = MyArrayAdapter(applicationContext, R.layout.list_layout, listBean.list)
        listView.adapter = adapter

        listView.setOnItemClickListener{ parent, view, position, id ->
            Log.d("selected", position.toString())
            val item = parent.getItemAtPosition(position).toString()
            Log.d("ITEM", item)
            AlertDialog.Builder(this).apply {
                //title = "画像を保存しますか？"
                setTitle("画像を保存しますか？")
                setMessage("必ず画像の投稿者の了承を得てから保存してください。")
                setPositiveButton("OK") { _, _ ->
                    imageDownloader.saveImage(item)

                    Toast.makeText(applicationContext, "画像の保存が完了しました", Toast.LENGTH_LONG).show()

                }
                setNegativeButton("Cancel", null)
            }.show()
            //imageDownloader.saveImage(item)
        }


    }

    override fun onStart(){
        super.onStart()

    }


}
