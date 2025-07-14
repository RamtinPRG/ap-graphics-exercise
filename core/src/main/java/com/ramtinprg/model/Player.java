package com.ramtinprg.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {

    private float x, y;
    private float referenceSpeed;
    private float speed;
    private boolean isShooting;
    private Vector2 valocity = new Vector2();
    private float shootCooldown = 0.2f, shootTimer = 0;

    private Animation<TextureRegion> idleAnim, walkAnim, runAnim;
    private float stateTime;
    private boolean facingRight = true;

    private float maxHp;
    private float hp;
    private int xp = 0;
    private boolean levelingUp = false;

    private int kills = 0;

    public boolean isLevelingUp() {
        return levelingUp;
    }

    public void setLevelingUp(boolean levelUp) {
        this.levelingUp = levelUp;
    }

    public int getXp() {
        return xp;
    }

    public void increaseXp(int xp) {
        int level = getLevel();
        this.xp += xp;
        int newLevel = getLevel();
        if (newLevel > level) {
            levelingUp = true;
        }
    }

    public void increaseHp(int hp) {
        if (this.hp < maxHp) {
            this.hp += Math.min(hp, maxHp - this.hp);
        }
    }

    public Player(float x, float y, Hero hero) {
        this.x = x;
        this.y = y;
        this.maxHp = hero.getMaxHP();
        this.hp = maxHp;
        this.referenceSpeed = hero.getSpeed() * 25;

        idleAnim = loadAnimation(hero.getAnimationPath() + "idle/", 0.2f);
        walkAnim = loadAnimation(hero.getAnimationPath() + "walk/", 0.2f);
        runAnim = loadAnimation(hero.getAnimationPath() + "run/", 0.2f);
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

    public Rectangle getBounds() {
        Animation<TextureRegion> currentAnim;
        if (valocity.isZero()) {
            currentAnim = idleAnim;
        } else {
            currentAnim = isShooting ? walkAnim : runAnim;
        }

        TextureRegion frame = currentAnim.getKeyFrame(stateTime, true);
        return new Rectangle(x - frame.getRegionWidth() / 2f, y - frame.getRegionHeight() / 2f, frame.getRegionWidth(),
                frame.getRegionHeight());
    }

    private Animation<TextureRegion> loadAnimation(String folder, float frameDuration) {
        FileHandle dir = Gdx.files.internal(folder);
        FileHandle[] files = dir.list();
        // Arrays.sort(files, Comparator.comparing(FileHandle::name)); // Ensure
        // consistent order

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

    public float getReferenceSpeed() {
        return referenceSpeed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getHp() {
        return hp;
    }

    public void decreaseHp(float amount) {
        this.hp -= Math.min(amount, this.hp);
    }

    public float getMaxHp() {
        return maxHp;
    }

    public int getLevel() {
        int x = 1;
        float xp = this.xp;
        while (xp >= x * 20) {
            xp -= x * 20;
            x++;
        }
        return x;
    }

    public void incrementKills(int amount) {
        this.kills += amount;
    }

    public int getKills() {
        return kills;
    }
}
