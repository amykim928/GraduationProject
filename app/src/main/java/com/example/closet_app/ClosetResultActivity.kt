package com.example.closet_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.closet_app.databinding.ActivityClosetResultBinding

class ClosetResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClosetResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClosetResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveBtn.setOnClickListener {
            val dialog = ClosetResultDialogActivity(this)
            dialog.showDialog()
        }

    }
}