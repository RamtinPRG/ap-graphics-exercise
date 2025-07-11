package com.ramtinprg.controller;

import com.ramtinprg.Main;
import com.ramtinprg.model.GameAssetManager;
import com.ramtinprg.model.Hero;
import com.ramtinprg.model.WeaponType;
import com.ramtinprg.view.GameScreen;
import com.ramtinprg.view.PregameView;

public class PregameController {

    private final PregameView view;

    public PregameController(PregameView view) {
        this.view = view;
    }

    public void handleSubmit(Hero hero, WeaponType weaponType, int duration) {
        Main.getMain().setScreen(new GameScreen(GameAssetManager.getInstance().getSkin(), hero, weaponType, duration));
    }

    public PregameView getView() {
        return view;
    }
}