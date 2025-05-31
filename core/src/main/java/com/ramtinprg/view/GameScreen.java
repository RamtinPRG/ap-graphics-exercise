package com.ramtinprg.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ramtinprg.Main;
import com.ramtinprg.model.Bullet;
import com.ramtinprg.model.Player;
import com.ramtinprg.model.TileMap;
import com.ramtinprg.model.Tree;
import com.ramtinprg.model.Weapon;
import com.ramtinprg.model.WeaponType;

public class GameScreen implements Screen {

    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private SpriteBatch batch;

    private Player player;
    private TileMap tileMap;
    private Weapon weapon;
    private Array<Tree> trees;
    private Array<Bullet> bullets;

    private Texture bulletTexture;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, 1920, 1080);
        viewport = new FitViewport(1920, 1080, camera);
        // camera.setToOrtho(false, 1920, 1080); // TODO: remove hardcode
        camera.zoom = 0.5f;
        viewport.apply();
        // camera.position.set(1920 / 2f, 1080 / 2f, 0);
        shapeRenderer = new ShapeRenderer();

        batch = Main.getBatch();

        bulletTexture = new Texture("Images/Texture2D/EscToExit_back.png");

        tileMap = new TileMap(100, 100); // 100x100 tile grid
        trees = tileMap.generateTrees(200); // Randomly place 200 trees

        player = new Player(50 * TileMap.TILE_SIZE, 50 * TileMap.TILE_SIZE); // Center start
        weapon = new Weapon(WeaponType.REVOLVER);
        bullets = new Array<>();

        camera.position.set(player.getX(), player.getY(), 0);
    }

    @Override
    public void render(float delta) {
        handleInput(delta);

        player.update(delta);
        weapon.update(delta);
        System.out.println(player.getX() + " " + player.getY());
        for (Bullet bullet : bullets) {
            bullet.update(delta);
        }

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        uiCamera.update();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();
        tileMap.render(batch);
        for (Tree tree : trees) {
            tree.render(batch);
        }

        player.render(batch);

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        for (Bullet bullet : bullets) {
            bullet.render(shapeRenderer);
        }
        shapeRenderer.end();

        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        drawReloadBar(shapeRenderer, weapon);
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
        boolean isShooting = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

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
        player.setFacingRight(direction.x >= 0);

        if (isShooting /*&& player.canShoot()*/) {
            // Vector3 world = camera.unproject(new Vector3(mouse.x, mouse.y, 0));
            // bullets.add(new Bullet(new Vector2(player.getX(), player.getY()), direction));
            // player.resetShootTimer();
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
        bulletTexture.dispose();
        player.dispose();
        tileMap.dispose();
    }
}
