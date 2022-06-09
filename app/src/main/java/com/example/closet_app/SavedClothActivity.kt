package com.example.closet_app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.closet_app.databinding.ActivitySavedClothBinding
import java.io.File

//추천된 옷을 볼 수 있는 Activity입니다
class SavedClothActivity : AppCompatActivity() {
    //binding과 bits, texts 선언
    lateinit var binding: ActivitySavedClothBinding
    private val bits=ArrayList<Bitmap>()
    val texts=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySavedClothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val goHome =binding.gohome


        setBits()
        setTexts()

        val numBit=bits.size/2
        when (numBit) {
            1 -> {
                binding.img1.setImageBitmap(bits[0])
                binding.img2.setImageBitmap(bits[1])
            }
            2 -> {
                binding.img1.setImageBitmap(bits[0])
                binding.img2.setImageBitmap(bits[1])
                binding.img3.setImageBitmap(bits[2])
                binding.img4.setImageBitmap(bits[3])
            }
            3 -> {
                binding.img1.setImageBitmap(bits[0])
                binding.img2.setImageBitmap(bits[1])
                binding.img3.setImageBitmap(bits[2])
                binding.img4.setImageBitmap(bits[3])
                binding.img5.setImageBitmap(bits[4])
                binding.img6.setImageBitmap(bits[5])
            }
        }
        when (numBit) {
            1 -> {
                binding.text1.text=texts[0]
                binding.text2.text=texts[1]
                binding.text3.text=texts[2]
            }
            2 -> {
                binding.text1.text=texts[0]
                binding.text2.text=texts[1]
                binding.text3.text=texts[2]
                binding.text4.text=texts[3]
                binding.text5.text=texts[4]
                binding.text6.text=texts[5]
            }
            3 -> {
                binding.text1.text=texts[0]
                binding.text2.text=texts[1]
                binding.text3.text=texts[2]
                binding.text4.text=texts[3]
                binding.text5.text=texts[4]
                binding.text6.text=texts[5]
                binding.text7.text=texts[6]
                binding.text8.text=texts[7]
                binding.text9.text=texts[8]
            }
        }

        when(numBit){
            1->{
                binding.img3.visibility=View.INVISIBLE
                binding.img4.visibility=View.INVISIBLE
                binding.img5.visibility=View.INVISIBLE
                binding.img6.visibility=View.INVISIBLE
                binding.text4.text=""
                binding.text5.text=""
                binding.text6.text=""
                binding.text7.text=""
                binding.text8.text=""
                binding.text9.text=""
            }
            2->{
                binding.img5.visibility=View.INVISIBLE
                binding.img6.visibility=View.INVISIBLE
                binding.text7.text=""
                binding.text8.text=""
                binding.text9.text=""
            }
        }

        goHome.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }
    }

    // recommend경로에서 txt를 읽어오고
    // txt의 값을 split으로 나눈뒤, texts에 저장합니다.
    // [블랙, 트레디셔널, 재킷]
    private fun setTexts() {
        val dirs = File("$filesDir/recommend")
        val fileDirs=dirs.listFiles()
        texts.clear()
        if(fileDirs != null){
            if(fileDirs.isEmpty()){
                Log.i("warning","cannot find dir or no files")
            }else{
                for(tmpFile in fileDirs){
                    var temp=""
                    Log.i("files", tmpFile.name)
                    val path="$dirs/${tmpFile.name}"
                    val tmp=File(path)
                    if(".txt" in path){
                        val reader=tmp.bufferedReader()
                        val iterator = reader.lineSequence().iterator()

                        while(iterator.hasNext()) {
                            temp+=iterator.next()
                        }
                        reader.close()
                    }
                    if(temp.split("/")[0].isNotEmpty()){
                        texts.addAll(temp.split("/"))
                    }

                }
            }
        }else{
            Log.i("error","filedirs null")
        }

        Log.i("texts:",texts.toString())
    }
    // recommend경로에서 txt를 읽어오고
    // jpg를 두가지로 나눠서 저장한 것을 읽어오고, texts에 저장합니다.
    private fun setBits() {
        val dirs = File("$filesDir/recommend")
        val fileDirs=dirs.listFiles()
        bits.clear()
        if(fileDirs != null){
            if(fileDirs.isEmpty()){
                Log.i("warning","cannot find dir or no files")
            }else{
                for(tmpFile in fileDirs){
                    Log.i("files", tmpFile.name)
                    val path="$dirs/${tmpFile.name}"
                    if("_0.jpg" in path){
                        bits.add(BitmapFactory.decodeFile(path))
                    }
                    else if("_1.jpg" in path){
                        bits.add(BitmapFactory.decodeFile(path))
                    }
                }
            }
        }else{
            Log.i("error","filedirs null")
        }


    }
}