package com.ramtinprg.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public enum Hero {
    SHANA("Shana", new Texture(Gdx.files.internal("Images/Texture2D/T_Shana_Portrait.png")), 4, 4, "Heros/Shana/"),
    DIAMOND("Diamond", new Texture(Gdx.files.internal("Images/Texture2D/T_Diamond_Portrait.png")), 1, 7,
            "Heros/Diamond/"),
    SCARLET("Scarlet", new Texture(Gdx.files.internal("Images/Texture2D/T_Scarlett_Portrait.png")), 5, 3,
            "Heros/Scarlet/"),
    LILITH("Lilith", new Texture(Gdx.files.internal("Images/Texture2D/T_Lilith_Portrait.png")), 3, 5, "Heros/Lilith/"),
    DASHER("Dasher", new Texture(Gdx.files.internal("Images/Texture2D/T_Dasher_Portrait.png")), 10, 2, "Heros/Dasher/");

    private final String name;
    private final Texture texture;
    private final float speed;
    private final float maxHP;
    private final String animationPath;

    private Hero(String name, Texture texture, float speed, float maxHP, String animationPath) {
        this.name = name;
        this.texture = texture;
        this.speed = speed;
        this.maxHP = maxHP;
        this.animationPath = animationPath;
    }

    public String getName() {
        return name;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getSpeed() {
        return speed;
    }

    public float getMaxHP() {
        return maxHP;
    }

    public String getAnimationPath() {
        return animationPath;
    }
}
