package com.ramtinprg.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public enum Hero {
    SHANA("Shana", new Texture(Gdx.files.internal("Images/Texture2D/T_Shana_Portrait.png"))),
    DIAMOND("Diamond", new Texture(Gdx.files.internal("Images/Texture2D/T_Diamond_Portrait.png"))),
    SCARLET("Scarlet", new Texture(Gdx.files.internal("Images/Texture2D/T_Scarlett_Portrait.png"))),
    LILITH("Lilith", new Texture(Gdx.files.internal("Images/Texture2D/T_Lilith_Portrait.png"))),
    DASHER("Dasher", new Texture(Gdx.files.internal("Images/Texture2D/T_Dasher_Portrait.png")));

    private final String name;
    private final Texture texture;

    private Hero(String name, Texture texture) {
        this.name = name;
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public Texture getTexture() {
        return texture;
    }
}
