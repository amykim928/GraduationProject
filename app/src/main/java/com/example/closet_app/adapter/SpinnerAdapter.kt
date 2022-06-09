package com.example.closet_app.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.closet_app.R


//어댑터는 분리를 해두는 게 접근하기 편하더라고요.
//이 부분은 현아님 코드 그대로 썼습니다.


class SpinnerAdapter(internal var context: Context, private var colorShapeData: Array<String>, private var colorTextData: Array<String>) :
    BaseAdapter() {
    private var inflter: LayoutInflater = LayoutInflater.from(context)

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

        val spinnerView = inflter.inflate(R.layout.color_spinner,null)
        val spinnerShape = spinnerView.findViewById<View>(R.id.colorSpinnerShape)
        val spinnerText = spinnerView.findViewById<TextView>(R.id.colorSpinnerText)
        spinnerShape!!.setBackgroundColor(Color.parseColor(colorShapeData[i]))
        spinnerText!!.text = colorTextData[i]
        return spinnerView
    }
}