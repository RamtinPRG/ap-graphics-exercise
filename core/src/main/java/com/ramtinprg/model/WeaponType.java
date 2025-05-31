package com.ramtinprg.model;

public enum WeaponType {
    REVOLVER("Revolver", 0.5f, 6, 1, 1f),
    SHOTGUN("Shotgun", 1f, 2, 5, 1f),
    SMG("SMG", 0.1f, 24, 1, 2f);

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
