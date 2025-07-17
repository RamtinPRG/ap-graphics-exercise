package com.ramtinprg.view;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramtinprg.Main;
import com.ramtinprg.model.GameAssetManager;
import com.ramtinprg.model.User;

public class ScoreboardView implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final List<User> allUsers;
    private final String currentUsername;

    private Table rowsTable;
    private Table table;

    float[] colWidths = { 200, 200, 200, 200, 200 };

    private enum SortField {
        SCORE, KILLS, TIME, USERNAME
    }

    private SortField currentSort = SortField.SCORE;

    public ScoreboardView(Skin skin) {
        this.skin = skin;
        this.allUsers = new ArrayList<>(User.getRegisteredUsers());
        this.currentUsername = Main.getLoggedUser().getUsername();
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        createUI();
    }

    private void createUI() {
        table = new Table(skin);
        table.setFillParent(true);
        stage.addActor(table);

        rowsTable = new Table(skin);
        for (int i = 0; i < colWidths.length; i++) {
            rowsTable.columnDefaults(i).width(colWidths[i]).pad(5);
        }

        Label title = new Label("Top 10 Players", skin);
        table.add(title).colspan(5).padBottom(20).row();
        addHeader();
        table.add(rowsTable).colspan(5).center().row();
        sortAndRenderRows();
    }

    private void addHeader() {
        TextButton rankBtn = new TextButton("Rank", skin);
        TextButton usernameBtn = new TextButton("Username", skin);
        TextButton scoreBtn = new TextButton("Score", skin);
        TextButton killsBtn = new TextButton("Kills", skin);
        TextButton timeBtn = new TextButton("Time", skin);

        table.add(rankBtn).width(colWidths[0]).pad(5);
        table.add(usernameBtn).width(colWidths[1]).pad(5);
        table.add(scoreBtn).width(colWidths[2]).pad(5);
        table.add(killsBtn).width(colWidths[3]).pad(5);
        table.add(timeBtn).width(colWidths[4]).pad(5);
        table.row();

        // Sort buttons
        usernameBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                currentSort = SortField.USERNAME;
                sortAndRenderRows();
            }
        });
        scoreBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                currentSort = SortField.SCORE;
                sortAndRenderRows();
            }
        });
        killsBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                currentSort = SortField.KILLS;
                sortAndRenderRows();
            }
        });
        timeBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                currentSort = SortField.TIME;
                sortAndRenderRows();
            }
        });
    }

    private void sortAndRenderRows() {
        rowsTable.clear(); // Clears all rows safely

        // Sort users
        allUsers.sort((a, b) -> {
            switch (currentSort) {
                case USERNAME:
                    return a.getUsername().compareToIgnoreCase(b.getUsername());
                case KILLS:
                    return Integer.compare(b.getKills(), a.getKills());
                case TIME:
                    return Float.compare(b.getSurvivalTime(), a.getSurvivalTime());
                case SCORE:
                default:
                    return Integer.compare(b.getScore(), a.getScore());
            }
        });

        int limit = Math.min(10, allUsers.size());
        for (int i = 0; i < limit; i++) {
            User user = allUsers.get(i);
            boolean isTop3 = i < 3;
            boolean isCurrent = user.getUsername().equals(currentUsername);

            LabelStyle style = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
            style.fontColor = Color.WHITE;
            if (isTop3)
                style.fontColor = new Color(1f, 0.8f - 0.3f * i, 0.1f, 1f);
            else if (isCurrent)
                style.fontColor = Color.CYAN;

            rowsTable.add(new Label(String.valueOf(i + 1), style)).center();
            rowsTable.add(new Label(user.getUsername(), style)).center();
            rowsTable.add(new Label(String.valueOf(user.getScore()), style)).center();
            rowsTable.add(new Label(String.valueOf(user.getKills()), style)).center();
            rowsTable.add(new Label(String.format("%.1f", user.getSurvivalTime()), style)).center();
            rowsTable.row();
        }
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Main.getMain().setScreen(new MainMenuView(skin)); // or however you initialize your main menu
            dispose(); // optional if you want to clean up
            return;
        }

        stage.getBatch().begin();
        stage.getBatch().draw(GameAssetManager.getInstance().getBackgroundImage(), 0, 0, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    // Boilerplate:
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
