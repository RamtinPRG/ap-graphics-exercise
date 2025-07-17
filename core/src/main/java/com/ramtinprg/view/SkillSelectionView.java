package com.ramtinprg.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramtinprg.Main;
import com.ramtinprg.model.GameAssetManager;
import com.ramtinprg.model.Player;
import com.ramtinprg.model.Skills;
import com.ramtinprg.model.Weapon;

public class SkillSelectionView implements Screen {

    private final Stage stage;
    private final Skin skin;
    private final Screen prevScreen;
    private final Player player;
    private final Weapon weapon;

    public SkillSelectionView(Skin skin, Screen prevScreen, Player player, Weapon weapon) {
        this.skin = skin;
        this.prevScreen = prevScreen;
        this.player = player;
        this.weapon = weapon;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        createUI();
    }

    private void createUI() {
        List<Skills> randomSkills = getRandomSkills(3);

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.pad(30);
        stage.addActor(root);

        Label.LabelStyle titleStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        titleStyle.fontColor = new Color(253f / 255f, 81f / 255f, 97f / 255f, 1f);
        Label titleLabel = new Label("Level " + player.getLevel(), titleStyle);
        titleLabel.setFontScale(3.5f); // optional: make it bigger
        root.add(titleLabel).padBottom(40).center().row();

        // Row for all skills side by side
        Table skillRow = new Table();
        skillRow.defaults().space(30).pad(10);
        skillRow.center();

        for (Skills skill : randomSkills) {
            Texture texture = skill.getTexture();

            skillRow.add(createSkillEntry(skill, texture)).expand().fill();
        }

        root.add(skillRow).center();
    }

    private Table createSkillEntry(Skills skill, Texture texture) {
        Image image = new Image(texture);
        image.setScaling(Scaling.fit);
        image.setSize(100, 100);

        Label.LabelStyle titleStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        titleStyle.fontColor = new Color(253f / 255f, 81f / 255f, 97f / 255f, 1f);
        Label titleLabel = new Label(skill.getName(), titleStyle);
        titleLabel.setFontScale(1.5f);

        Label.LabelStyle descriptionStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        descriptionStyle.fontColor = new Color(245f / 255f, 214f / 255f, 193f / 255f, 1f);
        Label descriptionLabel = new Label(skill.getDescription(), descriptionStyle);
        descriptionLabel.setFontScale(1.2f);
        descriptionLabel.setWrap(true);

        Table textTable = new Table();
        textTable.add(titleLabel).center().padBottom(20).row();
        textTable.add(descriptionLabel).width(240).center();

        Table contentTable = new Table();
        contentTable.top();
        contentTable.add(image).size(100).padBottom(20).row();
        contentTable.add(textTable).center().expandX();

        // Container to add background and click listener
        Container<Table> container = new Container<>(contentTable);
        // container.background("skill-bg"); // Must exist in skin (or use below
        // fallback)
        container.setBackground(createColoredBackground(new Color(0.2f, 0.2f, 0.25f, 1f)));
        container.pad(20);
        container.align(Align.center);
        container.fill();

        // Click listener
        container.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // Gdx.app.log("Skill Clicked", skill.getName());
                player.skills.put(skill, player.skills.get(skill) + 1);
                switch (skill) {
                    case AMOCREASE:
                        weapon.setMaxAmmo(weapon.getMaxAmmo() + 5);
                        break;
                    case DAMAGER:
                        weapon.powerUpDamage(10f, 1.25f);
                        break;
                    case PROCREASE:
                        weapon.setBulletsPerShot(weapon.getBulletsPerShot() + 1);
                        break;
                    case SPEEDY:
                        player.speedUp(10f, 2f);
                        break;
                    case VITALITY:
                        player.increaseMaxHP(1);
                        break;
                }
                Main.getMain().setScreen(prevScreen);
            }
        });

        return new Table().add(container).center().expand().fill().getTable();
    }

    private List<Skills> getRandomSkills(int count) {
        List<Skills> allSkills = new ArrayList<>(Arrays.asList(Skills.values()));
        Collections.shuffle(allSkills);
        return allSkills.subList(0, Math.min(count, allSkills.size()));
    }

    private Drawable createColoredBackground(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        Texture texture = new Texture(pixmap);
        pixmap.dispose(); // safe to dispose pixmap after creating texture

        TextureRegion region = new TextureRegion(texture);
        return new TextureRegionDrawable(region);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.08f, 0.08f, 0.08f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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