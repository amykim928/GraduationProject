package com.example.closet_app.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.closet_app.R


//옷장 recycleradapter입니다 정현님 구현과 거의 차이는 없지만, ArrayList를 url에서 bitmap으로 변경했습니다.

//ArrayList<Bitmap> , 비트맵(사진) arraylist를 받아, viewHolder에서 보여주는 역할을 합니다.

class RecyclerAdapter(private val bitArrayList: ArrayList<Bitmap>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.custom_single_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageBitmap(bitArrayList[position])
        holder.nameText.text="사진"

//        Log.d("position",bitArrayList[position].toString())
    }

    override fun getItemCount(): Int {
        return bitArrayList.size
    }
    //ViewHolder에서 이미지뷰와 TextView를 선언합니다. 그리고 그걸 onBindViewHolder에서 사용하죠.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView =itemView.findViewById(R.id.bitimg)
        val nameText:TextView = itemView.findViewById(R.id.nameText)


    }
}