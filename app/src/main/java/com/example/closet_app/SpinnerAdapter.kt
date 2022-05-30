package com.example.closet_app

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SpinnerAdapter(internal var context: Context, internal var colorShapeData: Array<String>, internal var colorTextData: Array<String>) :
    BaseAdapter() {
    internal var inflter: LayoutInflater

    init {
        inflter = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return 13
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {

        val view = inflter.inflate(R.layout.color_spinner,null)
        val spinnerShape = view.findViewById<View>(R.id.spinnershape)
        val spinnerText = view.findViewById<TextView>(R.id.spinnertext)
        spinnerShape!!.setBackgroundColor(Color.parseColor(colorShapeData[i]))
        spinnerText!!.text = colorTextData[i]
        return view
    }
}