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
//import com.example.closet_app.databinding.ActivityClosetBinding
import com.example.closet_app.R.layout.activity_saved_cloth

class SavedClothActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityClosetBinding
//    private lateinit var manager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_saved_cloth)

        val gohome: Button = findViewById<View>(R.id.gohome) as Button
        gohome.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }
    }
}


//        binding = ActivityClosetBinding.inflate(layoutInflater)
//        manager = GridLayoutManager(this, 3)
//
//        var data = listOf(
//            ClosetData(
//                getDrawable(R.drawable.blouse)!!,
//                getDrawable(R.drawable.skirt)!!,
//                "만족",
//                "트레디셔널",
//                "베이지"
//            ),
//            ClosetData(
//                getDrawable(R.drawable.blouse)!!,
//                getDrawable(R.drawable.skirt)!!,
//                "만족",
//                "트레디셔널",
//                "검정"
//            ),
//            ClosetData(
//                getDrawable(R.drawable.blouse)!!,
//                getDrawable(R.drawable.skirt)!!,
//                "만족",
//                "페미닌",
//                "핑크"
//            ),
//            ClosetData(
//                getDrawable(R.drawable.blouse)!!,
//                getDrawable(R.drawable.skirt)!!,
//                "만족",
//                "스트리트",
//                "파랑"
//            ),
//            ClosetData(
//                getDrawable(R.drawable.blouse)!!,
//                getDrawable(R.drawable.skirt)!!,
//                "만족",
//                "톰보이",
//                "빨강"
//            ),
//        )
//
//        binding.recyclerView.apply {
//            adapter = SavedClothRecyclerViewAdapter(data)
//            layoutManager = manager
//        }
//    }
//}
