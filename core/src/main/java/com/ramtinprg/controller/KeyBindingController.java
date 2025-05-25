package com.ramtinprg.controller;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

public class KeyBindingController {
    private static final String PREF_NAME = "KeyBindings";

    public static void saveBindings(Map<String, Integer> keyBindings) {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        for (Map.Entry<String, Integer> entry : keyBindings.entrySet()) {
            prefs.putInteger(entry.getKey(), entry.getValue());
        }
        prefs.flush();
    }

    public static void loadBindings(Map<String, Integer> keyBindings, Map<String, Integer> defaultBindings) {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        for (Map.Entry<String, Integer> entry : defaultBindings.entrySet()) {
            int key = prefs.getInteger(entry.getKey(), entry.getValue());
            keyBindings.put(entry.getKey(), key);
        }
    }

    public static String keyToString(int code) {
        if (code <= 0) {
            int mouseButton = -code;
            switch (mouseButton) {
                case Input.Buttons.LEFT: return "Left Click";
                case Input.Buttons.RIGHT: return "Right Click";
                case Input.Buttons.MIDDLE: return "Middle Click";
                default: return "Mouse Button " + mouseButton;
            }
        } else {
            return Input.Keys.toString(code);
        }
    }
}
