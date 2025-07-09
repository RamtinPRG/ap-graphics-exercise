package com.ramtinprg.model;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {

    private Vector2 position;
    private Vector2 velocity;
    private final float radius = 3f;
    // private static final float SPEED = 500;
    private final boolean byPlayer;
    private final float damage;

    public Bullet(Vector2 position, Vector2 direction, float speed, boolean byPlayer, float damage) {
        this.position = position;
        this.velocity = direction.scl(speed);
        this.byPlayer = byPlayer;
        this.damage = damage;
    }

    public void update(float delta) {
        position.mulAdd(velocity, delta);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(position.x, position.y, radius);
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isByPlayer() {
        return byPlayer;
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x - radius, position.y - radius, radius * 2, radius * 2);
    }

    public float getDamage() {
        return damage;
    }
}
