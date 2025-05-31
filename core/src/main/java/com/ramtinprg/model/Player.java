package com.ramtinprg.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player {
    
    private float x, y;
    private float speed = 100f;
    private boolean isShooting;
    private Vector2 valocity = new Vector2();
    private float shootCooldown = 0.2f, shootTimer = 0;

    private Animation<TextureRegion> idleAnim, walkAnim, runAnim;
    private float stateTime;
    private boolean facingRight = true;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;

        idleAnim = loadAnimation("Heros/Diamond/idle/", 0.2f);
        walkAnim = loadAnimation("Heros/Diamond/walk/", 0.2f);
        runAnim = loadAnimation("Heros/Diamond/run/", 0.2f);
    }

    public void setValocity(Vector2 velocity) {
        this.valocity.set(velocity);
    }

    public void setShooting(boolean shooting) {
        this.isShooting = shooting;
    }

    public boolean canShoot() {
        return shootTimer <= 0;
    }

    public void resetShootTimer() {
        shootTimer = shootCooldown;
    }

    public void update(float delta) {
        shootTimer -= delta;
        stateTime += delta;

        if (!valocity.isZero()) {
            valocity.nor().scl(speed * delta);
            x += valocity.x;
            y += valocity.y;
        }
    }

    public void render(SpriteBatch batch) {
        Animation<TextureRegion> currentAnim;
        if (valocity.isZero()) {
            currentAnim = idleAnim;
        } else {
            currentAnim = isShooting ? walkAnim : runAnim;
        }

        TextureRegion frame = currentAnim.getKeyFrame(stateTime, true);

        if ((!facingRight && !frame.isFlipX()) || (facingRight && frame.isFlipX())) {
            frame.flip(true, false);
        }

        batch.draw(frame, x - frame.getRegionWidth() / 2f, y - frame.getRegionHeight() / 2f);
    }

    private Animation<TextureRegion> loadAnimation(String folder, float frameDuration) {
        FileHandle dir = Gdx.files.internal(folder);
        FileHandle[] files = dir.list();
        // Arrays.sort(files, Comparator.comparing(FileHandle::name)); // Ensure consistent order

        System.out.println("Loading animation from: " + folder + files.length + dir.isDirectory());
        for (FileHandle file : files) {
            System.out.println("Found file: " + file.name());
        }

        TextureRegion[] frames = new TextureRegion[files.length];
        for (int i = 0; i < files.length; i++) {
            frames[i] = new TextureRegion(new Texture(files[i]));
        }
        return new Animation<>(frameDuration, frames);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void dispose() {
        // Dispose textures from animations if needed
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
