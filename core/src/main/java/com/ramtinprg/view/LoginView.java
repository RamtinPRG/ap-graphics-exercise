package com.ramtinprg.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramtinprg.Main;
import com.ramtinprg.controller.LoginController;
import com.ramtinprg.model.GameAssetManager;

public class LoginView implements Screen {

    private Stage stage;
    private final Skin skin;
    private TextField usernameField;
    private TextField passwordField;
    private Label errorLabel;

    private final LoginController controller;

    public LoginView(Skin skin) {
        this.skin = skin;
        this.controller = new LoginController(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport(), Main.getBatch());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        usernameField = new TextField("", skin);
        passwordField = new TextField("", skin);
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);

        TextButton submitButton = new TextButton("Login", skin);
        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);
        errorLabel.setWrap(true);
        errorLabel.setAlignment(Align.center);

        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                System.out.println(password);
                controller.handleSubmit(username, password);
            }
        });

        table.add(GameAssetManager.getInstance().getLogoImage()).colspan(2).center().padBottom(20);
        table.row().padBottom(50);

        table.add(new Label("Username:", skin)).right().pad(10);
        table.add(usernameField).width(300).left().pad(10);
        table.row();

        table.add(new Label("Password:", skin)).right().pad(10);
        table.add(passwordField).width(300).left().pad(10);
        table.row();

        table.add(new Label("Press F4 to register", skin)).padTop(15).center().colspan(2);
        table.row();

        table.add(submitButton).width(150).padTop(15).center().colspan(2);
        table.row();

        table.add(errorLabel).width(500).padTop(5).center().colspan(2);

        stage.addActor(table);
    }

    public void showError(String msg) {
        errorLabel.setText(msg);
    }

    public void clearError() {
        errorLabel.setText("");
    }

    public void openSignUpView() {
        Main.getMain().setScreen(new SignUpView(skin));
    }

    public void openMainMenuView() {
        Main.getMain().setScreen(new MainMenuView(skin));
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
            openSignUpView();
        }

        stage.getBatch().begin();
        stage.getBatch().draw(GameAssetManager.getInstance().getBackgroundImage(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
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
