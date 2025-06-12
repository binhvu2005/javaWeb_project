package com.data.project_javaWeb.repository.util;

import com.data.project_javaWeb.entity.Account;

public interface LoginRepository {
    Account findByEmail(String email);
    boolean validatePassword(String rawPassword, String storedPassword);
    void updateLoginStatus(String email, boolean status);
    void saveLoginAttempt(String email, boolean success);
}
