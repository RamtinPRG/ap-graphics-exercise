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

    public GameScreen(Skin skin) {
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

        player = new Player(50 * TileMap.TILE_SIZE, 50 * TileMap.TILE_SIZE, 7); // Center start // TODO: remove hardcode
        weapon = new Weapon(WeaponType.SMG);
        bullets = new Array<>();
        xpDrops = new Array<>();

        enemies = new Array<>();
        enemySpawner = new EnemySpawner(2 * 60); // TODO: remove hardcode

        gameTimer = new GameTimer(2 * 60); // TODO: remove hardcode

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
                    Gdx.app.exit();
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
            gameTimer.update(delta);

            enemySpawner.update(delta, enemies, camera);
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
        font.getData().setScale(1.5f);
        font.draw(batch, "XP: ",
                uiCamera.position.x - uiCamera.viewportWidth / 2 + 20,
                uiCamera.position.y + uiCamera.viewportHeight / 2 - 80);
        font.setColor(253f / 255f, 225f / 255f, 81f / 255f, 1);
        font.draw(batch, String.valueOf(player.getXp()),
                uiCamera.position.x - uiCamera.viewportWidth / 2 + 210,
                uiCamera.position.y + uiCamera.viewportHeight / 2 - 80);
        font.setColor(1, 1, 1, 1);
        font.getData().setScale(1f);

        drawHealthPoints(batch, player);

        batch.end();

        uiStage.act(delta);
        uiStage.draw();
    }

    private void drawHealthPoints(SpriteBatch batch, Player player) {
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

        player.setValocity(valocity);
        player.setShooting(isShooting);
        player.setSpeed(isShooting ? 50f : 100f);

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
        batch.dispose();
        player.dispose();
        tileMap.dispose();
        uiStage.dispose();
    }
}
