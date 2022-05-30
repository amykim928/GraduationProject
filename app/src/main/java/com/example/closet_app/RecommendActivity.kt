package com.example.closet_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

class RecommendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        val gohome: Button = findViewById<View>(R.id.gohome) as Button

        val gSatisfaction: RadioGroup = findViewById<View>(R.id.gSatisfaction) as RadioGroup
        val saveSatisfaction: Button = findViewById<View>(R.id.saveSatisfaction) as Button
        val saveResult: Button = findViewById<View>(R.id.saveResult) as Button
        val retry: Button = findViewById<View>(R.id.retry) as Button
        val goCloset: Button = findViewById<View>(R.id.goCloset) as Button

        saveSatisfaction.setOnClickListener{
            Toast.makeText(this, "만족도를 저장했습니다!", Toast.LENGTH_SHORT).show()
            gSatisfaction.clearCheck()
        }

        saveResult.setOnClickListener {
            //val myIntent = Intent(this, ClosetActivity::class.java)
            Toast.makeText(this, "결과를 저장했습니다!", Toast.LENGTH_SHORT).show()
        }

        retry.setOnClickListener {
            //val myIntent = Intent(this, ClosetActivity::class.java)
            //startActivity(myIntent)
        }

        goCloset.setOnClickListener {
            val myIntent = Intent(this, ClosetActivity::class.java)
            startActivity(myIntent)
        }

        gohome.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }

        val colorOption = findViewById<TextView>(R.id.colorOption)
        val styleOption = findViewById<TextView>(R.id.styleOption)
        colorOption.text = intent.getStringExtra("color")
        styleOption.text = intent.getStringExtra("style")
    }
}