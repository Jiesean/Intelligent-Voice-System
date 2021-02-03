#!/usr/bin/env bash
cd ../src
~/Library/Android/sdk/ndk-bundle/ndk-build APP_BUILD_SCRIPT=Android.mk NDK_APPLICATION_MK=Application.mk NDK_PROJECT_PATH=.
cd ../build

#cp -f ../src/libs/armeabi-v7a/* ../../libs/armeabi-v7a/