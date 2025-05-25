package com.ramtinprg.controller;

import com.ramtinprg.Main;
import com.ramtinprg.model.GameAssetManager;
import com.ramtinprg.view.LoginView;
import com.ramtinprg.view.MainMenuView;

public class MainMenuController {

    private final MainMenuView view;

    public MainMenuController(MainMenuView view) {
        this.view = view;
    }

    public void logout() {
        Main.getMain().setLoggedUser(null);
        Main.getMain().setScreen(new LoginView(GameAssetManager.getInstance().getSkin()));
    }
}
