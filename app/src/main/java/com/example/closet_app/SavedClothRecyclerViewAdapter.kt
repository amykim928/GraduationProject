package com.example.closet_app

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
        val view = inflater.inflate(R.layout.custom_saved_cloth_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageURI(uriArrayList[position])

        print(uriArrayList[position])
    }

    override fun getItemCount(): Int {
        return uriArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var textView: TextView

        init {
            imageView = itemView.findViewById(R.id.image)
            textView = itemView.findViewById(R.id.text1)
            textView = itemView.findViewById(R.id.text2)
            textView = itemView.findViewById(R.id.text3)
        }
    }
}