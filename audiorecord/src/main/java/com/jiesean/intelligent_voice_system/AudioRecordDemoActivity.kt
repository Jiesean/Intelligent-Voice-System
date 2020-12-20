package com.jiesean.intelligent_voice_system

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.jiesean.intelligent_voice_system.audiorecord.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import kotlin.concurrent.thread

public class AudioRecordDemoActivity : AppCompatActivity() {
    private var mAudioRecord: AudioRecord ? = null
    //回声消除
    private var mAEC: AcousticEchoCanceler ? = null

    //录音采集的来源，如系统mic、有线耳机mic等
    private var mAudioSource: Int = MediaRecorder.AudioSource.MIC
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

    private var mIsRecording: Boolean = false
    private var mSavedDataSize: Int = 0
    private val mStartRecordBtn : Button by lazy { findViewById<Button>(R.id.startRecordBtn) }
    private val mStopRecordBtn : Button by lazy { findViewById<Button>(R.id.stopRecordBtn) }
    private val mReadDateSize : TextView by lazy { findViewById<TextView>(R.id.textView2) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audiorecord_demo)

        mAudioRecord?.let {
            mAEC = AcousticEchoCanceler.create(it.audioSessionId)
            if (AcousticEchoCanceler.isAvailable()) {
                mAEC?.setEnabled(true)
            }
        }

        mStartRecordBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                try {
                    mAudioRecord = AudioRecord(mAudioSource, mAudioSampleRate, mAudioChanalConfig, mAudioBitDepth, mAudioBuffer)
                    mAudioRecord?.startRecording()
                }catch (ex: Exception){
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