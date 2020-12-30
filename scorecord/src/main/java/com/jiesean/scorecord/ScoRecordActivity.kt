package com.jiesean.scorecord

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

/**
 * SCO链路录取tws耳机的音频示例
 * 注意权限声明和动态权限获取
 * 此处不做详细说明
 */
class ScoRecordActivity : AppCompatActivity() {
    companion object {
        private val TAG: String = ScoRecordActivity.toString()
    }

    var mAudioManager: AudioManager ?= null

    private val mStartScoBtn : Button by lazy { findViewById<Button>(R.id.startScoBtn) }
    private val mStartRecordBtn : Button by lazy { findViewById<Button>(R.id.startRecordBtn) }
    private val mStopRecordBtn : Button by lazy { findViewById<Button>(R.id.stopRecordBtn) }
    private val mReadDateSize : TextView by lazy { findViewById<TextView>(R.id.textView2) }

    private var mAudioRecord: AudioRecord ? = null

    private var mIsRecording: Boolean = false
    private var mSavedDataSize: Int = 0

    //录音采集的来源，如系统mic、有线耳机mic等
    private var mAudioSource: Int = MediaRecorder.AudioSource.VOICE_COMMUNICATION
    //采样率，智能语音系统后续处理一般使用16k的原始音频，所以如果能满足的情况下，一般选择16k的采样率进行采样
    //如果系统不支持16K,可以选择44.1k或者其他采样率，然后通过降采样之后进行使用
    private var mAudioSampleRate: Int = 16000;
    //通道数，使用智能手机的情况下，一般选择一通道数据，作为近场语音的输入
    //特殊系统下，我们可以通过修改系统framework的音频的方式，将音频编码
    //通过AudioRecrod将其录制出来，AudioRecord一般默认支持2通道
    private var mAudioChanalConfig: Int =  AudioFormat.CHANNEL_IN_MONO
    //智能语音系统一般使用16bit的采样位深
    private var mAudioBitDepth: Int =  AudioFormat.ENCODING_PCM_16BIT
    //获取录音需要的buffer
    private var mAudioBuffer: Int = AudioRecord.getMinBufferSize(mAudioSampleRate,mAudioChanalConfig,mAudioBitDepth)
    //每次采集的buffer
    private var mAudioBufferPerFrame: ByteArray = ByteArray(32 * 16 )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sco_record)

        initScoStateReceiver()

        mStartScoBtn.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                startSco()
            }

        })

        mStartRecordBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                try {
                    mAudioRecord = AudioRecord(mAudioSource, mAudioSampleRate, mAudioChanalConfig, mAudioBitDepth, mAudioBuffer)
                    mAudioRecord?.startRecording()
                }catch (ex: Exception){
                    ex.stackTrace
                    Log.d("AudioRecordDemoActivity",ex.toString())
                }
                var thread: Thread = Thread(AudioRecordRunnable())
                thread.start()
                mSavedDataSize = 0
            }

        })

        mStopRecordBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                mIsRecording = false
                mAudioRecord?.stop()
                mAudioRecord?.release()
            }

        })
    }

    private fun startSco(){
        mAudioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        //设置Audio 模式为communication ,在部分华为手机上或者较老的机型上可能需要设置为call
        if (!mAudioManager!!.isBluetoothScoAvailableOffCall) {
            return
        }
        mAudioManager?.mode = AudioManager.MODE_IN_COMMUNICATION
        //开启sco 链路
        mAudioManager?.startBluetoothSco()
    }

    private fun initScoStateReceiver(){
        var scoStateFilter : IntentFilter = IntentFilter()
        scoStateFilter.addAction(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED)

        this.registerReceiver(ScoStateBroadcastReceiver(),scoStateFilter)
    }

    fun writeFile(buffer: ByteArray, filePath: String, isAppend: Boolean) {
        val file = File(filePath)
        var fos: FileOutputStream? = null
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            fos = FileOutputStream(file, isAppend)
            fos.write(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (fos != null) {
                    fos.flush()
                    fos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    inner class ScoStateBroadcastReceiver:BroadcastReceiver(){
        override fun onReceive(p0: Context?, intent: Intent?) {
            val state = intent?.getIntExtra(
                AudioManager.EXTRA_SCO_AUDIO_STATE,
                AudioManager.SCO_AUDIO_STATE_ERROR
            )

            when(state){
                AudioManager.SCO_AUDIO_STATE_DISCONNECTED ->{
                    Log.d(TAG,"Sco state = SCO_AUDIO_STATE_DISCONNECTED")
                }
                AudioManager.SCO_AUDIO_STATE_CONNECTED ->{
                    Log.d(TAG,"Sco state = SCO_AUDIO_STATE_CONNECTED")
                    mAudioManager?.isBluetoothScoOn = true
                }
                AudioManager.SCO_AUDIO_STATE_CONNECTING ->{
                    Log.d(TAG,"Sco state = SCO_AUDIO_STATE_CONNECTING")
                }
            }
        }

    }

    private inner class AudioRecordRunnable :Runnable{
        override fun run() {
            mIsRecording = true
            while (mIsRecording){
                var size = mAudioRecord?.read(mAudioBufferPerFrame,0,mAudioBufferPerFrame.size)
                size.let {
                    if(it!! > 0){
                        mSavedDataSize = mSavedDataSize + size!!
                        runOnUiThread {
                            mReadDateSize.setText("已录取音频，大小为: ${mSavedDataSize}")
                        }

//                        writeFile(mAudioBufferPerFrame,"/sdcard/record.pcm",true)
                    }
                }

            }
        }

    }
}