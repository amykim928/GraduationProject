package com.example.closet_app.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import com.example.closet_app.R
import java.io.File

class recommendDialog (context: Context)
{
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener
    val bit:ArrayList<Bitmap> = ArrayList<Bitmap>()


    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.dialog_for_recommend)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        setBits()
        if(bit.size==0){
            Log.i("낄낄","없다는데")
        }else{
            Log.i("있네",bit.size.toString())
        }
        val img1=dialog.findViewById<ImageView>(R.id.img1)
        val img2=dialog.findViewById<ImageView>(R.id.img2)
        val img3=dialog.findViewById<ImageView>(R.id.img3)


        img1.setOnClickListener{
            onClickListener.onClicked(img1.drawable)
            dialog.dismiss()
        }
        img2.setOnClickListener{
            onClickListener.onClicked(img2.drawable)
            dialog.dismiss()
        }
        img1.setImageBitmap(bit[0])
        img2.setImageBitmap(bit[1])

        dialog.show()

        dialog.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.finish_button).setOnClickListener {
            dialog.dismiss()
        }

    }

    interface OnDialogClickListener
    {
        fun onClicked(image: Drawable)
    }

    private fun setBits() {
        val dirs = File("/data/user/0/com.example.closet_app/files/save")
        val fileDirs=dirs.listFiles()

        if(fileDirs != null){
            if(fileDirs.isEmpty()){
                Log.i("warning","cannot find dir or no files")
            }else{
                for(tmpFile in fileDirs){
                    Log.i("files", tmpFile.name)
                    val path="$dirs/${tmpFile.name}"
                    bit.add(BitmapFactory.decodeFile(path))
                }
            }
        }else{
            Log.i("error","filedirs null")
        }


    }

}
