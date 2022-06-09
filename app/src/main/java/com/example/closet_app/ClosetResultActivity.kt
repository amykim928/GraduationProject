package com.example.closet_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.closet_app.databinding.ActivityClosetResultBinding
import com.example.closet_app.dialog.ClosetResultDialogActivity


//현아님 액티비티에서 가져왔는데 현아님이 못쓸 것 같다고 하셨죠.
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