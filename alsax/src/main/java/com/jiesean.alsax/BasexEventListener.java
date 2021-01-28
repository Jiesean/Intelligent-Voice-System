package com.jiesean.alsax;

public interface BasexEventListener {
    void onSaveData(byte[] data, int size);

    void onRecordStart();

    void onRecordEnd();
}
