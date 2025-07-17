package com.ramtinprg.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramtinprg.Main;
import com.ramtinprg.model.GameAssetManager;

public class GuideView implements Screen {
    private final Skin skin;
    private Stage stage;
    private Table rootTable;

    private Preferences keyBindings;
    private int upKey;
    private int downKey;
    private int leftKey;
    private int rightKey;
    private int shootKey;
    private int reloadKey;
    private int autoAimKey;

    private Screen prevScreen;

    public GuideView(Skin skin, Screen prevScreen) {
        this.skin = skin;
        this.prevScreen = prevScreen;
    }

    @Override
    public void show() {
        keyBindings = Gdx.app.getPreferences("KeyBindings");
        upKey = keyBindings.getInteger("Move Up", Input.Keys.W);
        downKey = keyBindings.getInteger("Move Down", Input.Keys.S);
        leftKey = keyBindings.getInteger("Move Left", Input.Keys.A);
        rightKey = keyBindings.getInteger("Move Right", Input.Keys.D);
        shootKey = keyBindings.getInteger("Shoot", Input.Buttons.LEFT);
        reloadKey = keyBindings.getInteger("Reload", Input.Keys.R);
        autoAimKey = keyBindings.getInteger("Auto Aim", Input.Keys.W);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        rootTable = new Table();
        rootTable.pad(20).top();
        rootTable.defaults().pad(10).left().fillX();
        rootTable.setFillParent(false);

        ScrollPane scrollPane = new ScrollPane(rootTable, skin);
        Table container = new Table();
        container.setFillParent(true);
        container.add(scrollPane).expand().fill();

        stage.addActor(container);

        addSectionTitle("Controls");
        addLine("Move Up: " + keyToString(upKey));
        addLine("Move Left: " + keyToString(leftKey));
        addLine("Move Down: " + keyToString(downKey));
        addLine("Move Right: " + keyToString(rightKey));
        addLine("Reload: " + keyToString(reloadKey));
        addLine("Shoot: " + keyToString(shootKey));
        addLine("Auto-aim: " + keyToString(autoAimKey));

        addSectionTitle("Cheats");
        addLine("F1: Increases HP by 1");
        addLine("F2: Reduces remaining time by 1 minute");
        addLine("F3: Upgrades to next level immediately");

        addSectionTitle("Characters");
        addLine("Shana - Speed: 4, HP: 4");
        addLine("Diamond - Speed: 1, HP: 7");
        addLine("Scarlet - Speed: 5, HP: 3");
        addLine("Lilith - Speed: 3, HP: 5");
        addLine("Dasher - Speed: 10, HP: 2");

        addSectionTitle("Abilities");
        addLine("Vitality - Max HP increased by 1");
        addLine("Damager - Weapon damage +25% for 10 seconds");
        addLine("Procrease - Fires 1 extra projectile per shot");
        addLine("Amocrease - Max ammo increased by 5");
        addLine("Speedy - Speed x2 for 10 seconds");

        // TextButton backButton = new TextButton("Back to Menu", skin);
        // backButton.addListener(new ClickListener() {
        // @Override
        // public void clicked(InputEvent event, float x, float y) {
        // Main.getMain().setScreen(new MainMenuView(skin));
        // }
        // });

        // rootTable.row();
        // rootTable.add(backButton).padTop(30).left();
    }

    private void addSectionTitle(String text) {
        Label.LabelStyle titleStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        titleStyle.fontColor = new Color(253f / 255f, 81f / 255f, 97f / 255f, 1f);
        Label label = new Label(text, titleStyle);
        label.setFontScale(1.5f);
        rootTable.row();
        rootTable.add(label).padTop(20).left();
    }

    private void addLine(String text) {
        Label label = new Label(text, skin);
        rootTable.row();
        rootTable.add(label);
    }

    private static String keyToString(int code) {
        if (code <= 0) {
            int mouseButton = -code;
            switch (mouseButton) {
                case Input.Buttons.LEFT:
                    return "Left Click";
                case Input.Buttons.RIGHT:
                    return "Right Click";
                case Input.Buttons.MIDDLE:
                    return "Middle Click";
                default:
                    return "Mouse Button " + mouseButton;
            }
        } else {
            return Input.Keys.toString(code);
        }
    }

    @Override
    public void render(float delta) {
        stage.getBatch().begin();
        stage.getBatch().draw(GameAssetManager.getInstance().getBackgroundImage(), 0, 0, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Main.getMain().setScreen(prevScreen);
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
