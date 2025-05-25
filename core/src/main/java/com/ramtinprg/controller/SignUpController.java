package com.ramtinprg.controller;

import java.util.regex.Pattern;

import com.ramtinprg.model.User;
import com.ramtinprg.view.SignUpView;

public class SignUpController {

    private final SignUpView view;
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@%$#&*()_]).{8,}$");

    public SignUpController(SignUpView view) {
        this.view = view;
    }

    public void handleSubmit(String username, String password, String question, String answer, int avatarIndex) {
        if (username.isEmpty() || answer.isEmpty() || password.isEmpty()) {
            view.showError("All fields must be filled.");
        } else if (!passwordPattern.matcher(password).matches()) {
            view.showError("Password must contain at least 1 lowercase, 1 uppercase, 1 digit, 1 special character and be 8+ characters.");
        } else if (User.usernameExists(username)) {
            view.showError("Username '%s' already exists!".formatted(username));
        } else {
            User user = User.register(username, password, question, answer, avatarIndex);
            view.clearError();
            view.openLoginView();
        }
    }

    public SignUpView getView() {
        return view;
    }

}
