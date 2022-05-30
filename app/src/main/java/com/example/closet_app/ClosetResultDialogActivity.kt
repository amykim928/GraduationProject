package com.example.closet_app

import android.app.Dialog
import android.content.Context
import android.view.WindowManager

class ClosetResultDialogActivity(context: Context){
    private val dialog = Dialog(context)

    fun showDialog(){
        dialog.setContentView(R.layout.activity_closet_result_dialog)

        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }
}