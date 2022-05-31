package com.example.closet_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.closet_app.adapter.SpinnerAdapter

class SpinnerActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)

        var colorShapeData = resources.getStringArray(R.array.colorShapeData)
        var colorTextData = resources.getStringArray(R.array.colorTextData)
        val colorAdapter = SpinnerAdapter(applicationContext, colorShapeData, colorTextData)

        val colorSpinner = findViewById<View>(R.id.colorSpinner) as Spinner
        colorSpinner.adapter = colorAdapter
        colorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        var styleSpinner = findViewById<Spinner>(R.id.styleSpinner)
        var styleData = resources.getStringArray(R.array.styleData)
        var styleAdapter = ArrayAdapter<String>(this, R.layout.style_spinner, R.id.styleSpinnerText, styleData)
        styleSpinner.adapter = styleAdapter
        styleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        val optionSaveBtn = findViewById<Button>(R.id.optionSaveBtn)
        optionSaveBtn.setOnClickListener {
            val myIntent = Intent(this, RecommendActivity::class.java)
            val colorSpinnerText = colorSpinner.selectedView.findViewById<TextView>(R.id.colorSpinnerText)
            myIntent.putExtra("color", colorSpinnerText.text.toString())
            myIntent.putExtra("style", styleSpinner.selectedItem.toString())
            startActivity(myIntent)
        }
    }
}