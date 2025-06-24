package com.data.project_javaWeb.service.util.login;

import com.data.project_javaWeb.entity.Account;

public interface LoginService {
    Account findByEmail(String email);
    boolean validatePassword(String rawPassword, String storedPassword);
    void updateLoginStatus(String email, boolean status);
    void saveLoginAttempt(String email, boolean success);
     void update(Account account);
}
