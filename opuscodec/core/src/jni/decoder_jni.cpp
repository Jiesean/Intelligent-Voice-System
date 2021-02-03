//
// Created by Jiesean on 2021/2/3.
//
extern "C"
JNIEXPORT jint JNICALL
Java_com_jiesean_opuscodec_opus_decoder_Decoder__1initDecoder(JNIEnv *env, jobject thiz,
                                                              jobject parameter) {
    // TODO: implement _initDecoder()
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_jiesean_opuscodec_opus_decoder_Decoder__1decode(JNIEnv *env, jobject thiz, jshortArray pcm,
                                                         jbyteArray opus) {
    // TODO: implement _decode()
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_jiesean_opuscodec_opus_decoder_Decoder__1destroyDecoder(JNIEnv *env, jobject thiz) {
    // TODO: implement _destroyDecoder()
}