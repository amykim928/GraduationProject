package com.example.closet_app


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.closet_app.R.layout.activity_main
import com.example.closet_app.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    //binding을 쓰면 개인적으로 편리해서, 이렇게 해봤습니다.
    //fragment에서는 처리가 좀 다르긴 한데, activity에서는 binding으로 찾아가면 findviewid 안써서 편하더라고요.

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val recommend: Button = binding.recommend
        val closet: Button = binding.closet
        val savedCloth: Button = binding.savedCloth

        recommend.setOnClickListener {
            val myIntent = Intent(this, SpinnerActivity::class.java)
            startActivity(myIntent)
        }

        closet.setOnClickListener {

            // 이동할 액티비티 경로 잡기
            val myIntent = Intent(this, ClosetActivity::class.java)

            // 이동하기
            startActivity(myIntent)

        }

        savedCloth.setOnClickListener {
            val myIntent = Intent(this, SavedClothActivity::class.java)
            startActivity(myIntent)
        }

    }
}