package com.ramtinprg.view;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramtinprg.Main;
import com.ramtinprg.controller.KeyBindingController;
import com.ramtinprg.model.GameAssetManager;

public class KeyBindingView extends ScreenAdapter {

    private final Stage stage;
    private final Skin skin;
    private final Map<String, Integer> keyBindings;
    private String awaitingKey = null;
    private Label infoLabel;

    public KeyBindingView(Skin skin, Map<String, Integer> keyBindings) {
        this.skin = skin;
        this.keyBindings = keyBindings;
        this.stage = new Stage(new ScreenViewport(), Main.getBatch());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (awaitingKey != null) {
                    keyBindings.put(awaitingKey, keycode);
                    KeyBindingController.saveBindings(keyBindings);
                    awaitingKey = null;
                    Main.getMain().setScreen(new KeyBindingView(skin, keyBindings));
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (awaitingKey != null) {
                    keyBindings.put(awaitingKey, -button); // Store mouse as negative
                    KeyBindingController.saveBindings(keyBindings);
                    awaitingKey = null;
                    Main.getMain().setScreen(new KeyBindingView(skin, keyBindings));
                    return true;
                }
                return false;
            }
        }));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        infoLabel = new Label("Click a control, then press a key or button.", skin);

        table.add(infoLabel).width(500).colspan(2).padBottom(20).row();

        for (Map.Entry<String, Integer> entry : keyBindings.entrySet()) {
            final String action = entry.getKey();
            int value = entry.getValue();
            final TextButton button = new TextButton(KeyBindingController.keyToString(value), skin);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    awaitingKey = action;
                    infoLabel.setText("Press a key for: " + action);
                }
            });

            table.add(new Label(action, skin)).left().padBottom(10);
            table.add(button).width(200).padBottom(10).row();
        }

        TextButton back = new TextButton("Back", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new SettingsView(skin));
            }
        });

        table.add(back).colspan(2).padTop(30);
    }

    @Override
    public void render(float delta) {
        stage.getBatch().begin();
        stage.getBatch().draw(GameAssetManager.getInstance().getBackgroundImage(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
