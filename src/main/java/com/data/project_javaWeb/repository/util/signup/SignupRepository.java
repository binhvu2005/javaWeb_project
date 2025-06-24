package com.data.project_javaWeb.repository.util.signup;

import com.data.project_javaWeb.entity.Account;

public interface SignupRepository {
    Account findByEmail(String email);
    void save(Account account);
}