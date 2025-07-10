package com.ramtinprg.model;

import com.badlogic.gdx.graphics.Texture;

public enum Skills {
    VITALITY("Vitality", GameAssetManager.getInstance().getVitalityImage(),
            "Increases max HP by 1."),
    DAMAGER("Damager", GameAssetManager.getInstance().getDamagerImage(),
            "Increases weapon damage by 25% for 10s."),
    PROCREASE("Procrease", GameAssetManager.getInstance().getProcreaseImage(),
            "Increases number of weapon's projectiles by 1."),
    AMOCREASE("Amocrease", GameAssetManager.getInstance().getAmocreaseImage(),
            "Increases max number of ammos by 5."),
    SPEEDY("Speedy", GameAssetManager.getInstance().getSpeedyImage(),
            "2x speed for 10s.");

    private final String name;
    private final Texture texture;
    private final String description;

    Skills(String name, Texture texture, String description) {
        this.name = name;
        this.texture = texture;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Texture getTexture() {
        return texture;
    }

    public String getDescription() {
        return description;
    }
}
