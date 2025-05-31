package com.ramtinprg.model;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class TileMap {

    public static final int TILE_SIZE = 32;
    private Texture tileTexture;
    private int width, height;

    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;
        tileTexture = new Texture("Images/Texture2D/T_TileGrass.png");
    }

    public void render(SpriteBatch batch) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                batch.draw(tileTexture, x * TILE_SIZE, y * TILE_SIZE);
            }
        }
    }

    public Array<Tree> generateTrees(int count) {
        Array<Tree> result = new Array<>();
        Texture treeTex = new Texture("Images/Sprite/T_TreeMonster_0.png");
        Random rand = new Random();

        for (int i = 0; i < count; i++) {
            int x = rand.nextInt(width) * TILE_SIZE;
            int y = rand.nextInt(height) * TILE_SIZE;
            result.add(new Tree(treeTex, x, y));
        }
        return result;
    }

    public void dispose() {
        tileTexture.dispose();
    }
}
