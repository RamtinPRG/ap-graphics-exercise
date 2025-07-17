package com.ramtinprg;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ramtinprg.model.GameAssetManager;
import com.ramtinprg.model.User;
import com.ramtinprg.view.EndGameView;
import com.ramtinprg.view.GameScreen;
import com.ramtinprg.view.SignUpView;
import com.ramtinprg.view.SkillSelectionView;

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

    private void startingRoutine() {
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<User>>() {
        }.getType();
        try (FileReader reader = new FileReader("users.json")) {
            ArrayList<User> users = gson.fromJson(reader, userListType);

            for (User user : users) {
                User.register(user.getUsername(), user.getPassword(), user.getSecurityQuestion(),
                        user.getSecurityQuestionAnswer(), user.getAvatarFilePath(), user.getScore(), user.getKills(),
                        user.getSurvivalTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void endingRoutine() {
        ArrayList<User> users = User.getRegisteredUsers();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("users.json")) {
            gson.toJson(users, writer);
            System.out.println("User saved to user.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create() {
        main = this;

        startingRoutine();

        DisplayMode mode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(mode);

        // Cursor
        Pixmap cursorPixmap = GameAssetManager.getInstance().getCursorPixmap();
        Gdx.graphics.setCursor(
                Gdx.graphics.newCursor(cursorPixmap, cursorPixmap.getWidth() / 2, cursorPixmap.getHeight() / 2));
        cursorPixmap.dispose();

        // Load grayscale shader
        ShaderProgram.pedantic = false;
        ShaderProgram shader = new ShaderProgram(
                Gdx.files.internal("Shaders/default.vert"),
                Gdx.files.internal("Shaders/grayscale.frag"));

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
        // getMain().setScreen(new
        // EndGameScreen(GameAssetManager.getInstance().getSkin(), true, 50, 100, 340));
        getMain().setScreen(new SignUpView(GameAssetManager.getInstance().getSkin()));
        // getMain().setScreen(new
        // GameScreen(GameAssetManager.getInstance().getSkin()));
        // getMain().setScreen(new
        // SkillSelectionView(GameAssetManager.getInstance().getSkin()));
    }

    @Override
    public void render() {
        // System.out.println(grayscaleEnabled + " " + (grayscaleShader != null) + " " +
        // grayscaleShader.isCompiled());
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
        endingRoutine();
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
