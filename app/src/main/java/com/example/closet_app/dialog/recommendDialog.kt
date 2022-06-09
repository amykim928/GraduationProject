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

//recommendActivty에서,
//저장된 옷을 불러올 때 쓰는 Dialog입니다.
class recommendDialog (context: Context)
{
    //dialog와 클릭되었을 때 이벤트를 만들 리스너, 그리고 bitmap 리스트를 담을 Bit입니다.
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener
    val bit:ArrayList<Bitmap> = ArrayList<Bitmap>()


    interface OnDialogClickListener
    {
        fun onClicked(image: Drawable,int: Int)
    }

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }


    //showDialog를 통해 다이얼로그가 보여지고,
    fun showDialog()
    {
        //3번째의 경우 밖에 화면을 누르면 화면이 자동으로 종료된다는 것입니다.
        dialog.setContentView(R.layout.dialog_for_recommend)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        //bit ArrayList를 초기화하고, 값을 설정합니다.
        setBits()

        //Dialog의 이미지뷰를 찾고
        val img1=dialog.findViewById<ImageView>(R.id.img1)
        val img2=dialog.findViewById<ImageView>(R.id.img2)
        val img3=dialog.findViewById<ImageView>(R.id.img3)


        //각 imageView에 버튼처럼 클릭 리스너를 달아줍니다.
        //함수에 img1에 들어있는 사진과 ,정수를 같이 보내줍니다.
        //위의 인터페이스에서,onClicked에는 정수와 drawable을 주겠다고 약속을 한것이죠.
        //이 클릭 리스너가 무엇을 할지는 recommendActivity에서 자세히 정의됩니다.
        //다이얼로그에서는 어떤 걸 넣어줄지를 정의하는 정도만!

        img1.setOnClickListener{
            onClickListener.onClicked(img1.drawable,0)
            dialog.dismiss()
        }
        img2.setOnClickListener{
            onClickListener.onClicked(img2.drawable,1)
            dialog.dismiss()
        }
        img3.setOnClickListener{
            onClickListener.onClicked(img3.drawable,1)
            dialog.dismiss()
        }
        //bit에 담긴 이미지를 통해 초기화합니다.
        //프로토타입이니까 3개만 출력했습니다.
        img1.setImageBitmap(bit[0])
        img2.setImageBitmap(bit[1])
        img3.setImageBitmap(bit[2])

        dialog.show()


        //두 코드 모두 dialog를 종료하는 코드입니다
        dialog.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.finish_button).setOnClickListener {
            dialog.dismiss()
        }

    }


    //음... setBits() 지겹도록 보실텐데
    private fun setBits() {
        //File 경로를 가진 파일 변수 선언
        val dirs = File("/data/user/0/com.example.closet_app/files/save")
        //그 경로의 하위 디렉토리를 listFiles통해 받아옵니다.
        val fileDirs=dirs.listFiles()


        //null check
        if(fileDirs != null){
            //파일이 없을 경우
            if(fileDirs.isEmpty()){
                Log.i("warning","cannot find dir or no files")
            }
            //파일이 있는 경우
            else{
                //tmpfile에서 하위 디렉토리 하나하나에 접근하게 됩니다.
                for(tmpFile in fileDirs){
                    //새로운 경로를 만들고
                    val path="$dirs/${tmpFile.name}"
                    //그 경로가 jpg이면 bitmap에 넣습니다.
                    if(".jpg" in path){
                        bit.add(BitmapFactory.decodeFile(path))
                    }else{
                        Log.i("텍스트입니다.", tmpFile.name)
                    }

                }
            }
        }else{
            Log.i("error","filedirs null")
        }


    }

}
