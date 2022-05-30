package com.example.closet_app

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class SpinnerActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)

        var colorSpinner = findViewById<Spinner>(R.id.colorSpinner)
        var colorData = resources.getStringArray(R.array.color)
        var colorAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, colorData)
        colorSpinner.adapter = colorAdapter
        colorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        var styleSpinner = findViewById<Spinner>(R.id.styleSpinner)
        var styleData = resources.getStringArray(R.array.style)
        var styleAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, styleData)
        styleSpinner.adapter = styleAdapter
        styleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

    }
}