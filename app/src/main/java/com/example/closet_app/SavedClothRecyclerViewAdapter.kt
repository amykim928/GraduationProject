package com.example.closet_app

import android.R
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SavedClothRecyclerViewAdapter(private val uriArrayList: ArrayList<Uri>) :
    RecyclerView.Adapter<SavedClothRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(com.example.closet_app.R.layout.custom_saved_cloth_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView1.setImageURI(uriArrayList[position])
        holder.imageView2.setImageURI(uriArrayList[position])

        print(uriArrayList[position])
    }

    override fun getItemCount(): Int {
        return uriArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView1: ImageView
        var imageView2: ImageView
        var textView1: TextView
        var textView2: TextView
        var textView3: TextView


        init {
            imageView1 = itemView.findViewById(com.example.closet_app.R.id.image1)
            imageView2 = itemView.findViewById(com.example.closet_app.R.id.image2)
            textView1 = itemView.findViewById(com.example.closet_app.R.id.text1)
            textView2 = itemView.findViewById(com.example.closet_app.R.id.text2)
            textView3 = itemView.findViewById(com.example.closet_app.R.id.text3)
        }
    }
}