package com.ramtinprg.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Weapon {

    private WeaponType type;
    private float timeSinceLastShot = 0;
    private int currentAmmo;
    private boolean reloading = false;
    private float reloadTimer = 0;

    public Weapon(WeaponType type) {
        this.type = type;
        this.currentAmmo = type.maxAmmo;
    }

    public void update(float delta) {
        timeSinceLastShot += delta;
        if (reloading) {
            reloadTimer += delta;
            if (reloadTimer >= type.reloadTime) {
                currentAmmo = type.maxAmmo;
                reloading = false;
                reloadTimer = 0;
            }
        }
    }

    public boolean canShoot() {
        // System.out.println((timeSinceLastShot >= type.fireRate) + " " + (currentAmmo > 0) + " " + (!reloading));
        // System.out.println((timeSinceLastShot) + " " + (type.fireRate));
        return timeSinceLastShot >= type.fireRate && currentAmmo > 0 && !reloading;
    }

    public void shoot(Vector2 position, Vector2 target, Array<Bullet> bullets) {
        if (!canShoot()) {
            return;
        }

        timeSinceLastShot = 0;
        currentAmmo--;

        for (int i = 0; i < type.bulletsPerShot; i++) {
            Vector2 direction = new Vector2(target).sub(position).nor();
            if (type == WeaponType.SHOTGUN) {
                direction.rotateDeg((float) (Math.random() * 20 - 10)); // Spread
            }
            bullets.add(new Bullet(new Vector2(position), direction));
        }

        // System.out.println("bullets added");
    }

    public void reload() {
        if (!reloading && currentAmmo < type.maxAmmo) {
            reloading = true;
            reloadTimer = 0;
        }
    }

    public boolean isReloading() {
        return reloading;
    }

    public float getReloadTimer() {
        return reloadTimer;
    }

    public float getReloadTime() {
        return type.reloadTime;
    }
}
