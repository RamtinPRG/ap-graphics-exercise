package com.ramtinprg.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramtinprg.Main;
import com.ramtinprg.controller.MainMenuController;
import com.ramtinprg.model.GameAssetManager;
import com.ramtinprg.model.User;

public class MainMenuView implements Screen {

    private Stage stage;
    private final Skin skin;
    private final MainMenuController controller;

    public MainMenuView(Skin skin) {
        this.skin = skin;
        this.controller = new MainMenuController(this);
    }

    @Override
    public void show() {
        User user = Main.getLoggedUser();

        stage = new Stage(new ScreenViewport(), Main.getBatch());
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        // root.top().padTop(20);
        stage.addActor(root);

        Texture logoTexture = GameAssetManager.getInstance().getLogoTexture();
        Image logo = new Image(logoTexture);
        float scalingFactor = 1.5f;
        logo.setSize(logoTexture.getWidth() * scalingFactor, logoTexture.getHeight() * scalingFactor);
        root.add(logo).colspan(2).width(logo.getWidth()).height(logo.getHeight()).padBottom(50);
        root.row();

        Table userInfo = new Table();
        userInfo.defaults().pad(5);
        userInfo.right().top().padRight(20);

        Label usernameLabel = new Label(user.getUsername(), skin);
        usernameLabel.setFontScale(1.2f);
        Image avatar = new Image(Main.getLoggedUser().getAvatarTexture());
        float avatarWidth = avatar.getWidth();
        float avatarHeight = avatar.getHeight();
        float finalAvatarWidth = 96;
        scalingFactor = finalAvatarWidth / avatarWidth;
        avatar.setSize(avatarWidth * scalingFactor, avatarHeight * scalingFactor);
        TextButton logoutButton = new TextButton("Logout", skin);

        userInfo.add(avatar).size(avatar.getWidth(), avatar.getHeight()).padRight(15);
        userInfo.add(usernameLabel).right().padRight(50);
        userInfo.add(logoutButton);

        root.add(userInfo).expandX().center().padBottom(50);
        root.row();

        String[] buttonNames = { "New Game", "Continue", "Scoreboard", "Settings", "Profile", "Hint", "Exit" };
        for (String name : buttonNames) {
            TextButton button = new TextButton(name, skin);
            // button.getLabel().setFontScale(1.2f);
            button.pad(5);
            // button.getStyle().up = skin.newDrawable("default-round",
            // Color.valueOf("2c3e50"));
            // button.getStyle().over = skin.newDrawable("default-round",
            // Color.valueOf("34495e"));
            // button.getStyle().down = skin.newDrawable("default-round",
            // Color.valueOf("1c2833"));
            root.add(button).width(300).height(65).colspan(2).pad(5);
            root.row();

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    handleMenuClick(name);
                }
            });
        }

        logoutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("User logged out");
                controller.logout();
            }
        });
    }

    private void handleMenuClick(String name) {
        switch (name) {
            case "New Game":
                Main.getMain().setScreen(new PregameView(skin));
                break;
            case "Continue":
                // Load saved game
                break;
            case "Scoreboard":
                // game.setScreen(new ScoreboardScreen(game));
                break;
            case "Settings":
                Main.getMain().setScreen(new SettingsView(skin, this));
                break;
            case "Profile":
                Main.getMain().setScreen(new ProfileView(Main.getLoggedUser(), skin));
                break;
            case "Hint":
                // Show hint pop-up
                break;
            case "Exit":
                Gdx.app.exit();
                break;
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
        skin.dispose();
    }
}
