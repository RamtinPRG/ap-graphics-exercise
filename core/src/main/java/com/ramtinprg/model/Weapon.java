package com.ramtinprg.model;

public enum Weapon {
    REVOLVER("Revolver"),
    SHOTGUN("Shotgun"),
    SMG("SMG");

    final String name;

    private Weapon(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
