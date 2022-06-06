package com.example.closet_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast

class RecommendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        val gohome: Button = findViewById<View>(R.id.gohome) as Button
        gohome.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }

        val gSatisfaction: RadioGroup = findViewById<View>(R.id.gSatisfaction) as RadioGroup
        val satisfaction: Button = findViewById<View>(R.id.saveSatisfaction) as Button
        satisfaction.setOnClickListener {
            Toast.makeText(this@RecommendActivity, "만족도를 저장하였습니다!", Toast.LENGTH_SHORT).show()
            gSatisfaction.clearCheck()
//            val myIntent = Intent(this, MainActivity::class.java)
//            startActivity(myIntent)
        }

        val saveResult: Button = findViewById<View>(R.id.saveResult) as Button
        saveResult.setOnClickListener {
            Toast.makeText(this@RecommendActivity, "결과를 저장하였습니다!", Toast.LENGTH_SHORT).show()
//            val myIntent = Intent(this, MainActivity::class.java)
//            startActivity(myIntent)
        }

        val goCloset: Button = findViewById<View>(R.id.goCloset) as Button
        goCloset.setOnClickListener {
            val myIntent = Intent(this, SavedClothActivity::class.java)
            startActivity(myIntent)
        }
    }
}