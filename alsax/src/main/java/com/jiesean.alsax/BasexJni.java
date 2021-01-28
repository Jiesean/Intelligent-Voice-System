package com.jiesean.alsax;

public class BasexJni {

    public native int initBaseX(int time, String hw, int micNum, int channelNum, String channelMap,
                                int bit, int sampleRate, int micshift, int refshift, int mode,
                                int periodSize, int bufferSize, BasexJniCallback listener);

    public native int releaseBaseX();
}
