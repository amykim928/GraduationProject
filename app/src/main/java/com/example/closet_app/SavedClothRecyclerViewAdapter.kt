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

//class SavedClothRecyclerViewAdapter(private val itemList: List<ClosetData>) : RecyclerView.Adapter<SavedClothRecyclerViewAdapter.MyViewHolder>(){
//    inner class MyViewHolder(val binding: ClosetItemBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: ClosetData){
//            binding.cls = item
//        }
//    }
////ClosetItemBinding
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val lisItemBinding = ClosetItemBinding.inflate(inflater, parent, false)
//        return MyViewHolder(lisItemBinding)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.bind(itemList[position])
//    }
//
//    override fun getItemCount(): Int {
//        return itemList.size
//    }
//
//}