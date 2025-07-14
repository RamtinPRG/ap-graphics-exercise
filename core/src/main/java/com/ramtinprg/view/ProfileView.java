package com.ramtinprg.view;

import java.util.regex.Pattern;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramtinprg.Main;
import com.ramtinprg.model.DesktopFilePicker;
import com.ramtinprg.model.FilePicker;
import com.ramtinprg.model.GameAssetManager;
import com.ramtinprg.model.User;

public class ProfileView implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final User user;
    private final FilePicker filePicker;

    private Texture avatarTexture;
    private Image avatarImage;
    private Label errorLabel, usernameLabel;
    private TextField usernameField, passwordField;

    private final String[] predefinedAvatars = GameAssetManager.getInstance().getAvatarsFilesPath();

    public ProfileView(User user, Skin skin) {
        this.user = user;
        this.skin = skin;
        this.filePicker = new DesktopFilePicker();
        this.stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        // Gdx.input.setInputProcessor(new InputMultiplexer(stage, new
        // DragDropListener()));
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        root.pad(20);
        stage.addActor(root);

        Label titleLabel = new Label("Profile", skin);

        // avatarImage = new Image(Main.getLoggedUser().getAvatarTexture());
        avatarTexture = Main.getLoggedUser().getAvatarTexture();
        avatarImage = new Image();
        int width = avatarTexture.getWidth();
        int height = avatarTexture.getHeight();
        float finalImageHeight = 128;
        float scalingFactor = finalImageHeight / height;
        avatarImage.setDrawable(new Image(avatarTexture).getDrawable());
        // heroLabel.setText(heros.get(currentHeroIndex).getName());
        avatarImage.setSize(width * scalingFactor, height * scalingFactor);
        // avatarImage.setSize(100, 100);

        usernameLabel = new Label("Username: " + user.getUsername(), skin);
        usernameField = new TextField("", skin);
        usernameField.setMessageText("New username");

        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText("New password");

        TextButton usernameBtn = new TextButton("Change Username", skin);
        TextButton passwordBtn = new TextButton("Change Password", skin);
        TextButton deleteAccountBtn = new TextButton("Delete Account", skin);
        TextButton backBtn = new TextButton("Back", skin);

        SelectBox<String> avatarSelect = new SelectBox<>(skin);
        avatarSelect.setItems(predefinedAvatars);
        TextButton applyAvatarBtn = new TextButton("Apply Selected Avatar", skin);
        TextButton uploadAvatarBtn = new TextButton("Upload Avatar", skin);

        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);

        root.add(titleLabel).colspan(2).center().padBottom(20).row();
        root.add(avatarImage).size(avatarImage.getWidth(), avatarImage.getHeight()).pad(10);
        root.add(avatarSelect).width(200).pad(10).row();
        root.add(applyAvatarBtn).colspan(2).pad(5).row();
        root.add(uploadAvatarBtn).colspan(2).pad(5).row();
        root.add(usernameLabel).colspan(2).padTop(20).row();
        root.add(usernameField).colspan(2).width(300).pad(5).row();
        root.add(usernameBtn).colspan(2).pad(5).row();
        root.add(passwordField).colspan(2).width(300).pad(5).row();
        root.add(passwordBtn).colspan(2).pad(5).row();
        root.add(errorLabel).colspan(2).padTop(10).row();
        root.add(deleteAccountBtn).colspan(2).padTop(20).row();
        root.add(backBtn).colspan(2).padTop(20).row();

        // --- Listeners ---
        usernameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String newName = usernameField.getText().trim();
                if (User.usernameExists(newName)) {
                    errorLabel.setText("Username '%s' already exists!".formatted(newName));
                } else {
                    user.setUsername(newName);
                }
            }
        });

        passwordBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@%$#&*()_]).{8,}$");
                String newPass = passwordField.getText();
                if (!passwordPattern.matcher(newPass).matches()) {
                    errorLabel.setText(
                            "Password must contain at least 1 lowercase, 1 uppercase, 1 digit, 1 special character and be 8+ characters.");
                } else {
                    user.setPassword(newPass);
                }
            }
        });

        applyAvatarBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileHandle file = Gdx.files.internal(avatarSelect.getSelected());
                System.out.println(file.path());
                updateAvatar(avatarSelect.getSelected());
                errorLabel.setText("Avatar changed.");
            }
        });

        uploadAvatarBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                filePicker.pickFile(file -> {
                    if (file.exists()) {
                        // user.setAvatar(file);
                        System.out.println(file.path());
                        updateAvatar(file.path());
                        errorLabel.setText("Custom avatar uploaded.");
                    } else {
                        errorLabel.setText("File not found.");
                    }
                });
            }
        });

        deleteAccountBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                errorLabel.setText("Account deletion not implemented.");
            }
        });

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // errorLabel.setText("Account deletion not implemented.");
                Main.getMain().setScreen(new MainMenuView(skin));
            }
        });
    }

    private void updateAvatar(String path) {
        if (avatarTexture != null)
            avatarTexture.dispose();
        user.setAvatarFilePath(path);
        avatarTexture = user.getAvatarTexture();
        int width = avatarTexture.getWidth();
        int height = avatarTexture.getHeight();
        float finalImageHeight = 128;
        float scalingFactor = finalImageHeight / height;
        // heroImage.setDrawable(new Image(texture).getDrawable());
        // heroLabel.setText(heros.get(currentHeroIndex).getName());
        avatarImage.setDrawable(new Image(avatarTexture).getDrawable());
        avatarImage.setSize(width * scalingFactor, height * scalingFactor);
    }

    // private class DragDropListener extends InputAdapter {

    // // @Override
    // public boolean fileDropped(String path) {
    // FileHandle file = Gdx.files.absolute(path);
    // if (file.exists()) {
    // // user.setAvatar(file);
    // updateAvatar();
    // errorLabel.setText("Avatar updated by drag & drop.");
    // return true;
    // }
    // return false;
    // }
    // }

    @Override
    public void render(float delta) {
        stage.getBatch().begin();
        stage.getBatch().draw(GameAssetManager.getInstance().getBackgroundImage(), 0, 0, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        if (avatarTexture != null)
            avatarTexture.dispose();
    }
}
