package com.ramtinprg.model;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class DesktopFilePicker implements FilePicker {
    @Override
    public void pickFile(FilePickedCallback callback) {
        SwingUtilities.invokeLater(() -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select Avatar Image");
            chooser.setFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "jpeg"));
            int result = chooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                if (selected.exists()) {
                    FileHandle file = Gdx.files.absolute(selected.getAbsolutePath());
                    Gdx.app.postRunnable(() -> callback.onFilePicked(file)); // ✅ switch to OpenGL thread
                }
            }
        });
    }
}
