package com.jiesean.opuscodec.opus.encoder;

import com.jiesean.opuscodec.opus.Opus;
import com.jiesean.opuscodec.opus.Parameter;

/**
 * @author Wangjie
 * @description:
 * @date :2021/2/3 2:40 PM
 */
public class Encoder {
    public Encoder() {
        Opus.getInstance();
    }

    public int initEncoder(Parameter parameter) {
        return _initEncoder(parameter);
    }

    public int encode(byte[] opus, short[] pcm) {
        return _encode(opus, pcm);
    }

    public int destroyEncoder() {
        return _destroyEncoder();
    }

    private native int _initEncoder(Parameter parameter);

    private native int _encode(byte[] opus, short[] pcm);

    private native int _destroyEncoder();

}
