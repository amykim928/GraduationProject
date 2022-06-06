package com.example.closet_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SavedClothActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var textView: TextView? = null
    var uri = ArrayList<Uri>()

    var adapter: SavedClothRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_cloth)

        val gohome: Button = findViewById<View>(R.id.gohome) as Button
        gohome.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }

        textView = findViewById(R.id.totalPhotos)
        recyclerView = findViewById(R.id.recyclerView)
        adapter = SavedClothRecyclerViewAdapter(uri)
        recyclerView?.setLayoutManager(GridLayoutManager(this@SavedClothActivity, 4))
        recyclerView?.setAdapter(adapter)
    }
}