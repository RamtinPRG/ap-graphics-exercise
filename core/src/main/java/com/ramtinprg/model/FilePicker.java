package com.ramtinprg.model;

import com.badlogic.gdx.files.FileHandle;

public interface FilePicker {
    void pickFile(FilePickedCallback callback);

    interface FilePickedCallback {
        void onFilePicked(FileHandle file);
    }
}