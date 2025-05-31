package com.ramtinprg.model;

public enum WeaponType {
    REVOLVER("Revolver", 0.5f, 6, 1, 2f),
    SHOTGUN("Shotgun", 1f, 5, 5, 1.5f),
    SMG("SMG", 0.1f, 30, 1, 0.5f);

    public final String name;
    public final float fireRate; // Seconds between shots
    public final int maxAmmo;
    public final int bulletsPerShot;
    public final float reloadTime;

    private WeaponType(String name, float fireRate, int maxAmmo, int bulletsPerShot, float reloadTime) {
        this.name = name;
        this.fireRate = fireRate;
        this.maxAmmo = maxAmmo;
        this.bulletsPerShot = bulletsPerShot;
        this.reloadTime = reloadTime;
    }

    public String getName() {
        return name;
    }
}
