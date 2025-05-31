package com.ramtinprg.model.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.ramtinprg.model.Player;

public abstract class Enemy {

    protected float x, y;
    protected int hp;
    protected float speed;
    protected Animation<TextureRegion> anim;
    protected float stateTime;
    protected boolean facingRight = true;

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

    public void damage(int dmg) {
        hp -= dmg;
    }

    public abstract void render(SpriteBatch batch);

    public Rectangle getBounds() {
        return new Rectangle(x, y, this.anim.getKeyFrame(stateTime).getRegionWidth(), this.anim.getKeyFrame(stateTime).getRegionHeight());
    }

    protected Animation<TextureRegion> loadAnimation(String folder, float frameDuration) {
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
}
