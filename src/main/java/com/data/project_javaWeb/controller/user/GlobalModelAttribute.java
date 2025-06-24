package com.data.project_javaWeb.controller.user;

import com.data.project_javaWeb.entity.Account;
import com.data.project_javaWeb.repository.util.login.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttribute {
    @Autowired
    private LoginRepository accountRepository;

    @ModelAttribute
    public void addCandidateNameToModel(
            @CookieValue(value = "rememberEmail", required = false) String emailFromCookie,
            Model model
    ) {
        String candidateName = null;
        if (emailFromCookie != null) {
            Account account = accountRepository.findByEmail(emailFromCookie);
            if (account != null && account.getCandidate() != null) {
                candidateName = account.getCandidate().getName();
            }
        }
        model.addAttribute("candidateName", candidateName);
    }
}
