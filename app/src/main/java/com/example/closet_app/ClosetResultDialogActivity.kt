package com.example.closet_app


import android.content.Intent
import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button

class ClosetResultDialogActivity(context: Context){
    private val dialog = Dialog(context)

    fun showDialog(){
        dialog.setContentView(R.layout.activity_closet_result_dialog)

        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        val saveAnotherBtn = dialog.findViewById<Button>(R.id.saveAnotherBtn)
        val searchAnotherBtn = dialog.findViewById<Button>(R.id.searchAnotherBtn)

        saveAnotherBtn.setOnClickListener {
            val myIntent = Intent(dialog.context, ClosetActivity::class.java)
            dialog.context.startActivity(myIntent);
        }

        searchAnotherBtn.setOnClickListener {
            val myIntent = Intent(dialog.context, RecommendActivity::class.java)
            dialog.context.startActivity(myIntent);
        }

        dialog.show()
    }

}