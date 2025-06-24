package com.data.project_javaWeb.service.util.signup;

import com.data.project_javaWeb.dto.AccountDTO;

public interface SignupService {
    boolean register(AccountDTO dto);
}