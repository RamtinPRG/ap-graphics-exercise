package com.ramtinprg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ramtinprg.model.GameAssetManager;
import com.ramtinprg.model.User;
import com.ramtinprg.view.GameScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {

    private static Main main = null;
    private static SpriteBatch batch;

    private static ShaderProgram grayscaleShader;
    private static boolean grayscaleEnabled;

    private static User loggedUser = null;

    public static SpriteBatch getBatch() {
        return batch;
    }

    public static void setBatch(SpriteBatch batch) {
        Main.batch = batch;
    }

    public static void setGrayscaleEnabled(boolean grayscaleEnabled) {
        Main.grayscaleEnabled = grayscaleEnabled;
    }

    @Override
    public void create() {
        main = this;

        DisplayMode mode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(mode);

        // Load grayscale shader
        ShaderProgram.pedantic = false;
        ShaderProgram shader = new ShaderProgram(
                Gdx.files.internal("Shaders/default.vert"),
                Gdx.files.internal("Shaders/grayscale.frag")
        );

        if (!shader.isCompiled()) {
            System.err.println("Shader compile error:\n" + shader.getLog());
        } else {
            grayscaleShader = shader;
        }

        // Load setting
        Preferences prefs = Gdx.app.getPreferences("Settings");
        grayscaleEnabled = prefs.getBoolean("grayscale", false);

        GameAssetManager.getInstance().getBackgroundMusic().setLooping(true);
        GameAssetManager.getInstance().getBackgroundMusic().setVolume(1f);
        GameAssetManager.getInstance().getBackgroundMusic().play();

        batch = new SpriteBatch();
        // getMain().setScreen(new SignUpView(GameAssetManager.getInstance().getSkin()));
        getMain().setScreen(new GameScreen());
    }

    @Override
    public void render() {
        // System.out.println(grayscaleEnabled + " " + (grayscaleShader != null) + " " + grayscaleShader.isCompiled());
        if (grayscaleEnabled && grayscaleShader != null && grayscaleShader.isCompiled()) {
            batch.setShader(grayscaleShader);
        } else {
            batch.setShader(null);
        }

        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (grayscaleShader != null) {
            grayscaleShader.dispose();
        }
    }

    public static Main getMain() {
        return main;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(User loggedUser) {
        Main.loggedUser = loggedUser;
    }
}
