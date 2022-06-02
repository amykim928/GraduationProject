package com.example.closet_app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.closet_app.databinding.ActivitySavedClothBinding
import java.io.File

class SavedClothActivity : AppCompatActivity() {

    lateinit var binding: ActivitySavedClothBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySavedClothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gohome: Button = findViewById<View>(R.id.gohome) as Button

        val file = File(filesDir.toString())
        val files = file.listFiles()
        //    Log.i("first","maybehere")
        var bit:Bitmap?=null

        for (tempFile in files!!) {
            val path = filesDir.toString() + "/" + tempFile.name
            if("saved" in tempFile.name){
                Log.i("tags","saved")
                bit=BitmapFactory.decodeFile(path)
            }
        }
        binding.savedImgs.setImageBitmap(bit)
        binding.textView2.text="매우 만족/블랙/트레이셔널"





        gohome.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }
    }
}