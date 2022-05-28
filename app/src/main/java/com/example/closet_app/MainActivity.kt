package com.example.closet_app


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.closet_app.R.layout.activity_main


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        val recommend: Button = findViewById<View>(R.id.recommend) as Button
        val closet: Button = findViewById<View>(R.id.closet) as Button
        val savedCloth: Button = findViewById<View>(R.id.savedCloth) as Button

        recommend.setOnClickListener {
            val myIntent = Intent(this, RecommendActivity::class.java)
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