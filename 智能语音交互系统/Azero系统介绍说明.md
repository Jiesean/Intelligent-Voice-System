# Android音频系统和只能语音服务文档

## 整体架构

## 硬件选型与结构设计

## 声学性能测试

## 录音

### 标准录音

### 蓝牙录音

1. 设置通话模式

   ```kotlin
   mAudioManager?.mode = AudioManager.MODE_IN_COMMUNICATION
   ```

2. 开启sco连接

   ```kotlin
   mAudioManager?.startBluetoothSco()
   ```

3. 监听sco连接广播

```kotlin
AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED -> {
    val state = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, AudioManager.SCO_AUDIO_STATE_ERROR)
    when(state){
        AudioManager.SCO_AUDIO_STATE_CONNECTED ->{
            bluetoothHeadetScoConnectionChanged(true)
        }
    }
}
```

4. 通知Audio sco已建立

```kotlin
 mAudioManager?.setBluetoothScoOn(false)
```



### 多麦克风阵列拾音



## 播放器

## 信号处理

### 唤醒

### 降噪

### AEC

### 波束形成

### 人声检测

## SDK状态机和对话管理

## AVS协议

## Vsi服务

## Skill

## NLP

## ASR

## TTS

## 应用及场景

### 免唤醒的技术方案

## 总结