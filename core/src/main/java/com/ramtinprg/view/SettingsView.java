package com.ramtinprg.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramtinprg.Main;
import com.ramtinprg.model.GameAssetManager;

public class SettingsView extends ScreenAdapter {

    private Stage stage;
    private final Skin skin;
    private final Preferences prefs;
    private final Preferences keyBindingsPref;

    private final String[] musicTracks = {"Pretty Dungeon", "Wasteland Combat"};
    private final Map<String, Integer> keyBindings = new LinkedHashMap<>();

    public SettingsView(Skin skin) {
        this.skin = skin;
        prefs = Gdx.app.getPreferences("Settings");
        keyBindingsPref = Gdx.app.getPreferences("KeyBindings");

        // Default keys
        keyBindings.put("Move Up", keyBindingsPref.getInteger("Move Up", Input.Keys.W));
        keyBindings.put("Move Down", keyBindingsPref.getInteger("Move Down", Input.Keys.S));
        keyBindings.put("Move Left", keyBindingsPref.getInteger("Move Left", Input.Keys.A));
        keyBindings.put("Move Right", keyBindingsPref.getInteger("Move Right", Input.Keys.D));
        keyBindings.put("Shoot", keyBindingsPref.getInteger("Shoot", -Input.Buttons.LEFT));
        keyBindings.put("Auto Aim", keyBindingsPref.getInteger("Auto Aim", Input.Keys.SPACE));
        keyBindings.put("Reload", keyBindingsPref.getInteger("Reload", Input.Keys.R));
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport(), Main.getBatch());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Music volume
        Label volumeLabel = new Label("Music Volume", skin);
        final Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        volumeSlider.setValue(prefs.getFloat("musicVolume", 1f));

        volumeSlider.addListener(event -> {
            GameAssetManager.getInstance().getBackgroundMusic().setVolume(volumeSlider.getValue());
            prefs.putFloat("musicVolume", volumeSlider.getValue());
            prefs.flush();
            return false;
        });

        // Music dropdown
        Label musicLabel = new Label("Music Track", skin);
        final SelectBox<String> musicSelectBox = new SelectBox<>(skin);
        musicSelectBox.setItems(musicTracks);
        musicSelectBox.setSelected(prefs.getString("musicTrack", musicTracks[0]));

        musicSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int index = musicSelectBox.getSelectedIndex();
                GameAssetManager.getInstance().setBackgroundMusic(GameAssetManager.getInstance().getTrackList()[index]);
                prefs.putString("musicTrack", musicSelectBox.getSelected());
                prefs.flush();
            }
        });

        // SFX toggle
        final CheckBox sfxCheckbox = new CheckBox(" Enable SFX", skin);
        sfxCheckbox.setChecked(prefs.getBoolean("sfx", true));
        sfxCheckbox.addListener(event -> {
            prefs.putBoolean("sfx", sfxCheckbox.isChecked());
            prefs.flush();
            return false;
        });

        // Auto Reload
        final CheckBox autoReloadCheckbox = new CheckBox(" Auto Reload", skin);
        autoReloadCheckbox.setChecked(prefs.getBoolean("autoReload", true));
        autoReloadCheckbox.addListener(event -> {
            prefs.putBoolean("autoReload", autoReloadCheckbox.isChecked());
            prefs.flush();
            return false;
        });

        // Grayscale
        final CheckBox grayscaleCheckbox = new CheckBox(" Grayscale Mode", skin);
        grayscaleCheckbox.setChecked(prefs.getBoolean("grayscale", false));
        grayscaleCheckbox.addListener(event -> {
            Main.setGrayscaleEnabled(grayscaleCheckbox.isChecked());
            prefs.putBoolean("grayscale", grayscaleCheckbox.isChecked());
            prefs.flush();
            return false;
        });

        // Key Binding Button
        TextButton keyBindingBtn = new TextButton("Edit Key Bindings", skin);
        keyBindingBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new KeyBindingView(skin, keyBindings));
            }
        });

        // Back button
        TextButton backBtn = new TextButton("Back", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new MainMenuView(skin));
            }
        });

        // Layout
        table.add(volumeLabel).left().padRight(20);
        table.add(volumeSlider).width(200).padBottom(10).row();

        table.add(musicLabel).left().padRight(20);
        table.add(musicSelectBox).width(200).padBottom(10).row();

        table.add(sfxCheckbox).colspan(2).left().padBottom(10).row();
        table.add(autoReloadCheckbox).colspan(2).left().padBottom(10).row();
        table.add(grayscaleCheckbox).colspan(2).left().padBottom(10).row();

        table.add(keyBindingBtn).width(300).padTop(20).colspan(2).row();
        table.add(backBtn).width(300).padTop(10).colspan(2).row();
    }

    @Override
    public void render(float delta) {
        stage.getBatch().begin();
        stage.getBatch().draw(GameAssetManager.getInstance().getBackgroundImage(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        // Main.getBatch().begin();
        // Main.getBatch().draw(GameAssetManager.getInstance().getBackgroundImage(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Main.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
