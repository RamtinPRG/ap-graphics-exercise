package com.ramtinprg.model;

import java.util.ArrayList;

public class User {

    static ArrayList<User> registeredUsers = new ArrayList<>();

    String username;
    String password;
    String securityQuestion;
    String securityQuestionAnswer;
    int avatarIndex;

    private User(String username, String password, String securityQuestion, String securityQuestionAnswer, int avatarIndex) {
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityQuestionAnswer = securityQuestionAnswer;
        this.avatarIndex = avatarIndex;
    }

    public static User register(String username, String password, String securityQuestion, String securityQuestionAnswer, int avatarIndex) {
        if (usernameExists(username)) {
            return null;
        }
        User newUser = new User(username, password, securityQuestion, securityQuestionAnswer, avatarIndex);
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

    public int getAvatarIndex() {
        return avatarIndex;
    }

    public void setAvatarIndex(int avatarIndex) {
        this.avatarIndex = avatarIndex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
