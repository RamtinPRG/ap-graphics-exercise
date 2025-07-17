package com.ramtinprg.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramtinprg.Main;
import com.ramtinprg.model.GameAssetManager;

public class EndGameView implements Screen {

    private final Stage stage;
    private final Skin skin;
    private final boolean victory;
    private final String username;
    private final int score;
    private final float survivalTime;
    private final int kills;
    private final Image avatarImage;

    public EndGameView(Skin skin, boolean victory, int score, float survivalTime, int kills) {
        this.skin = skin;
        this.victory = victory;
        this.username = Main.getLoggedUser().getUsername();
        this.score = score;
        this.survivalTime = survivalTime;
        this.kills = kills;
        this.avatarImage = new Image(Main.getLoggedUser().getAvatarTexture());
        this.stage = new Stage(new ScreenViewport());

        Main.getLoggedUser().increaseScore(score);
        Main.getLoggedUser().increaseKills(kills);
        Main.getLoggedUser().increaseSurvivalTime(survivalTime);

        Gdx.input.setInputProcessor(stage);
        createUI();
    }

    private void createUI() {
        Table root = new Table();
        root.setFillParent(true);
        root.pad(20);
        stage.addActor(root);

        // Victory / Defeat Label
        Label resultLabel = new Label(victory ? "Victory!" : "Defeat", skin);
        resultLabel.setColor(victory ? Color.GREEN : new Color(253f / 255f, 81f / 255f, 97f / 255f, 1f));
        resultLabel.setFontScale(3.5f);
        root.add(resultLabel).colspan(2).center().padBottom(50).row();

        // Username and avatar
        Table userTable = new Table();
        Image avatar = new Image(avatarImage.getDrawable());
        float avatarWidth = avatar.getWidth();
        float avatarHeight = avatar.getHeight();
        float finalAvatarWidth = 128;
        float scalingFactor = finalAvatarWidth / avatarWidth;
        avatar.setSize(avatarWidth * scalingFactor, avatarHeight * scalingFactor);

        Label usernameLabel = new Label(username, skin);
        usernameLabel.setFontScale(1.2f);

        userTable.add(avatar).size(avatar.getWidth(), avatar.getHeight()).padRight(30);
        userTable.add(usernameLabel).left().padTop(10);

        root.add(userTable).colspan(2).padBottom(20).row();

        // Score label
        root.add(new Label("Score: " + score, skin)).left().colspan(2).padBottom(20).row();

        // Survival time
        root.add(new Label("Survived: " + String.format("%.1f", survivalTime) + "s", skin)).left().colspan(2)
                .padBottom(20).row();

        // Kills
        root.add(new Label("Kills: " + kills, skin)).left().colspan(2).padBottom(40).row();

        // Back to Main Menu Button
        TextButton backBtn = new TextButton("Back", skin);
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new MainMenuView(skin));
            }
        });
        root.add(backBtn).colspan(2).center();
    }

    @Override
    public void show() {
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
    }
}
