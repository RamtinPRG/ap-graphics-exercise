package com.ramtinprg.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ramtinprg.Main;
import com.ramtinprg.model.Bullet;
import com.ramtinprg.model.EnemySpawner;
import com.ramtinprg.model.GameAssetManager;
import com.ramtinprg.model.GameTimer;
import com.ramtinprg.model.Hero;
import com.ramtinprg.model.Player;
import com.ramtinprg.model.TileMap;
import com.ramtinprg.model.Tree;
import com.ramtinprg.model.Weapon;
import com.ramtinprg.model.WeaponType;
import com.ramtinprg.model.XPDrop;
import com.ramtinprg.model.enemies.Enemy;
import com.ramtinprg.model.enemies.EyeBat;

public class GameScreen implements Screen {

    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Viewport uiViewport;
    private SpriteBatch batch;

    private Player player;
    private TileMap tileMap;
    private Weapon weapon;
    private Array<Tree> trees;
    private Array<Bullet> bullets;
    private Array<XPDrop> xpDrops;

    private Array<Enemy> enemies;
    private EnemySpawner enemySpawner;

    private GameTimer gameTimer;

    private Stage uiStage;
    private Table pauseMenu;
    private boolean paused = false;
    private boolean initiatedUI = false;
    private Skin skin;
    private InputMultiplexer multiplexer;

    public GameScreen(Skin skin, Hero hero, WeaponType weaponType, int duration) {
        this.skin = skin;
        camera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, 1920, 1080);
        viewport = new FitViewport(1920, 1080, camera);
        uiViewport = new ScreenViewport(uiCamera);
        // camera.setToOrtho(false, 1920, 1080); // TODO: remove hardcode
        camera.zoom = 0.5f;
        viewport.apply();
        // camera.position.set(1920 / 2f, 1080 / 2f, 0);
        shapeRenderer = new ShapeRenderer();

        batch = Main.getBatch();

        tileMap = new TileMap(100, 100); // 100x100 tile grid
        trees = tileMap.generateTrees(200); // Randomly place 200 trees

        player = new Player(50 * TileMap.TILE_SIZE, 50 * TileMap.TILE_SIZE, hero); // Center start // TODO: remove
                                                                                   // hardcode
        weapon = new Weapon(weaponType);
        bullets = new Array<>();
        xpDrops = new Array<>();

        enemies = new Array<>();
        enemySpawner = new EnemySpawner(duration * 60);

        gameTimer = new GameTimer(duration * 60);

        camera.position.set(player.getX(), player.getY(), 0);

        // Pause Menu UI
        uiStage = new Stage(uiViewport);
    }

    @Override
    public void show() {
        if (!initiatedUI) {
            // Dim Background
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            Texture blackPixel = new Texture(pixmap);
            pixmap.dispose();

            Image dimBackground = new Image(new TextureRegionDrawable(new TextureRegion(blackPixel)));
            dimBackground.setColor(0, 0, 0, 0.5f);
            dimBackground.setFillParent(true);
            dimBackground.setVisible(false);

            pauseMenu = new Table();
            pauseMenu.setFillParent(true);
            pauseMenu.setVisible(false);

            Label pauseLabel = new Label("Paused", skin);
            TextButton resumeButton = new TextButton("Resume", skin);
            TextButton settingsButton = new TextButton("Settings", skin);
            TextButton quitButton = new TextButton("Quit", skin);

            resumeButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    paused = false;
                    pauseMenu.setVisible(false);
                    dimBackground.setVisible(false);
                }
            });

            settingsButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Main.getMain().setScreen(new SettingsView(skin, GameScreen.this));
                }
            });

            quitButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    // Main.getMain().setScreen(new MainMenuView(skin));
                    int score = (int) gameTimer.getPassedTime() * player.getKills();
                    Main.getMain()
                            .setScreen(
                                    new EndGameView(skin, false, score, gameTimer.getPassedTime(),
                                            player.getKills()));
                }
            });

            pauseMenu.add(pauseLabel).padBottom(20).row();
            pauseMenu.add(resumeButton).pad(10).row();
            pauseMenu.add(settingsButton).pad(10).row();
            pauseMenu.add(quitButton).pad(10);

            uiStage.addActor(dimBackground);
            uiStage.addActor(pauseMenu);

            multiplexer = new InputMultiplexer();
            multiplexer.addProcessor(uiStage);
            multiplexer.addProcessor(new InputAdapter() {
                @Override
                public boolean keyDown(int keycode) {
                    if (keycode == Input.Keys.ESCAPE) {
                        paused = !paused;
                        pauseMenu.setVisible(paused);
                        dimBackground.setVisible(paused);
                        return true;
                    }
                    return false;
                }
            });

            initiatedUI = true;
        }
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        handleInput(delta);

        if (!paused) {

            // Check for game ending
            int score = (int) gameTimer.getPassedTime() * player.getKills();
            if (player.getHp() <= 0) {
                Main.getMain()
                        .setScreen(new EndGameView(skin, false, score, gameTimer.getPassedTime(), player.getKills()));
            }
            if (gameTimer.isFinished()) {
                Main.getMain()
                        .setScreen(new EndGameView(skin, true, score, gameTimer.getPassedTime(), player.getKills()));
            }

            gameTimer.update(delta);
            enemySpawner.update(delta, enemies, camera);

            if (player.isLevelingUp()) {
                openSkillSelectionView();
            }
            player.update(delta);
            weapon.update(delta);
            for (Tree tree : trees) {
                tree.update(delta);
            }
            // System.out.println(player.getX() + " " + player.getY());
            for (Bullet bullet : bullets) {
                if (bullet.isByPlayer()) {
                    for (Enemy enemy : enemies) {
                        if (enemy.getBounds().overlaps(bullet.getBounds())) {
                            // enemies.removeValue(enemy, true);
                            enemy.decreaseHp(bullet.getDamage());
                            if (enemy.isDead()) {
                                player.incrementKills(1);
                                xpDrops.add(new XPDrop(enemy.getPosition(), enemy.getXpDropValue()));
                                enemies.removeValue(enemy, true);
                            }
                            bullets.removeValue(bullet, true);
                            break;
                        }
                    }
                } else {
                    if (player.getBounds().overlaps(bullet.getBounds())) {
                        player.decreaseHp(bullet.getDamage());
                        bullets.removeValue(bullet, true);
                    }
                }
            }

            for (XPDrop xpDrop : xpDrops) {
                if (player.getBounds().overlaps(xpDrop.getBounds())) {
                    player.increaseXp(xpDrop.getValue());
                    xpDrops.removeValue(xpDrop, true);
                }
            }

            for (Enemy enemy : enemies) {
                if (enemy.getBounds().overlaps(player.getBounds())) {
                    player.decreaseHp(1);
                    enemies.removeValue(enemy, true);
                }
            }

            for (Bullet bullet : bullets) {
                bullet.update(delta);
            }

            for (Enemy enemy : enemies) {
                if (enemy != null) {
                    if (enemy instanceof EyeBat) {
                        EyeBat eyeBat = (EyeBat) enemy;
                        eyeBat.update(delta, player, bullets);
                        continue;
                    }
                    enemy.update(delta, player);
                }
            }

            camera.position.set(player.getX(), player.getY(), 0);
            camera.update();

            uiCamera.update();
        }

        // Rendering Game Elements
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();
        tileMap.render(batch);
        for (Tree tree : trees) {
            tree.render(batch);
        }
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (XPDrop xpDrop : xpDrops) {
            shapeRenderer.setColor(Color.YELLOW);
            xpDrop.render(shapeRenderer);
        }
        shapeRenderer.end();

        batch.begin();
        player.render(batch);
        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Bullet bullet : bullets) {
            shapeRenderer.setColor(Color.GRAY);
            if (!bullet.isByPlayer()) {
                shapeRenderer.setColor(253f / 255f, 81f / 255f, 97f / 255f, 1);
            }
            bullet.render(shapeRenderer);
        }
        shapeRenderer.end();

        // Rendering UI Elements
        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        drawReloadBar(shapeRenderer, weapon);
        drawXPBar(shapeRenderer);

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        BitmapFont font = GameAssetManager.getInstance().getGameFont();
        font.getData().setScale(1.5f);
        font.draw(batch, "Time Left: ",
                uiCamera.position.x - uiCamera.viewportWidth / 2 + 20,
                uiCamera.position.y + uiCamera.viewportHeight / 2 - 35);
        font.setColor(253f / 255f, 81f / 255f, 97f / 255f, 1);
        font.draw(batch, gameTimer.getFormattedTime(),
                uiCamera.position.x - uiCamera.viewportWidth / 2 + 210,
                uiCamera.position.y + uiCamera.viewportHeight / 2 - 35);
        font.setColor(1, 1, 1, 1);
        font.getData().setScale(1f);

        // font = GameAssetManager.getInstance().getGameFont();
        // font.getData().setScale(1.5f);
        // font.draw(batch, "XP: ",
        // uiCamera.position.x - uiCamera.viewportWidth / 2 + 20,
        // uiCamera.position.y + uiCamera.viewportHeight / 2 - 80);
        // font.setColor(253f / 255f, 81f / 255f, 224f / 255f, 1);
        // font.draw(batch, String.valueOf(player.getXp()),
        // uiCamera.position.x - uiCamera.viewportWidth / 2 + 210,
        // uiCamera.position.y + uiCamera.viewportHeight / 2 - 80);
        // font.setColor(1, 1, 1, 1);
        // font.getData().setScale(1f);
        drawLevelData(batch);
        drawKills(batch);
        drawAmmos(batch);
        drawHealthPoints(batch);

        batch.end();

        uiStage.act(delta);
        uiStage.draw();
    }

    private void openSkillSelectionView() {
        player.setLevelingUp(false);
        Main.getMain().setScreen(new SkillSelectionView(skin, this, player));
    }

    private void drawLevelData(SpriteBatch batch) {
        BitmapFont font = GameAssetManager.getInstance().getGameFont();
        font.getData().setScale(1.5f);
        font.draw(batch, "XP: ",
                uiCamera.position.x - uiCamera.viewportWidth / 2 + 20,
                uiCamera.position.y + uiCamera.viewportHeight / 2 - 80);
        font.setColor(253f / 255f, 81f / 255f, 224f / 255f, 1);
        font.draw(batch, String.valueOf(player.getXp()),
                uiCamera.position.x - uiCamera.viewportWidth / 2 + 210,
                uiCamera.position.y + uiCamera.viewportHeight / 2 - 80);
        font.setColor(1, 1, 1, 1);

        font.draw(batch, "Level: ",
                uiCamera.position.x - uiCamera.viewportWidth / 2 + 20,
                uiCamera.position.y + uiCamera.viewportHeight / 2 - 125);
        font.setColor(161f / 255f, 81f / 255f, 253f / 255f, 1);
        font.draw(batch, String.valueOf(player.getLevel()),
                uiCamera.position.x - uiCamera.viewportWidth / 2 + 210,
                uiCamera.position.y + uiCamera.viewportHeight / 2 - 125);

        font.setColor(1, 1, 1, 1);
        font.getData().setScale(1f);
    }

    private void drawXPBar(ShapeRenderer shapeRenderer) {
        int xp = player.getXp();
        int level = player.getLevel();
        float levelProgress = (float) (xp - 10 * level * (level - 1)) / (float) (20 * level);
        float barWidth = 250;
        float barHeight = 30;
        float x = uiCamera.position.x - uiCamera.viewportWidth / 2 + 20;
        float y = uiCamera.position.y + uiCamera.viewportHeight / 2 - 200;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Background
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, barWidth, barHeight);

        // Foreground - progress
        shapeRenderer.setColor(161f / 255f, 81f / 255f, 253f / 255f, 1);
        shapeRenderer.rect(x, y, barWidth * Math.min(levelProgress, 1f), barHeight);
        // shapeRenderer.rect(uiCamera.position.x - uiCamera.viewportWidth / 2 + 10,
        // uiCamera.position.y, barWidth,
        // barHeight);

        shapeRenderer.end();
    }

    private void drawKills(SpriteBatch batch) {
        BitmapFont font = GameAssetManager.getInstance().getGameFont();
        font.getData().setScale(1.5f);
        font.draw(batch, "Kills: ",
                uiCamera.position.x - uiCamera.viewportWidth / 2 + 20,
                uiCamera.position.y + uiCamera.viewportHeight / 2 - 245);
        font.setColor(81f / 255f, 87f / 255f, 253f / 255f, 1);
        font.draw(batch, String.valueOf(player.getKills()),
                uiCamera.position.x - uiCamera.viewportWidth / 2 + 210,
                uiCamera.position.y + uiCamera.viewportHeight / 2 - 245);

        font.setColor(1, 1, 1, 1);
        font.getData().setScale(1f);
    }

    private void drawAmmos(SpriteBatch batch) {
        BitmapFont font = GameAssetManager.getInstance().getGameFont();
        font.getData().setScale(1.5f);
        font.draw(batch, "Ammos: ",
                uiCamera.position.x - uiCamera.viewportWidth / 2 + 20,
                uiCamera.position.y + uiCamera.viewportHeight / 2 - 290);
        font.setColor(81f / 255f, 161f / 255f, 253f / 255f, 1);
        font.draw(batch, String.valueOf(weapon.getCurrentAmmo()),
                uiCamera.position.x - uiCamera.viewportWidth / 2 + 210,
                uiCamera.position.y + uiCamera.viewportHeight / 2 - 290);

        font.setColor(1, 1, 1, 1);
        font.getData().setScale(1f);
    }

    private void drawHealthPoints(SpriteBatch batch) {
        Texture heart = new Texture(Gdx.files.internal("Images/Sprite/HeartAnimation_1.png"));
        Texture brokenHeart = new Texture(Gdx.files.internal("Images/Sprite/HeartAnimation_3.png"));
        for (int i = 0; i < player.getMaxHp(); i++) {
            Texture toDraw;
            if (i < player.getHp()) {
                toDraw = heart;
            } else {
                toDraw = brokenHeart;
            }
            float width = toDraw.getWidth();
            float height = toDraw.getHeight();
            float scalingFactor = 1.5f;
            batch.draw(toDraw,
                    uiCamera.position.x - uiCamera.viewportWidth / 2 + 150 + i * 45,
                    40, width * scalingFactor, height * scalingFactor);
        }
    }

    private void drawReloadBar(ShapeRenderer shapeRenderer, Weapon weapon) {
        if (!weapon.isReloading()) {
            return;
        }

        float reloadProgress = weapon.getReloadTimer() / weapon.getReloadTime();
        float barWidth = 200;
        float barHeight = 20;
        float x = Gdx.graphics.getWidth() / 2f - barWidth / 2f;
        float y = 50; // pixels from bottom

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Background
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, barWidth, barHeight);

        // Foreground - progress
        shapeRenderer.setColor(Color.LIME);
        shapeRenderer.rect(x, y, barWidth * Math.min(reloadProgress, 1f), barHeight);

        shapeRenderer.end();
    }

    private void handleInput(float delta) {
        if (paused)
            return;

        // Handling cheat codes
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            player.increaseHp(1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            gameTimer.update(60);
            enemySpawner.update(60, enemies, camera);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            int nextLevel = player.getLevel() + 1;
            player.increaseXp((nextLevel - 1) * nextLevel * 10 - player.getXp());
        }

        boolean shootingPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean isShooting = shootingPressed && !weapon.isReloading();

        Vector2 valocity = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            valocity.y += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            valocity.y -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            valocity.x -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            valocity.x += 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            weapon.reload();
        }

        float referenceSpeed = player.getReferenceSpeed();
        player.setValocity(valocity);
        player.setShooting(isShooting);
        player.setSpeed(isShooting ? (referenceSpeed / 2) : referenceSpeed);

        Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mouse);
        Vector2 direction = mouse.cpy().sub(new Vector2(player.getX(), player.getY()));
        player.setFacingRight((direction.x >= 0 && isShooting) || (!isShooting && valocity.x >= 0));

        if (shootingPressed /* && player.canShoot() */) {
            weapon.shoot(new Vector2(player.getX(), player.getY()), mouse, bullets);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        // batch.dispose();
        player.dispose();
        tileMap.dispose();
        uiStage.dispose();
    }
}
