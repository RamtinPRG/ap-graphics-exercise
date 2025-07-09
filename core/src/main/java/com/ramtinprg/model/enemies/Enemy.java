package com.ramtinprg.model.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ramtinprg.model.Player;

public abstract class Enemy {

    protected float x, y;
    protected float hp;
    protected float speed;
    protected Animation<TextureRegion> anim;
    protected float stateTime;
    protected boolean facingRight = true;
    protected int xpDropValue = 1;

    public Enemy(float x, float y, int hp, float speed) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.speed = speed;
    }

    public abstract void update(float delta, Player player);

    public boolean isDead() {
        return hp <= 0;
    }

    public void decreaseHp(float dmg) {
        hp -= dmg;
    }

    public abstract void render(SpriteBatch batch);

    public Rectangle getBounds() {
        TextureRegion frame = this.anim.getKeyFrame(stateTime);
        return new Rectangle(x - frame.getRegionWidth() / 2f, y - frame.getRegionHeight() / 2f,
                frame.getRegionWidth(),
                frame.getRegionHeight());
    }

    protected Animation<TextureRegion> loadAnimation(String folder, float frameDuration) {
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

    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    public int getXpDropValue() {
        return xpDropValue;
    }
}
