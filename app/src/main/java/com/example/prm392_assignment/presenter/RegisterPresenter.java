package com.example.prm392_assignment.presenter;

import android.content.Context;
import com.example.prm392_assignment.model.User;
import com.example.prm392_assignment.model.UserRepository;

public class RegisterPresenter {
    private UserRepository userRepository;
    private RegisterView view;

    public interface RegisterView {
        void onRegisterSuccess(User user);
        void onRegisterError(String message);
    }

    public RegisterPresenter(Context context, RegisterView view) {
        this.userRepository = new UserRepository(context);
        this.view = view;
    }

    public void register(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            view.onRegisterError("All fields are required");
            return;
        }
        if (userRepository.isEmailExists(email)) {
            view.onRegisterError("Email already exists");
            return;
        }
        User user = new User(0, name, email, password, "Customer");
        long id = userRepository.registerUser(user);
        if (id > 0) {
            user.setId((int) id);
            view.onRegisterSuccess(user);
        } else {
            view.onRegisterError("Registration failed");
        }
    }
}

