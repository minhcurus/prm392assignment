package com.example.prm392_assignment.presenter;

import android.content.Context;
import com.example.prm392_assignment.model.User;
import com.example.prm392_assignment.model.UserRepository;

public class LoginPresenter {
    private UserRepository userRepository;
    private LoginView view;

    public interface LoginView {
        void onLoginSuccess(User user);
        void onLoginError(String message);
    }

    public LoginPresenter(Context context, LoginView view) {
        this.userRepository = new UserRepository(context);
        this.view = view;
    }

    public void login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            view.onLoginError("Email and password cannot be empty");
            return;
        }
        User user = userRepository.loginUser(email, password);
        if (user != null) {
            view.onLoginSuccess(user);
        } else {
            view.onLoginError("Invalid email or password");
        }
    }
}

