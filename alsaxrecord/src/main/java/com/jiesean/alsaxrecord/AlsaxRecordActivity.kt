package com.jiesean.alsaxrecord

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jiesean.alsax.BasexEventListener
import com.jiesean.alsax.BasexManager
import com.jiesean.alsaxrecord.databinding.ActivityAlsaxRecordBinding

/**
 * tinyalsa录音的使用示例，具体的调用代码请参考alsax module以及alsax so代码
 */
class AlsaxRecordActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityAlsaxRecordBinding

    companion object{
        val alsax: BasexManager
            get() = BasexManager.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //在kotlin-android-extentions被废弃的情况下，使用viewBinding进行控件绑定
        viewBinding = ActivityAlsaxRecordBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.alsaxRecordBtn.setOnClickListener {
            //此次参数仅为一个使用示例，该示例只能在特定的平台上才能运行，需要针对性进行设置
            //参数过于冗长，且很多参数应该设置默认值，无需每一个参数都进行设置
            alsax.initBasex(-1,"hw:1,0",6,8,"0,1,2,3,4,5,6,7",16,16000,
                0,0,0,256,4,AlsaxListener())
        }
    }

    class AlsaxListener: BasexEventListener{
        override fun onRecordEnd() {
        }

        override fun onSaveData(data: ByteArray?, size: Int) {
            //保存音频在此处即可
        }

        override fun onRecordStart() {
        }

    }
}