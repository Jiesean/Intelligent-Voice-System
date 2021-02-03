#!/usr/bin/env bash
# Mirror
javah -o ../core/src/jni/decoder_jni.h -classpath ../build/intermediates/classes/release/ ../../src/com.jiesean.opuscodec.opus.encoder.Decoder
javah -o ../core/src/jni/encoder_jni.h -classpath ../build/intermediates/classes/release/ ../../src/com.jiesean.opuscodec.opus.encoder.Encoder