package com.ramtinprg.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramtinprg.Main;
import com.ramtinprg.controller.MainMenuController;

public class MainMenu implements Screen {

    private Stage stage;
    private TextButton playButton;
    private Label title;
    private TextField username;
    private Table table;
    private MainMenuController controller;

    public MainMenu(MainMenuController controller, Skin skin) {
        this.controller = controller;
        this.playButton = new TextButton("Hello!", skin);
        this.title = new Label("welcome!", skin);
        this.username = new TextField("Enter your username", skin);
        this.table = new Table();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.center();
        table.add(title);
        table.row().pad(10, 0, 10, 0);
        table.add(username).width(600);
        table.row().pad(10, 0, 10, 0);
        table.add(playButton);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Main.getBatch().begin();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    }

}
