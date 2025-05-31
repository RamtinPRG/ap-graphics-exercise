package com.ramtinprg.model.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ramtinprg.model.Bullet;
import com.ramtinprg.model.Player;

public class EyeBat extends Enemy {

    private float shootTimer = 0;

    public EyeBat(float x, float y) {
        super(x, y, 50, 10); // Eyebat might be stationary or have its own movement
        anim = loadAnimation("Enemies/EyeBat/", 0.1f);
    }

    @Override
    public void update(float delta, Player player) {
    }

    public void update(float delta, Player player, Array<Bullet> bullets) {
        stateTime += delta;
        Vector2 dir = new Vector2(player.getX() - x, player.getY() - y).nor();
        x += dir.x * speed * delta;
        y += dir.y * speed * delta;
        facingRight = dir.x >= 0;
        shootTimer += delta;
        if (shootTimer >= 3f) {
            shootTimer = 0;
            shootAtPlayer(player, bullets);
        }
    }

    private void shootAtPlayer(Player player, Array<Bullet> bullets) {
        Vector2 dir = new Vector2(player.getX() - x, player.getY() - y).nor();
        Bullet bullet = new Bullet(new Vector2(x, y), dir, 100f, false);
        bullets.add(bullet);
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
