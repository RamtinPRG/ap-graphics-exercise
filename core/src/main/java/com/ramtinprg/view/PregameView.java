package com.ramtinprg.view;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramtinprg.Main;
import com.ramtinprg.model.GameAssetManager;
import com.ramtinprg.model.Hero;
import com.ramtinprg.model.WeaponType;

public class PregameView implements Screen {

    private final Stage stage;
    private final Skin skin;

    private ArrayList<Hero> heros;
    private int currentHeroIndex = 0;

    private Image heroImage;
    private Label heroLabel;

    private final ArrayList<WeaponType> weapons;
    private Label weaponLabel;
    private int currentWeaponIndex = 0;

    public PregameView(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport(), Main.getBatch());

        heros = new ArrayList<>();
        weapons = new ArrayList<>();

        heros.addAll(Arrays.asList(Hero.values()));
        weapons.addAll(Arrays.asList(WeaponType.values()));
    }

    private void updateHeroSelection() {
        Texture texture = heros.get(currentHeroIndex).getTexture();
        int width = texture.getWidth();
        int height = texture.getHeight();
        float finalImageHeight = 300;
        float scalingFactor = finalImageHeight / height;
        heroImage.setDrawable(new Image(texture).getDrawable());
        heroLabel.setText(heros.get(currentHeroIndex).getName());
        heroImage.setSize(width * scalingFactor, height * scalingFactor);
    }

    private void updateWeaponSelection() {
        weaponLabel.setText(weapons.get(currentWeaponIndex).getName());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // Left Button
        TextButton leftButton = new TextButton("<", skin);
        leftButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentHeroIndex = (currentHeroIndex - 1 + heros.size()) % heros.size();
                updateHeroSelection();
            }
        });

        // Right Button
        TextButton rightButton = new TextButton(">", skin);
        rightButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentHeroIndex = (currentHeroIndex + 1) % heros.size();
                updateHeroSelection();
            }
        });

        // Image and Label
        heroImage = new Image(heros.get(currentHeroIndex).getTexture());
        heroLabel = new Label(heros.get(currentHeroIndex).getName(), skin);
        heroImage.setAlign(Align.center);
        updateHeroSelection();

        // Navigation row
        Table heroTable = new Table();
        heroTable.add(leftButton).width(50);
        heroTable.add(heroImage).height(heroImage.getHeight()).pad(10);
        heroTable.add(rightButton).width(50);

        TextButton leftWeaponButton = new TextButton("<", skin);
        TextButton rightWeaponButton = new TextButton(">", skin);
        weaponLabel = new Label(weapons.get(currentWeaponIndex).getName(), skin);

        leftWeaponButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentWeaponIndex = (currentWeaponIndex - 1 + weapons.size()) % weapons.size();
                updateWeaponSelection();
            }
        });

        rightWeaponButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentWeaponIndex = (currentWeaponIndex + 1) % weapons.size();
                updateWeaponSelection();
            }
        });

        Table weaponTable = new Table();
        weaponTable.add(leftWeaponButton).width(50);
        weaponTable.add(weaponLabel).pad(10).width(200).center();
        weaponTable.add(rightWeaponButton).width(50);

        // Play Time SelectBox (can be replaced with another carousel if desired)
        SelectBox<String> timeSelectBox = new SelectBox<>(skin);
        timeSelectBox.setItems("2 minutes", "5 minutes", "10 minutes", "20 minutes");

        // Start Button
        TextButton startButton = new TextButton("Start Game", skin);
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // String selectedHero = heroNames[currentHeroIndex];
                int playTime = Integer.parseInt(timeSelectBox.getSelected().split(" ")[0]);

                // Main.getMain().setScreen(new GameScreen(game, selectedHero, "DefaultWeapon", playTime));
            }
        });

        // Layout
        table.add(new Label("Select Your Hero", skin)).colspan(3).padBottom(10);
        table.row();
        table.add(heroTable).colspan(3);
        table.row();
        table.add(heroLabel).colspan(3).padBottom(30);
        table.row();
        table.add().colspan(3).height(20); // Spacer
        table.row();

        table.add(new Label("Select Your Weapon", skin)).colspan(3).padBottom(10);
        table.row();
        table.add(weaponTable).colspan(3);
        table.row();
        table.add().colspan(3).height(20); // Spacer
        table.row();

        table.add(new Label("Select Play Time", skin)).colspan(3).padTop(20);
        table.row();
        table.add(timeSelectBox).width(200).colspan(3).pad(10);
        table.row();
        table.add(startButton).colspan(3).padTop(30);

        TextButton backButton = new TextButton("<", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new MainMenuView(skin)); // Replace with your main menu screen
            }
        });

        Table topBar = new Table();
        topBar.top().left();
        topBar.setFillParent(true);
        topBar.add(backButton).pad(50).left().top();

        stage.addActor(topBar);
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
