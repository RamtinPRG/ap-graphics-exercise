package com.ramtinprg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ramtinprg.controller.MainMenuController;
import com.ramtinprg.model.GameAssetManager;
import com.ramtinprg.view.MainMenu;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {

    private static Main main = null;
    private static SpriteBatch batch;

    public static SpriteBatch getBatch() {
        return batch;
    }

    public static void setBatch(SpriteBatch batch) {
        Main.batch = batch;
    }

    @Override
    public void create() {
        main = this;
        batch = new SpriteBatch();
        getMain().setScreen(new MainMenu(new MainMenuController(), GameAssetManager.getInstance().getSkin()));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public static Main getMain() {
        return main;
    }
}
