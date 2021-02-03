package com.jiesean.opuscodec.opus.decoder;


import com.jiesean.opuscodec.opus.Opus;
import com.jiesean.opuscodec.opus.Parameter;

/**
 * @author Wangjie
 * @description:
 * @date :2021/2/3 2:40 PM
 */
public class Decoder {
    public Decoder() {
        Opus.getInstance();
    }

    public int initDecoder(Parameter parameter) {
        return _initDecoder(parameter);
    }

    public int decode(short[] pcm, byte[] opus) {
        return _decode(pcm, opus);
    }

    public int destroyDecoder() {
        return _destroyDecoder();
    }

    private native int _initDecoder(Parameter parameter);

    private native int _decode(short[] pcm, byte[] opus);

    private native int _destroyDecoder();
}
