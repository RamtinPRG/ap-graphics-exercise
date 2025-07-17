package com.ramtinprg.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Weapon {

    private WeaponType type;
    private float timeSinceLastShot = 0;
    private int currentAmmo;
    private boolean reloading = false;
    private float reloadTimer = 0;

    private float reloadTime;
    private int maxAmmo;
    private float fireRate;
    private int bulletsPerShot;
    private int damage;

    private float damagePowerUpCountDown = 0;
    private float damagePowerUpRatio = 1;
    // private boolean damagePowerUpEnabled = false;

    public Weapon(WeaponType type) {
        this.type = type;
        this.currentAmmo = type.maxAmmo;
        this.maxAmmo = type.maxAmmo;
        this.reloadTime = type.reloadTime;
        this.fireRate = type.fireRate;
        this.bulletsPerShot = type.bulletsPerShot;
        this.damage = type.damage;
    }

    public void update(float delta) {
        if (damagePowerUpCountDown > 0) {
            damagePowerUpCountDown -= delta;
        }
        timeSinceLastShot += delta;
        if (reloading) {
            reloadTimer += delta;
            if (reloadTimer >= reloadTime) {
                currentAmmo = maxAmmo;
                reloading = false;
                reloadTimer = 0;
            }
        }
    }

    public boolean canShoot() {
        // System.out.println((timeSinceLastShot >= type.fireRate) + " " + (currentAmmo
        // > 0) + " " + (!reloading));
        // System.out.println((timeSinceLastShot) + " " + (type.fireRate));
        return timeSinceLastShot >= fireRate && currentAmmo > 0 && !reloading;
    }

    public void shoot(Vector2 position, Vector2 target, Array<Bullet> bullets) {
        if (!canShoot()) {
            if (currentAmmo == 0 && !reloading) {
                reload();
            }
            return;
        }

        timeSinceLastShot = 0;
        currentAmmo--;

        for (int i = 0; i < bulletsPerShot; i++) {
            Vector2 direction = new Vector2(target).sub(position).nor();
            // if (type == WeaponType.SHOTGUN) {
            if (i > 0) {
                direction.rotateDeg((float) (Math.random() * 20 - 10)); // Spread
            }
            bullets.add(new Bullet(new Vector2(position), direction, 500f, true,
                    damagePowerUpCountDown >= 0 ? damage * damagePowerUpRatio : damage));
        }

        // System.out.println("bullets added");
    }

    public void reload() {
        if (!reloading && currentAmmo < maxAmmo) {
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

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    public int getBulletsPerShot() {
        return bulletsPerShot;
    }

    public void setBulletsPerShot(int bulletsPerShot) {
        this.bulletsPerShot = bulletsPerShot;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void powerUpDamage(float duration, float ratio) {
        this.damagePowerUpCountDown = duration;
        this.damagePowerUpRatio = ratio;
    }

}
