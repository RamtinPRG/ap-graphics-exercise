package com.ramtinprg.model;

import java.util.Random;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class XPDrop {
    private Vector2 position;
    private final float radius = new Random().nextFloat(1, 5);
    private final int value;

    public XPDrop(Vector2 position, int value) {
        this.position = position;
        this.value = value;
    }

    // public void update(float delta) {
    // }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(position.x, position.y, radius);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x - radius, position.y - radius, radius * 2, radius * 2);
    }

    public int getValue() {
        return value;
    }
}
