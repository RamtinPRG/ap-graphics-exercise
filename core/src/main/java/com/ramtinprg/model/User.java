package com.ramtinprg.model;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class User {

    static ArrayList<User> registeredUsers = new ArrayList<>();

    String username;
    String password;
    String securityQuestion;
    String securityQuestionAnswer;
    // int avatarIndex;
    String avatarFilePath;

    private User(String username, String password, String securityQuestion, String securityQuestionAnswer,
            String avatarFilePath) {
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityQuestionAnswer = securityQuestionAnswer;
        // this.avatarIndex = avatarIndex;
        this.avatarFilePath = avatarFilePath;
    }

    public static User register(String username, String password, String securityQuestion,
            String securityQuestionAnswer, String avatarFilePath) {
        if (usernameExists(username)) {
            return null;
        }
        User newUser = new User(username, password, securityQuestion, securityQuestionAnswer, avatarFilePath);
        registeredUsers.add(newUser);
        return newUser;
    }

    public static boolean usernameExists(String username) {
        for (User user : registeredUsers) {
            if (user.username.equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static User getByUsername(String username) {
        for (User user : registeredUsers) {
            if (user.username.equals(username)) {
                return user;
            }
        }
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityQuestionAnswer() {
        return securityQuestionAnswer;
    }

    public void setSecurityQuestionAnswer(String securityQuestionAnswer) {
        this.securityQuestionAnswer = securityQuestionAnswer;
    }

    public Texture getAvatarTexture() {
        return new Texture(Gdx.files.internal(avatarFilePath));
    }

    public void setAvatarFilePath(String avatarFilePath) {
        this.avatarFilePath = avatarFilePath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
