package com.example.closet_app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import com.example.closet_app.data.API
import com.example.closet_app.data.ImageFeatures
import com.example.closet_app.data.ImgDataModel
import com.example.closet_app.data.ImgLabelModel
import com.example.closet_app.dialog.recommendDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*

class RecommendActivity : AppCompatActivity() {
    lateinit var mRetrofit: Retrofit // 사용할 레트로핏 객체입니다.
    lateinit var mRetrofitAPI: API.RetrofitAPI // 레트로핏 api객체입니다.
    lateinit var mCallImgList: Call<ImgDataModel> // Json형식의 데이터를 요청하는 객체입니다.


    lateinit var mCallImgList2: Call<ImgLabelModel>

    val ImgMap= hashMapOf("0" to "탑", "1" to "블라우스", "2" to "티셔츠", "3" to "니트웨어", "4" to "셔츠", "5" to "브라탑",
    "6" to "후드티", "7" to "청바지", "8" to "팬츠", "9" to "스커트", "10" to "레깅스", "11" to "조거팬츠", "12" to "코트",
        "13" to "재킷", "14" to "점퍼", "15" to "패딩", "16" to "베스트", "17" to "가디건", "18" to "짚업", "19" to "드레스", "20" to "점프수트"
    )
    lateinit var imageView: ImageView
    //detectActivity에서 ArrayList<Uri>를 건네주는 식으로 바꾸려고 하네요.
    val bit = ArrayList<Bitmap>()
    val labels=ArrayList<String>()
    val scores=ArrayList<Int>()

    val savedClothes=ArrayList<String>()
    val savedClothesName=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)
        setRetrofit()
        val gohome: Button = findViewById<View>(R.id.gohome) as Button

        //binding으로 합시다...
        //너무 많아요 ㅠ

        imageView=findViewById(R.id.recommendImg)
        val fromCloset=findViewById<ImageView>(R.id.fromClosetImg)

        val gSatisfaction: RadioGroup = findViewById<View>(R.id.gSatisfaction) as RadioGroup
        val radioBtn1=findViewById<RadioButton>(R.id.veryUnsatisfied)
        val radioBtn2=findViewById<RadioButton>(R.id.unsatisfied)
        val radioBtn3=findViewById<RadioButton>(R.id.good)
        val radioBtn4=findViewById<RadioButton>(R.id.satisfied)
        val radioBtn5=findViewById<RadioButton>(R.id.verySatisfied)

        val saveSatisfaction: Button = findViewById<View>(R.id.saveSatisfaction) as Button
        val saveResult: Button = findViewById<View>(R.id.saveResult) as Button
        val retry: Button = findViewById<View>(R.id.retry) as Button
        val getCloset: Button = findViewById<View>(R.id.getCloth) as Button
        val colorOption = findViewById<TextView>(R.id.colorOption)
        val styleOption = findViewById<TextView>(R.id.styleOption)
        var forCloset:Int=0

        var satisfaction:String=""


        loadinginit()

        saveSatisfaction.setOnClickListener {

            if(radioBtn1.isChecked){
                satisfaction="매우 불만족"
            }
            if(radioBtn2.isChecked){
                satisfaction="불만족"
            }
            if(radioBtn3.isChecked){
                satisfaction="보통"
            }
            if(radioBtn4.isChecked){
                satisfaction="만족"
            }
            if(radioBtn5.isChecked){
                satisfaction="매우 만족"
            }
            Log.i("satisfaction",satisfaction.toString())
            Toast.makeText(this, "만족도를 저장했습니다!", Toast.LENGTH_SHORT).show()
            gSatisfaction.clearCheck()
        }

        saveResult.setOnClickListener {
            val style=styleOption.text.toString()
            val color=colorOption.text.toString()
            var name=0
            val dirs = File("$filesDir/recommend")
            if (!dirs.exists()){
                dirs.mkdirs()
            }
            if(dirs.isDirectory) {	Log.i("dir", "디렉토리입니다..: $filesDir/recommend")}
            name= dirs.listFiles()?.size?.div(2) ?: 0

            val file = File("$filesDir/recommend/${name}_0.jpg")
            file.createNewFile()
            fromCloset.drawable.toBitmap().compress(Bitmap.CompressFormat.JPEG,100,FileOutputStream(file))
            //추천에서 받아온것.
            val file3= File("$filesDir/recommend/${name}_1.jpg")
            file.createNewFile()
            imageView.drawable.toBitmap().compress(Bitmap.CompressFormat.JPEG,100,FileOutputStream(file3))


            val file2=File("$filesDir/recommend/$name.txt")
            val printWriter= FileWriter(file2)
            val buffer= BufferedWriter(printWriter)
            buffer.write("$satisfaction/$style/$color")
            buffer.close()

            val myIntent = Intent(this, SavedClothActivity::class.java)
            startActivity(myIntent)
            finish()
        }

        //내부저장소에서 불러와서 Dialog로, Dialog에서 이미지 클릭하면 그 이미지가 추가됨
        retry.setOnClickListener {
            val dialog = recommendDialog(this)
            dialog.showDialog()
            dialog.setOnClickListener(object : recommendDialog.OnDialogClickListener {
                override fun onClicked(image: Drawable,int: Int) {
                    fromCloset.setImageDrawable(image)
                    forCloset=int
                //    Log.d("forCloset",int.toString())
                }
            })
        }


        // 이걸로 내부 저장소에 있는 옷을 가져와서 api에 보내보죠.
        getCloset.setOnClickListener {

            val bitString = bitmapToString(fromCloset.drawable.toBitmap())
            val category=findCategory(forCloset).toString()
  //         mCallImgList = mRetrofitAPI.postImgPredict(bitString)  // RetrofitAPI에서 Json객체 요청을 반환하는 메서드를 불러옵니다.

//           mCallImgList.enqueue(mRetrofitCallback)
            val hashMap= hashMapOf<String, ImageFeatures>(Pair(bitString,ImageFeatures(colorOption.text.toString(),category)))

            mCallImgList2=mRetrofitAPI.postPredict(hashMap)
            mCallImgList2.enqueue(mRetrofitCallback2)
//            Log.d("checkHashMap",labels[0].toString())
        }




        gohome.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }


        colorOption.text = intent.getStringExtra("color")
        styleOption.text = intent.getStringExtra("style")


        //해야할 것->내부 저장소에서 의상 불러오기
        //의상을 불러와  base64로 바꾸고, json으로 파싱해서 서버로 보내고
        //서버(모델)에서 값을 받아옴

    }

    private fun loadinginit() {
        val tmp = File("$filesDir/save/")
        val fileDirs=tmp.listFiles()

        if(fileDirs != null){
            if(fileDirs.isEmpty()){
                Log.i("warning","cannot find dir or no files")
            }else{
                for(tmpFile in fileDirs){
                    Log.i("files", tmpFile.name)
                    val path="$tmp/${tmpFile.name}"
                    var temp=""
                    if(".txt" in path){
                        val reader=tmpFile.bufferedReader()
                        val iterator = reader.lineSequence().iterator()

                        while(iterator.hasNext()) {
                            temp+=iterator.next()
                        }
                        reader.close()
                        savedClothes.add(temp)
                        savedClothesName.add(tmpFile.name)
                    }
                }
                Log.i("savedClothes",savedClothes.toString())
                Log.i("savedClothes",savedClothesName.toString())
            }
        }else{
            Log.i("error","filedirs null")
        }
    }

    private fun findCategory(forCloset: Int): String {
        val tmp = File("$filesDir/save/$forCloset.txt")
        var temp=""
        if(tmp.exists()){

            val reader=tmp.bufferedReader()
            val iterator = reader.lineSequence().iterator()

            while(iterator.hasNext()) {
                temp+=iterator.next()
            }
            reader.close()

        }
        temp=temp.replace(" ","")
        Log.i("tmp",temp)
        return temp
    }

    private fun setRetrofit() {
        //레트로핏으로 가져올 url설정하고 세팅
        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()
        mRetrofit = Retrofit
            .Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
//http://10.0.2.2:5000  //에뮬레이터 구동


        // http://192.168.115.236:5000</string>    핫스팟시,
        //인터페이스로 만든 레트로핏 api요청 받는 것 변수로 등록
        mRetrofitAPI = mRetrofit.create(API.RetrofitAPI::class.java)
    }
    //http요청을 보냈고 이건 응답을 받을 콜벡메서드
    private val mRetrofitCallback2 = (object : retrofit2.Callback<ImgLabelModel> {
        override fun onResponse(call: Call<ImgLabelModel>, response: Response<ImgLabelModel>) {
            val result = response.body()
            if(!bit.isEmpty()){
                bit.clear()
                scores.clear()
                labels.clear()
            }
            Log.i("need print result",result.toString())
            val img0=stringToBitmap(result!!.img0[0].toString())
            val img1=stringToBitmap(result.img1[0].toString())
            val img2=stringToBitmap(result.img2[0].toString())
            val img3=stringToBitmap(result.img3[0].toString())
            val img4=stringToBitmap(result.img4[0].toString())
            val img5=stringToBitmap(result.img5[0].toString())
            val img6=stringToBitmap(result.img6[0].toString())
            val img7=stringToBitmap(result.img7[0].toString())
            bit.add(img0)
            bit.add(img1)
            bit.add(img2)
            bit.add(img3)
            bit.add(img4)
            bit.add(img5)
            bit.add(img6)
            bit.add(img7)

            val label0=result.img0[1].toString()
            val label1=result.img1[1].toString()
            val label2=result.img2[1].toString()
            val label3=result.img3[1].toString()
            val label4=result.img4[1].toString()
            val label5=result.img5[1].toString()
            val label6=result.img6[1].toString()
            val label7=result.img7[1].toString()
            ImgMap[label0]?.let { labels.add(it) }
            ImgMap[label1]?.let { labels.add(it) }
            ImgMap[label2]?.let { labels.add(it) }
            ImgMap[label3]?.let { labels.add(it) }
            ImgMap[label4]?.let { labels.add(it) }
            ImgMap[label5]?.let { labels.add(it) }
            ImgMap[label6]?.let { labels.add(it) }
            ImgMap[label7]?.let { labels.add(it) }

            val score0=result.img0[2].toString().toInt()
            val score1=result.img1[2].toString().toInt()
            val score2=result.img2[2].toString().toInt()
            val score3=result.img3[2].toString().toInt()
            val score4=result.img4[2].toString().toInt()
            val score5=result.img5[2].toString().toInt()
            val score6=result.img6[2].toString().toInt()
            val score7=result.img7[2].toString().toInt()

            scores.addAll(listOf(score0,score1,score2,score3,score4,score5,score6,score7))
            Log.i("check score",scores.toString())
            if(scores.contains(2)){
                val idx:Int=scores.indexOf(2)
                val findLabel=labels[idx]
                Log.i("label?",findLabel)
                if(savedClothes.contains(findLabel)){
                    val findFile= savedClothesName[savedClothes.indexOf(findLabel)].replace(".txt",".jpg")
                    val dirs = File("$filesDir/save")
                    val path="$dirs/${findFile}"
                    Log.i("check path",path)
                    if(".jpg" in path){
                        imageView.setImageBitmap(BitmapFactory.decodeFile(path))
                    }
                }
                else{
                    imageView.setImageBitmap(bit[idx])
                }

            }
            else if(scores.contains(0)){
                val idx=scores.indexOf(0)
                val findLabel=labels[idx]
                Log.i("label?",findLabel)
                if(savedClothes.contains(findLabel)) {
                    val findFile =
                        savedClothesName[savedClothes.indexOf(findLabel)].replace(".txt", ".jpg")
                    val dirs = File("$filesDir/save")
                    val path = "$dirs/${findFile}"
                    Log.i("check path", path)
                    if (".jpg" in path) {
                        imageView.setImageBitmap(BitmapFactory.decodeFile(path))
                    }
                }
                else{
                    imageView.setImageBitmap(bit[idx])
                }
            }
            else{
                imageView.setImageBitmap(bit[0])
            }

            Log.i("check Label",label0)
        }

        override fun onFailure(call: Call<ImgLabelModel>, t: Throwable) {
            t.printStackTrace()
            Log.i("failureT",t.message.toString())
        }


    })//Json객체를 응답받는 콜백 객체



    private fun bitmapToString(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun stringToBitmap(encodedString: String): Bitmap {

        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}



// val bitmap = stringToBitmap("여기에 인코딩된 이미지 string")
//
//image.setImageBitmap(bitmap)

//    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, "여기에 이미지 uri")
//
//    bitmapToString(bitmap)
//http요청을 보냈고 이건 응답을 받을 콜벡메서드
//private val mRetrofitCallback = (object : retrofit2.Callback<ImgDataModel> {
//    override fun onResponse(call: Call<ImgDataModel>, response: Response<ImgDataModel>) {
//        val result = response.body()
//        val img0=stringToBitmap(result!!.img0)
//        val img1=stringToBitmap(result.img1)
//        val img2=stringToBitmap(result.img2)
//        val img3=stringToBitmap(result.img3)
//        val img4=stringToBitmap(result.img4)
//        val img5=stringToBitmap(result.img5)
//        val img6=stringToBitmap(result.img6)
//        val img7=stringToBitmap(result.img7)
//        bit.add(img0)
//        bit.add(img1)
//        bit.add(img2)
//        bit.add(img3)
//        bit.add(img4)
//        bit.add(img5)
//        bit.add(img6)
//        bit.add(img7)
//        imageView.setImageBitmap(img1)
//        //  saveBitmap(img1,1.toString())
//        Log.i("fifth","maybehere")
//        Log.i("bits?:", bit[1].height.toString())
//        // Toast.makeText(this@RecommendActivity,"추천 완료",Toast.LENGTH_SHORT).show()
//
//
//    }
//
//    override fun onFailure(call: Call<ImgDataModel>, t: Throwable) {
//        t.printStackTrace()
//        Log.i("failureT",t.message.toString())
//    }
//
//
//})//Json객체를 응답받는 콜백 객체
//
//private fun saveBitmap(bitmap: Bitmap, name: String) {
//
//    //내부저장소 캐시 경로를 받아옵니다.
//    //지금은 그냥 filesdir로 했지만,
//    //나중에는 dir에 디렉토리를 만들어서 /files/cropped/1.jpg 이런식으로 저장하는게 좋겟죠.
//
//    val storage=filesDir
//    val filename= "saved$name.jpg"
//
//    val tmpFile=File(storage,filename)
//    tmpFile.createNewFile()
//    bitmap.compress(Bitmap.CompressFormat.JPEG,100, FileOutputStream(tmpFile))
//
//}