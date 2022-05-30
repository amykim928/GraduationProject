package com.example.closet_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ClosetActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var textView: TextView? = null
    var pick: Button? = null
    var uri = ArrayList<Uri>()

    var adapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closet)

        val gohome: Button = findViewById<View>(R.id.gohome) as Button
        gohome.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }

        val result: Button = findViewById<View>(R.id.result) as Button
        result.setOnClickListener {
            val myIntent = Intent(this, ClosetResultActivity::class.java)
            startActivity(myIntent)
        }

        textView = findViewById(R.id.totalPhotos)
        recyclerView = findViewById(R.id.recyclerView)
        pick = findViewById(R.id.pick)
        adapter = RecyclerAdapter(uri)
        recyclerView?.setLayoutManager(GridLayoutManager(this@ClosetActivity, 4))
        recyclerView?.setAdapter(adapter)
        if (ContextCompat.checkSelfPermission(
                this@ClosetActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@ClosetActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Read_Permission
            )
        }
        pick?.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data!!.clipData != null) {
                val x = data.clipData!!.itemCount
                for (i in 0 until x) {
                    uri.add(data.clipData!!.getItemAt(i).uri)
                }
                adapter!!.notifyDataSetChanged()
                textView!!.text = "옷장(" + uri.size + ")"
            } else if (data.data != null) {
                val imageURL = data.data!!.path
                uri.add(Uri.parse(imageURL))
            }
        }
    }

    companion object {
        private const val Read_Permission = 101
    }
}