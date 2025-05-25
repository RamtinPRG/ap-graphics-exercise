package com.ramtinprg.controller;

import com.ramtinprg.Main;
import com.ramtinprg.model.User;
import com.ramtinprg.view.LoginView;

public class LoginController {

    private final LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
    }

    public void handleSubmit(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showError("All fields must be filled.");
        } else if (!User.usernameExists(username)) {
            view.showError("Username does not exist.");
        } else if (!User.getByUsername(username).getPassword().equals(password)) {
            view.showError("Wrong password!");
        } else {
            view.clearError();
            Main.getMain().setLoggedUser(User.getByUsername(username));
            view.openMainMenuView();
        }
    }

    public LoginView getView() {
        return view;
    }
}
