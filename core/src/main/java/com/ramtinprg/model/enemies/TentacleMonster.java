package com.ramtinprg.model.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ramtinprg.model.Player;

public class TentacleMonster extends Enemy {

    public TentacleMonster(float x, float y) {
        super(x, y, 25, 70); // Example speed
        anim = loadAnimation("Enemies/TentacleMonster/", 0.2f);
        xpDropValue = 5;
    }

    @Override
    public void update(float delta, Player player) {
        stateTime += delta;
        Vector2 dir = new Vector2(player.getX() - x, player.getY() - y).nor();
        x += dir.x * speed * delta;
        y += dir.y * speed * delta;
        facingRight = dir.x >= 0;
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion frame = anim.getKeyFrame(stateTime, true);

        if ((!facingRight && !frame.isFlipX()) || (facingRight && frame.isFlipX())) {
            frame.flip(true, false);
        }

        batch.draw(frame, x - frame.getRegionWidth() / 2f, y - frame.getRegionHeight() / 2f);
    }
}
