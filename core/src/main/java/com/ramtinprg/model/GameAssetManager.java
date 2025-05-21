package com.ramtinprg.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {
    private static GameAssetManager instance = null;

    private Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

    private GameAssetManager() {}

    public static GameAssetManager getInstance() {
        if (instance == null) {
            instance = new GameAssetManager();
        }
        return instance;
    }

    public Skin getSkin() {
        return skin;
    }
}
