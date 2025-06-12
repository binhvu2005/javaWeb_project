package com.data.project_javaWeb.service.util;

import com.data.project_javaWeb.entity.Account;
import com.data.project_javaWeb.repository.util.LoginRepository;
import org.springframework.stereotype.Service;

@Service

public class LoginServiceImp implements LoginService{
    private final LoginRepository loginRepository;
    public LoginServiceImp(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public Account findByEmail(String email) {
        return loginRepository.findByEmail(email);
    }

    @Override
    public boolean validatePassword(String rawPassword, String storedPassword) {
        return loginRepository.validatePassword(rawPassword, storedPassword);
    }

    @Override
    public void updateLoginStatus(String email, boolean status) {
        loginRepository.updateLoginStatus(email, status);
    }

    @Override
    public void saveLoginAttempt(String email, boolean success) {
        loginRepository.saveLoginAttempt(email, success);
    }
}
