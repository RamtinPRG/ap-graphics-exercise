package com.ramtinprg.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class GameAssetManager {

    private static GameAssetManager instance = null;

    private final Skin skin;
    private final Texture logoTexture = new Texture(Gdx.files.internal("Images/Sprite/T_20Logo.png"));
    private final Image logoImage = new Image(logoTexture);
    private final Texture[] avatarsTextures = {
        new Texture(Gdx.files.internal("Avatars/1.png")),
        new Texture(Gdx.files.internal("Avatars/2.png")),
        new Texture(Gdx.files.internal("Avatars/3.png")),
        new Texture(Gdx.files.internal("Avatars/4.png")),
        new Texture(Gdx.files.internal("Avatars/5.png")),
        new Texture(Gdx.files.internal("Avatars/6.png")),
        new Texture(Gdx.files.internal("Avatars/7.png")),
        new Texture(Gdx.files.internal("Avatars/8.png")),
        new Texture(Gdx.files.internal("Avatars/9.png")),
        new Texture(Gdx.files.internal("Avatars/10.png")),
        new Texture(Gdx.files.internal("Avatars/11.png")),
        new Texture(Gdx.files.internal("Avatars/12.png"))
    };
    private final Image[] avatars = new Image[]{
        new Image(avatarsTextures[0]),
        new Image(avatarsTextures[1]),
        new Image(avatarsTextures[2]),
        new Image(avatarsTextures[3]),
        new Image(avatarsTextures[4]),
        new Image(avatarsTextures[5]),
        new Image(avatarsTextures[6]),
        new Image(avatarsTextures[7]),
        new Image(avatarsTextures[8]),
        new Image(avatarsTextures[9]),
        new Image(avatarsTextures[10]),
        new Image(avatarsTextures[11])
    };
    private final Texture backgroundImage = new Texture(Gdx.files.internal("background.png"));

    private final Music[] trackList = new Music[]{
        Gdx.audio.newMusic(Gdx.files.internal("Musics/Pretty Dungeon Loop.wav")),
        Gdx.audio.newMusic(Gdx.files.internal("Musics/Wasteland Combat Loop.wav")),};

    private Music backgroundMusic = trackList[0];

    private final BitmapFont gameFont;

    private GameAssetManager() {
        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Font/ChevyRay - Lantern.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        BitmapFont newFont = generator.generateFont(parameter);
        gameFont = newFont;
        generator.dispose();
        for (Label.LabelStyle style : skin.getAll(Label.LabelStyle.class).values()) {
            style.font = newFont;
        }
        for (TextButton.TextButtonStyle style : skin.getAll(TextButton.TextButtonStyle.class).values()) {
            style.font = newFont;
        }
        for (CheckBox.CheckBoxStyle style : skin.getAll(CheckBox.CheckBoxStyle.class).values()) {
            style.font = newFont;
        }
        for (TextField.TextFieldStyle style : skin.getAll(TextField.TextFieldStyle.class).values()) {
            style.font = newFont;
        }
        for (List.ListStyle style : skin.getAll(List.ListStyle.class).values()) {
            style.font = newFont;
        }
        for (SelectBox.SelectBoxStyle style : skin.getAll(SelectBox.SelectBoxStyle.class).values()) {
            style.font = newFont;
        }
        for (Window.WindowStyle style : skin.getAll(Window.WindowStyle.class).values()) {
            style.titleFont = newFont;
        }
    }

    public static GameAssetManager getInstance() {
        if (instance == null) {
            instance = new GameAssetManager();
        }
        return instance;
    }

    public Skin getSkin() {
        return skin;
    }

    public Texture getLogoTexture() {
        return logoTexture;
    }

    public Image getLogoImage() {
        return logoImage;
    }

    public Image[] getAvatars() {
        return avatars;
    }

    public Texture getBackgroundImage() {
        return backgroundImage;
    }

    public Music getBackgroundMusic() {
        return backgroundMusic;
    }

    public void setBackgroundMusic(Music backgroundMusic) {
        Preferences prefs = Gdx.app.getPreferences("Settings");
        this.backgroundMusic.stop();
        this.backgroundMusic = backgroundMusic;
        this.backgroundMusic.setVolume(prefs.getFloat("musicVolume", 1f));
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.play();
    }

    public Music[] getTrackList() {
        return trackList;
    }

    public BitmapFont getGameFont() {
        return gameFont;
    }
}
