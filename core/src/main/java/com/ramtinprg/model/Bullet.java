package com.ramtinprg.model;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 position;
    private Vector2 velocity;
    private static final float SPEED = 500;

    public Bullet(Vector2 position, Vector2 direction) {
        this.position = position;
        this.velocity = direction.scl(SPEED);
    }

    public void update(float delta) {
        position.mulAdd(velocity, delta);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(position.x, position.y, 3);
    }

    public Vector2 getPosition() {
        return position;
    }
}

